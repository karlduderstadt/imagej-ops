/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.imagemoments.normalizedcentralmoments;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.ImageMoments.CentralMoment00;
import net.imagej.ops.Ops.ImageMoments.CentralMoment30;
import net.imagej.ops.Ops.ImageMoments.NormalizedCentralMoment30;
import net.imagej.ops.RTs;
import net.imagej.ops.imagemoments.AbstractImageMomentOp;
import net.imagej.ops.imagemoments.ImageMomentOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@link NormalizedCentralMoment30}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @author Christian Dietz, University of Konstanz.
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = NormalizedCentralMoment30.class,
	label = "Image Moment: NormalizedCentralMoment30")
public class DefaultNormalizedCentralMoment30<I extends RealType<I>, O extends RealType<O>>
	extends AbstractImageMomentOp<I, O> implements NormalizedCentralMoment30
{

	private FunctionOp<IterableInterval<I>, O> centralMoment00Func;

	private FunctionOp<IterableInterval<I>, O> centralMoment30Func;

	@Override
	public void initialize() {
		centralMoment00Func = RTs.function(ops(), CentralMoment00.class, in());
		centralMoment30Func = RTs.function(ops(), CentralMoment30.class, in());
	}

	@Override
	public void compute(final IterableInterval<I> input, final O output) {
		double centralMoment00 = centralMoment00Func.compute(input).getRealDouble();
		double centralMoment30 = centralMoment30Func.compute(input).getRealDouble();

		output.setReal(centralMoment30 /
			Math.pow(centralMoment00, 1 + ((3 + 0) / 2)));
	}
}
