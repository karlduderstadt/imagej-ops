/*-
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2020 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.morphology.outline;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBounds;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.util.Util;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The Op creates an output interval where the objects are hollow versions from
 * the input. Rectangles become outlines, solid cubes become surfaces etc.
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Ops.Morphology.Outline.class)
public class Outline<B extends BooleanType<B>> extends
	AbstractBinaryHybridCF<RandomAccessibleInterval<B>, Boolean, RandomAccessibleInterval<BitType>>
	implements Ops.Morphology.Outline
{

	@Parameter
	private ThreadService threadService;

	@Parameter
	private LogService logService;

	@Override
	public RandomAccessibleInterval<BitType> createOutput(
		final RandomAccessibleInterval<B> input, final Boolean input2)
	{
		final long[] dims = new long[input.numDimensions()];
		input.dimensions(dims);
		final FinalDimensions dimensions = new FinalDimensions(dims);
		return ops().create().img(dimensions, new BitType());
	}

	/**
	 * Copies the outlines of the objects in the input interval into the output
	 *
	 * @param input an N-dimensional binary interval
	 * @param excludeEdges are elements on stack edges outline or not
	 *          <p>
	 *          For example, a 2D square:<br>
	 *          0 0 0 0<br>
	 *          1 1 1 0<br>
	 *          E 1 1 0<br>
	 *          1 1 1 0<br>
	 *          0 0 0 0<br>
	 *          Element E is removed if parameter true, kept if false
	 *          </p>
	 * @param output outlines of the objects in interval
	 */
	@Override
	public void compute(final RandomAccessibleInterval<B> input,
		final Boolean excludeEdges,
		final RandomAccessibleInterval<BitType> output)
	{
		final ExtendedRandomAccessibleInterval<B, RandomAccessibleInterval<B>> extendedInput =
				extendInterval(input);
		final List<Future<?>> futures = new ArrayList<>();
		final IterableInterval<B> iterable = Views.iterable(input);
		final int cores = Runtime.getRuntime().availableProcessors();
		final int nThreads = (int) Math.min(cores, iterable.size());
		for (int thread = 0; thread < nThreads; thread++) {
			final Cursor<B> cursor = iterable.cursor();
			final OutOfBounds<B> inputAccess = extendedInput.randomAccess();
			final RandomAccess<BitType> outputAccess = output.randomAccess();
			final Runnable runnable = createTask(cursor, inputAccess,
					outputAccess, thread, nThreads);
			futures.add(threadService.run(runnable));
		}

		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				logService.error(e);
			}
		}
	}

	/**
	 * Creates a task that finds and copies outline pixels to the output.
	 *
	 * @param inputCursor Cursor to the input image
	 * @param inputAccess Random access to the extended input image
	 * @param output Access to the output image
	 * @param start Which pixel thread should start from
	 * @param jump Number of pixels the thread jumps after each access
	 * @param <B> Type of elements in the input image
	 * @return a task for parallel processing.
	 */
	// region -- Helper methods --
	private static <B extends BooleanType<B>> Runnable createTask(
			final Cursor<B> inputCursor, final OutOfBounds<B> inputAccess,
			final RandomAccess<BitType> output, final long start,
			final int jump) {
		return () -> {
			final long[] coordinates = new long[inputAccess.numDimensions()];
			inputCursor.jumpFwd(start + 1);
			while (inputCursor.hasNext()) {
				if (inputCursor.get().get()) {
					inputCursor.localize(coordinates);
					inputAccess.setPosition(coordinates);
					if (isAnyNeighborBackground(inputAccess, coordinates)) {
						output.setPosition(coordinates);
						output.get().set(inputCursor.get().get());
					}
				}
				inputCursor.jumpFwd(jump);
			}
		};
	}

	private ExtendedRandomAccessibleInterval<B, RandomAccessibleInterval<B>>
		extendInterval(RandomAccessibleInterval<B> interval)
	{
		final B type = Util.getTypeFromInterval(interval).createVariable();
		type.set(in2());
		return Views.extendValue(interval, type);
	}

	/** Checks if any element in the N-dimensional neighbourhood is background */
	private static <B extends BooleanType<B>> boolean isAnyNeighborBackground(
			final OutOfBounds<B> access, final long[] position)
	{
		final int dimension = access.numDimensions() - 1;
		return isAnyNeighborBackground(dimension, access, position);
	}

	private static <B extends BooleanType<B>> boolean isAnyNeighborBackground(
			final int dimension, final OutOfBounds<B> access,
			final long[] position) {
		for (long p = -1; p <= 1; p++) {
			access.setPosition(position[dimension] + p, dimension);
			if (dimension == 0) {
				if (!access.get().get()) {
					return true;
				}
			} else {
				if (isAnyNeighborBackground(dimension - 1, access, position)) {
					return true;
				}
			}
		}
		return false;
	}
	// endregion
}
