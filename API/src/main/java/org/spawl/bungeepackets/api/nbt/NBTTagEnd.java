package org.spawl.bungeepackets.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagEnd extends NBTBase
{
	public static final NBTType TYPE = NBTType.END;
	public static final NBTTagEnd INSTANCE = new NBTTagEnd();

	private NBTTagEnd() {}

	@Override
	void load(DataInput in, int depth, NBTReadLimiter limiter) {}

	@Override
	void write(DataOutput out) {}

	@Override
	public NBTType getType()
	{
		return TYPE;
	}

	@Override
	public String toString()
	{
		return "END";
	}
}