package org.spawl.bungeepackets.api.nbt;

public class NBTReadLimiter
{
	public static final NBTReadLimiter UNLIMITED = new NBTReadLimiter(0)
	{
		@Override
		public void increment(long increment) {}
	};
	private final long max;
	private long counter;

	public NBTReadLimiter(long max)
	{
		this.max = max;
	}

	public void increment(long increment)
	{
		counter += increment;

		if (counter > max)
			throw new IllegalArgumentException("Tried to increment NBT tag that was too big; tried to allocate: " + counter +
					"bytes where max allowed: " + max);
	}
}