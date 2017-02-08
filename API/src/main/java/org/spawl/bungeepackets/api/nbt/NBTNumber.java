package org.spawl.bungeepackets.api.nbt;

public abstract class NBTNumber extends NBTBase
{
	public abstract long longValue();

	public abstract int intValue();

	public abstract short shortValue();

	public abstract byte byteValue();

	public abstract double doubleValue();

	public abstract float floatValue();
}