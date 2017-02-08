package org.spawl.bungeepackets.api.nbt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NBTTagDouble extends NBTNumber
{
	public static final NBTType TYPE = NBTType.DOUBLE;
	private double value;

	@Override
	void write(DataOutput out)
			throws IOException
	{
		out.writeDouble(value);
	}

	@Override
	void load(DataInput in, int depth, NBTReadLimiter limiter)
			throws IOException
	{
		limiter.increment(8);
		value = in.readDouble();
	}

	@Override
	public NBTType getType()
	{
		return TYPE;
	}

	@Override
	public String toString()
	{
		return String.valueOf(value) + 'd';
	}

	@Override
	public long longValue()
	{
		return (long) value;
	}

	@Override
	public int intValue()
	{
		return (int) value;
	}

	@Override
	public short shortValue()
	{
		return (short) value;
	}

	@Override
	public byte byteValue()
	{
		return (byte) value;
	}

	@Override
	public double doubleValue()
	{
		return value;
	}

	@Override
	public float floatValue()
	{
		return (float) value;
	}
}