package org.spawl.bungeepackets.api.nbt;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LimitStream extends FilterInputStream
{
	private final NBTReadLimiter limit;

	public LimitStream(InputStream in, NBTReadLimiter limit)
	{
		super(in);
		this.limit = limit;
	}

	@Override
	public int read()
			throws IOException
	{
		limit.increment(1L);
		return super.read();
	}

	@Override
	public int read(byte[] b)
			throws IOException
	{
		limit.increment(b.length);
		return super.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len)
			throws IOException
	{
		limit.increment(len);
		return super.read(b, off, len);
	}
}
