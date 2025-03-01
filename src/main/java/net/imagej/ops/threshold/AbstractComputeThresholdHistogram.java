/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2021 ImageJ2 developers.
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

package net.imagej.ops.threshold;

import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.type.numeric.RealType;

/**
 * Abstract superclass of {@link ComputeThresholdHistogram} implementations.
 *
 * @author Curtis Rueden
 */
public abstract class AbstractComputeThresholdHistogram<T extends RealType<T>>
	extends AbstractUnaryHybridCF<Histogram1d<T>, T> implements
	ComputeThresholdHistogram<T>
{

	// -- UnaryComputerOp methods --

	@Override
	public void compute(final Histogram1d<T> input, final T output) {
		final long binPos = computeBin(input);

		// convert bin number to corresponding gray level
		input.getCenterValue(binPos, output);
	}

	// -- UnaryOutputFactory methods --

	@Override
	public T createOutput(final Histogram1d<T> input) {
		return input.firstDataValue().createVariable();
	}

}
