package org.spawl.bungeepackets.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase
{
	abstract void write(DataOutput paramDataOutput)
			throws IOException;

	abstract void load(DataInput paramDataInput, int paramInt, NBTReadLimiter paramNBTReadLimiter)
			throws IOException;

	public byte getTypeId()
	{
		return getType().getID();
	}

	public abstract NBTType getType();
}