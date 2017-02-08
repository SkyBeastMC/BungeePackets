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
public class NBTTagByte extends NBTNumber
{
	public static final NBTType TYPE = NBTType.BYTE;
	private byte value;

	@Override
	void write(DataOutput out)
			throws IOException
	{
		out.writeByte(value);
	}

	@Override
	void load(DataInput in, int depth, NBTReadLimiter limiter)
			throws IOException
	{
		limiter.increment(1);
		value = in.readByte();
	}

	@Override
	public NBTType getType()
	{
		return TYPE;
	}

	@Override
	public String toString()
	{
		return String.valueOf(value) + 'b';
	}

	@Override
	public long longValue()
	{
		return value;
	}

	@Override
	public int intValue()
	{
		return value;
	}

	@Override
	public short shortValue()
	{
		return value;
	}

	@Override
	public byte byteValue()
	{
		return value;
	}

	@Override
	public double doubleValue()
	{
		return value;
	}

	@Override
	public float floatValue()
	{
		return value;
	}
}