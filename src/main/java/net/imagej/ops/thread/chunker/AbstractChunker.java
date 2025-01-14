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

package net.imagej.ops.thread.chunker;

import net.imagej.ops.AbstractOp;

import org.scijava.plugin.Parameter;
import org.scijava.thread.ThreadService;

/**
 * Abstract {@link ChunkerOp}.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public abstract class AbstractChunker extends AbstractOp implements ChunkerOp {

	/**
	 * ThreadService used for multi-threading
	 */
	@Parameter
	protected ThreadService threadService;

	/**
	 * {@link Chunk} to be executed
	 */
	@Parameter
	protected Chunk chunkable;

	/**
	 * Total number of elements to be processed
	 */
	@Parameter
	protected long numberOfElements;

	/** Reason for cancelation, or null if not canceled. */
	private String cancelReason;

	// -- Chunker methods --

	@Override
	public void setChunk(final Chunk definition) {
		this.chunkable = definition;
	}

	@Override
	public void setNumberOfElements(final int totalSize) {
		this.numberOfElements = totalSize;
	}

	// -- Cancelable methods --

	@Override
	public boolean isCanceled() {
		return cancelReason != null;
	}

	/** Cancels the command execution, with the given reason for doing so. */
	@Override
	public void cancel(final String reason) {
		cancelReason = reason;
	}

	@Override
	public String getCancelReason() {
		return cancelReason;
	}

}
