/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 ImageJ developers.
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

package net.imagej.ops.image.invert;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops;
import net.imagej.types.UnboundedIntegerType;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.Unsigned128BitType;
import net.imglib2.type.numeric.integer.Unsigned12BitType;
import net.imglib2.type.numeric.integer.Unsigned2BitType;
import net.imglib2.type.numeric.integer.Unsigned4BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedLongType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.integer.UnsignedVariableBitLengthType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

/**
 * @author Martin Horn (University of Konstanz)
 * @author Gabe Selzer
 */
public class InvertTest extends AbstractOpTest {

	@Test
	public void testBitTypeInvert() {
		final Img<BitType> inBitType = generateBitArrayTestImg(true, 10, 10);
		final Img<BitType> outBitType = inBitType.factory().create(inBitType,
			new BitType());
		assertDefaultInvert(inBitType, outBitType);
		assertDefaultInvertMinMaxProvided(inBitType, outBitType, new BitType(false),
			new BitType(true));
		assertDefaultInvertMinMaxProvided(inBitType, outBitType, new BitType(false),
			new BitType(false));
	}

	@Test
	public void testByteTypeInvert() {
		final Img<ByteType> inByteType = generateByteArrayTestImg(true, 5, 5);
		final Img<ByteType> outByteType = inByteType.factory().create(inByteType,
			new ByteType());
		assertDefaultInvert(inByteType, outByteType);
		assertDefaultInvertMinMaxProvided(inByteType, outByteType, new ByteType(
			(byte) 0), new ByteType((byte) 0));
		assertDefaultInvertMinMaxProvided(inByteType, outByteType, new ByteType(
			(byte) 20), new ByteType((byte) 10));
		assertDefaultInvertMinMaxProvided(inByteType, outByteType, new ByteType(
			(byte) 256), new ByteType((byte) 256));
	}

	@Test
	public void testUnsigned2BitTypeInvert() {
		final Img<Unsigned2BitType> inUnsigned2BitType =
			generateUnsigned2BitArrayTestImg(true, 5, 5);
		final Img<Unsigned2BitType> outUnsigned2BitType = inUnsigned2BitType
			.factory().create(inUnsigned2BitType, new Unsigned2BitType());
		assertDefaultInvert(inUnsigned2BitType, outUnsigned2BitType);
		assertDefaultInvertMinMaxProvided(inUnsigned2BitType, outUnsigned2BitType,
			new Unsigned2BitType(2), new Unsigned2BitType(3));
	}

	@Test
	public void testUnsigned4BitTypeInvert() {
		final Img<Unsigned4BitType> inUnsigned4BitType =
			generateUnsigned4BitArrayTestImg(true, 5, 5);
		final Img<Unsigned4BitType> outUnsigned4BitType = inUnsigned4BitType
			.factory().create(inUnsigned4BitType, new Unsigned4BitType());
		assertDefaultInvert(inUnsigned4BitType, outUnsigned4BitType);
		assertDefaultInvertMinMaxProvided(inUnsigned4BitType, outUnsigned4BitType,
			new Unsigned4BitType(14), new Unsigned4BitType(15));
	}

	@Test
	public void testUnsigned12BitTypeInvert() {
		final Img<Unsigned12BitType> inUnsigned12BitType =
			generateUnsigned12BitArrayTestImg(true, 5, 5);
		final Img<Unsigned12BitType> outUnsigned12BitType = inUnsigned12BitType
			.factory().create(inUnsigned12BitType, new Unsigned12BitType());
		assertDefaultInvert(inUnsigned12BitType, outUnsigned12BitType);
		assertDefaultInvertMinMaxProvided(inUnsigned12BitType, outUnsigned12BitType,
			new Unsigned12BitType(3025), new Unsigned12BitType(3846));
	}

	@Test
	public void
		testUnsigned12BitTypeInvertToUnsignedVariableBitLengthTypeInvert()
	{
		final Img<Unsigned12BitType> inUnsigned12BitType =
			generateUnsigned12BitArrayTestImg(true, 5, 5);
		final Img<UnsignedVariableBitLengthType> outUnsignedVariableBitLengthType =
			generateUnsignedVariableBitLengthTypeArrayTestImg(false, 64, 5, 5);
		assertIntegerInvert(inUnsigned12BitType, outUnsignedVariableBitLengthType);
		assertIntegerInvertMinMaxProvided(inUnsigned12BitType,
			outUnsignedVariableBitLengthType, new Unsigned12BitType(1),
			new Unsigned12BitType(17));
	}

	@Test
	public void testUnsignedByteTypeInvert() {
		final Img<UnsignedByteType> inUnsignedByteType =
			generateUnsignedByteArrayTestImg(true, 5, 5);
		final Img<UnsignedByteType> outUnsignedByteType = inUnsignedByteType
			.factory().create(inUnsignedByteType, new UnsignedByteType());
		assertDefaultInvert(inUnsignedByteType, outUnsignedByteType);
		assertDefaultInvertMinMaxProvided(inUnsignedByteType, outUnsignedByteType,
			new UnsignedByteType((byte) 127), new UnsignedByteType((byte) 127));
		assertDefaultInvertMinMaxProvided(inUnsignedByteType, outUnsignedByteType,
			new UnsignedByteType((byte) -12), new UnsignedByteType((byte) -10));
	}

	@Test
	public void testDoubleTypeInvert() {
		final Img<DoubleType> inDoubleType = generateDoubleArrayTestImg(true, 5, 5);
		final Img<DoubleType> outDoubleType = inDoubleType.factory().create(
			inDoubleType, new DoubleType());
		assertDefaultInvert(inDoubleType, outDoubleType);
		assertDefaultInvertMinMaxProvided(inDoubleType, outDoubleType,
			new DoubleType(437d), new DoubleType(8008d));
		assertDefaultInvertMinMaxProvided(inDoubleType, outDoubleType,
			new DoubleType(5d), new DoubleType(Double.MAX_VALUE));
	}

	@Test
	public void testDoubleTypeCustomInvert() {
		final Img<DoubleType> inDoubleType = generateDoubleArrayTestImg(true, 5, 5);
		final Img<DoubleType> outDoubleType = inDoubleType.factory().create(
			inDoubleType, new DoubleType());

		// set this array of doubles to be the pixel values.
		final double[] nums = new double[] { Double.MAX_VALUE, Double.MIN_VALUE, 1d,
			-1d, 0d, Double.MAX_VALUE + 1, -Double.MAX_VALUE - 1, Math.PI, Math.E,
			Math.sqrt(Math.PI), Math.pow(25, 25), 2, 3, 5, 8, 13, 21, 34, 55, 89, 144,
			Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
			Double.NEGATIVE_INFINITY };
		final Cursor<DoubleType> c = inDoubleType.localizingCursor();
		for (final double i : nums) {
			c.next();
			c.get().set(i);
		}
		assertDefaultInvert(inDoubleType, outDoubleType);
		assertDefaultInvertMinMaxProvided(inDoubleType, outDoubleType,
			new DoubleType(437d), new DoubleType(8008d));
	}

	@Test
	public void testDoubleTypeToIntTypeInvert() {
		final Img<DoubleType> inDoubleType = generateDoubleArrayTestImg(true, 5, 5);
		final Img<IntType> outIntType = generateIntArrayTestImg(false, 5, 5);
		assertDefaultInvert(inDoubleType, outIntType);
	}

	@Test
	public void testFloatTypeInvert() {
		final Img<FloatType> inFloatType = generateFloatArrayTestImg(true, 5, 5);
		final Img<FloatType> outFloatType = inFloatType.factory().create(
			inFloatType, new FloatType());
		assertDefaultInvert(inFloatType, outFloatType);
		assertDefaultInvertMinMaxProvided(inFloatType, outFloatType, new FloatType(
			0f), new FloatType(1f));
	}

	@Test
	public void testIntTypeInvert() {
		final Img<IntType> inIntType = generateIntArrayTestImg(true, 5, 5);
		final Img<IntType> outIntType = inIntType.factory().create(inIntType,
			new IntType());
		assertDefaultInvert(inIntType, outIntType);
		assertDefaultInvertMinMaxProvided(inIntType, outIntType, new IntType(10),
			new IntType(40));
		assertDefaultInvertMinMaxProvided(inIntType, outIntType, new IntType(
			Integer.MIN_VALUE), new IntType(-10));
	}

	@Test
	public void testIntTypeToDoubleTypeInvert() {
		final Img<IntType> inIntType = generateIntArrayTestImg(true, 5, 5);
		final Img<DoubleType> outDoubleType = generateDoubleArrayTestImg(false, 5,
			5);
		assertDefaultInvert(inIntType, outDoubleType);
		assertDefaultInvertMinMaxProvided(inIntType, outDoubleType, new IntType(
			Integer.MAX_VALUE), new IntType(Integer.MAX_VALUE));
	}

	@Test
	public void testIntTypeToUnboundedIntegerTypeInvert() {
		final Img<IntType> inIntType = generateIntArrayTestImg(false, 5, 5);
		final Img<UnboundedIntegerType> outUnboundedIntegerType =
			generateUnboundedIntegerTypeListTestImg(true, 5, 5);
		assertIntegerInvert(inIntType, outUnboundedIntegerType);
		assertIntegerInvertMinMaxProvided(inIntType, outUnboundedIntegerType,
			new IntType(Integer.MAX_VALUE - 1), new IntType(Integer.MAX_VALUE));
	}

	@Test
	public void testUnsignedIntTypeInvert() {
		final Img<UnsignedIntType> inUnsignedIntType =
			generateUnsignedIntArrayTestImg(true, 5, 5);
		final Img<UnsignedIntType> outUnsignedIntType = inUnsignedIntType.factory()
			.create(inUnsignedIntType, new UnsignedIntType());
		assertDefaultInvert(inUnsignedIntType, outUnsignedIntType);
		assertDefaultInvertMinMaxProvided(inUnsignedIntType, outUnsignedIntType,
			new UnsignedIntType(237), new UnsignedIntType(257));
		assertDefaultInvertMinMaxProvided(inUnsignedIntType, outUnsignedIntType,
			new UnsignedIntType(10), new UnsignedIntType(-10));
	}

	@Test
	public void testLongTypeInvert() {
		final Img<LongType> inLongType = generateLongArrayTestImg(true, 5, 5);
		final Img<LongType> outLongType = inLongType.factory().create(inLongType,
			new LongType());
		assertIntegerInvert(inLongType, outLongType);
		assertIntegerInvertMinMaxProvided(inLongType, outLongType, new LongType(
			3025), new LongType(3846));
	}

	@Test
	public void testUnsignedLongTypeInvert() {
		final Img<UnsignedLongType> inUnsignedLongType =
			generateUnsignedLongArrayTestImg(true, 5, 5);
		final Img<UnsignedLongType> outUnsignedLongType = inUnsignedLongType
			.factory().create(inUnsignedLongType, new UnsignedLongType());
		assertIntegerInvert(inUnsignedLongType, outUnsignedLongType);
		assertIntegerInvertMinMaxProvided(inUnsignedLongType, outUnsignedLongType,
			new UnsignedLongType(3025), new UnsignedLongType(3846));
	}

	@Test
	public void testUnsigned128ByteTypeInvert() {
		final Img<Unsigned128BitType> inUnsigned128BitType =
			generateUnsigned128BitArrayTestImg(true, 5, 5);
		final Img<Unsigned128BitType> outUnsigned128BitType = inUnsigned128BitType
			.factory().create(inUnsigned128BitType, new Unsigned128BitType());
		assertIntegerInvert(inUnsigned128BitType, outUnsigned128BitType);
		assertIntegerInvertMinMaxProvided(inUnsigned128BitType,
			outUnsigned128BitType, new Unsigned128BitType(BigInteger.valueOf(3025)),
			new Unsigned128BitType(BigInteger.valueOf(3468)));
	}

	@Test
	public void testShortTypeInvert() {
		final Img<ShortType> inShortType = generateShortArrayTestImg(true, 5, 5);
		final Img<ShortType> outShortType = inShortType.factory().create(
			inShortType, new ShortType());
		assertDefaultInvert(inShortType, outShortType);
		assertDefaultInvertMinMaxProvided(inShortType, outShortType, new ShortType(
			(Short.MIN_VALUE)), new ShortType((short) (Short.MIN_VALUE + 1)));
		assertDefaultInvertMinMaxProvided(inShortType, outShortType, new ShortType(
			(Short.MAX_VALUE)), new ShortType((short) (Short.MAX_VALUE - 1)));
		assertDefaultInvertMinMaxProvided(inShortType, outShortType, new ShortType(
			(Short.MAX_VALUE)), new ShortType((Short.MAX_VALUE)));
	}

	@Test
	public void testShortTypeToUnsignedShortTypeInvert() {
		final Img<ShortType> inShortType = generateShortArrayTestImg(true, 5, 5);
		final Img<UnsignedShortType> outUnsignedShortType =
			generateUnsignedShortArrayTestImg(false, 5, 5);
		assertDefaultInvert(inShortType, outUnsignedShortType);
		assertDefaultInvertMinMaxProvided(inShortType, outUnsignedShortType,
			new ShortType((Short.MAX_VALUE)), new ShortType((Short.MAX_VALUE)));
		assertDefaultInvertMinMaxProvided(inShortType, outUnsignedShortType,
			new ShortType((short) (-5)), new ShortType((short) -3));
	}

	@Test
	public void testUnsignedShortTypeInvert() {
		final Img<UnsignedShortType> inUnsignedShortType =
			generateUnsignedShortArrayTestImg(true, 5, 5);
		final Img<UnsignedShortType> outUnsignedShortType = inUnsignedShortType
			.factory().create(inUnsignedShortType, new UnsignedShortType());
		assertDefaultInvert(inUnsignedShortType, outUnsignedShortType);
		assertDefaultInvertMinMaxProvided(inUnsignedShortType, outUnsignedShortType,
			new UnsignedShortType((short) 437), new UnsignedShortType((short) 8008));
	}

	@Test
	public void testUnsignedShortTypeToShortTypeInvert() {
		final Img<UnsignedShortType> inUnsignedShortType =
			generateUnsignedShortArrayTestImg(true, 5, 5);
		final Img<ShortType> outShortType = generateShortArrayTestImg(false, 5, 5);
		assertDefaultInvert(inUnsignedShortType, outShortType);
		assertDefaultInvertMinMaxProvided(inUnsignedShortType, outShortType,
			new UnsignedShortType((short) 65540), new UnsignedShortType(
				(short) 70000));
	}

	@Test
	public void testUnboundedIntegerTypeInvert() {
		final Img<UnboundedIntegerType> inUnboundedIntegerType =
			generateUnboundedIntegerTypeListTestImg(true, 5, 5);
		final Img<UnboundedIntegerType> outUnboundedIntegerType =
			inUnboundedIntegerType.factory().create(inUnboundedIntegerType,
				new UnboundedIntegerType());
		assertIntegerInvert(inUnboundedIntegerType, outUnboundedIntegerType);
		assertIntegerInvertMinMaxProvided(inUnboundedIntegerType,
			outUnboundedIntegerType, new UnboundedIntegerType(437),
			new UnboundedIntegerType(8008));
		assertIntegerInvertMinMaxProvided(inUnboundedIntegerType,
			outUnboundedIntegerType, new UnboundedIntegerType(0),
			new UnboundedIntegerType(1));
	}

	@Test
	public void testUnboundedIntegerTypeToIntTypeInvert() {
		final Img<UnboundedIntegerType> inUnboundedIntegerType =
			generateUnboundedIntegerTypeListTestImg(true, 5, 5);
		final Img<IntType> outIntType = generateIntArrayTestImg(false, 5, 5);
		assertIntegerInvert(inUnboundedIntegerType, outIntType);
		assertIntegerInvertMinMaxProvided(inUnboundedIntegerType, outIntType,
			new UnboundedIntegerType(Integer.MAX_VALUE), new UnboundedIntegerType(
				Integer.MAX_VALUE + 1));
	}

	@Test
	public void testUnsignedVariableBitLengthTypeInvert() {
		final Img<UnsignedVariableBitLengthType> inUnsignedVariableBitLengthType =
			generateUnsignedVariableBitLengthTypeArrayTestImg(true, 64, 5, 5);
		final Img<UnsignedVariableBitLengthType> outUnsignedVariableBitLengthType =
			inUnsignedVariableBitLengthType.factory().create(
				inUnsignedVariableBitLengthType, new UnsignedVariableBitLengthType(1,
					64));
		assertIntegerInvert(inUnsignedVariableBitLengthType,
			outUnsignedVariableBitLengthType);
		assertIntegerInvertMinMaxProvided(inUnsignedVariableBitLengthType,
			outUnsignedVariableBitLengthType, new UnsignedVariableBitLengthType(
				((long) Math.pow(2, 64) - 1), 64), new UnsignedVariableBitLengthType(
					((long) Math.pow(2, 64) - 1), 64));
		assertIntegerInvertMinMaxProvided(inUnsignedVariableBitLengthType,
			outUnsignedVariableBitLengthType, new UnsignedVariableBitLengthType(
				123456789, 64), new UnsignedVariableBitLengthType(123456790, 64));
		assertIntegerInvertMinMaxProvided(inUnsignedVariableBitLengthType,
			outUnsignedVariableBitLengthType, new UnsignedVariableBitLengthType(4,
				12), new UnsignedVariableBitLengthType(6, 12));
		assertIntegerInvertMinMaxProvided(inUnsignedVariableBitLengthType,
			outUnsignedVariableBitLengthType, new UnsignedVariableBitLengthType(0, 6),
			new UnsignedVariableBitLengthType(1, 6));
	}

	@Test
	public void testUnsignedVariableBitLengthTypeToUnsigned12BitTypeInvert() {
		final Img<UnsignedVariableBitLengthType> inUnsignedVariableBitLengthType =
			generateUnsignedVariableBitLengthTypeArrayTestImg(true, 64, 5, 5);
		final Img<Unsigned12BitType> outUnsigned12BitType =
			generateUnsigned12BitArrayTestImg(true, 5, 5);
		assertIntegerInvert(inUnsignedVariableBitLengthType, outUnsigned12BitType);
		assertIntegerInvertMinMaxProvided(inUnsignedVariableBitLengthType,
			outUnsigned12BitType, new UnsignedVariableBitLengthType(4100, 13),
			new UnsignedVariableBitLengthType(4500, 13));
	}

	private <I extends RealType<I>, O extends RealType<O>> void
		assertDefaultInvert(final Img<I> in, final Img<O> out)
	{

		final I type = in.firstElement();
		final I min = type.copy();
		min.setReal(type.getMinValue());
		final I max = type.copy();
		max.setReal(type.getMaxValue());
		ops.run(InvertII.class, out, in);

		defaultCompare(in, out, min, max);
	}

	private <I extends RealType<I>, O extends RealType<O>> void
		assertDefaultInvertMinMaxProvided(final Img<I> in, final Img<O> out,
			final RealType<I> min, final RealType<I> max)
	{

		ops.run(InvertII.class, out, in, (min), (max));

		defaultCompare(in, out, (I) min, (I) max);
	}

	private <I extends RealType<I>, O extends RealType<O>> void defaultCompare(
		final Img<I> in, final Img<O> out, final I min, final I max)
	{
		final Cursor<I> inAccess = in.localizingCursor();
		final RandomAccess<O> outAccess = out.randomAccess();
		while (inAccess.hasNext()) {
			final I inVal = inAccess.next();
			outAccess.setPosition(inAccess);
			final O outVal = outAccess.get();
			final double bigIn = inVal.getRealDouble();
			final double minMax = min.getRealDouble() + max.getRealDouble() - bigIn;
			final double bigOut = outVal.getRealDouble();
			final O minMaxType = outVal.createVariable();
			minMaxType.setReal(minMax);
			if (minMax <= outVal.getMinValue()) assertEquals(outVal.getMinValue(),
				bigOut, 0.00005);
			else if (minMax >= outVal.getMaxValue()) assertEquals(outVal
				.getMaxValue(), bigOut, 0.00005);
			else assertEquals(minMaxType, outVal);
		}
	}

	private <I extends IntegerType<I>, O extends IntegerType<O>> void
		assertIntegerInvertMinMaxProvided(final Img<I> in, final Img<O> out,
			final I min, final I max)
	{

		// unsigned type test
		ops.run(Ops.Image.Invert.class, out, in, min, max);

		integerCompare(in, out, min, max);

	}

	private <I extends IntegerType<I>, O extends IntegerType<O>> void
		assertIntegerInvert(final Img<I> in, final Img<O> out)
	{

		ops.run(Ops.Image.Invert.class, out, in);

		integerCompare(in, out, null, null);
	}

	private <I extends IntegerType<I>, O extends IntegerType<O>> void
		integerCompare(final Img<I> in, final Img<O> out, final IntegerType<I> min,
			final IntegerType<I> max)
	{

		// Get min/max for the output type.
		final BigInteger minOut = InvertIIInteger.minValue(out.firstElement())
			.getBigInteger();
		final BigInteger maxOut = InvertIIInteger.maxValue(out.firstElement())
			.getBigInteger();
		BigInteger minMax = BigInteger.ZERO;

		// min + max
		if (min == null && max == null) {
			minMax = InvertIIInteger.minValue(in.firstElement()).getBigInteger().add(
				InvertIIInteger.maxValue(in.firstElement()).getBigInteger());
		}
		else {
			minMax = min.getBigInteger().add(max.getBigInteger());
		}

		final Cursor<I> inAccess = in.localizingCursor();
		final RandomAccess<O> outAccess = out.randomAccess();
		while (inAccess.hasNext()) {
			final I inVal = inAccess.next();
			outAccess.setPosition(inAccess);
			final O outVal = outAccess.get();
			final BigInteger bigIn = inVal.getBigInteger();
			final BigInteger bigOut = outVal.getBigInteger();
			final BigInteger calcOut = minMax.subtract(bigIn);
			if (calcOut.compareTo(minOut) <= 0) assertEquals(minOut, bigOut);
			else if (calcOut.compareTo(maxOut) >= 0) assertEquals(maxOut, bigOut);
			else {

				assertEquals(calcOut, bigOut);

			}
		}
	}
}
