package org.spawl.bungeepackets.api.nbt;

import io.netty.buffer.ByteBufInputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class NBTCompressedStreamTools
{
	private NBTCompressedStreamTools() {}

	public static NBTRoot<NBTTagCompound> readCompound(InputStream in)
			throws IOException
	{
		DataInputStream dataIn = new DataInputStream(new BufferedInputStream(
				new GZIPInputStream(in)));

		return readCompound(dataIn, NBTReadLimiter.UNLIMITED);
	}

	public static NBTRoot<NBTTagCompound> readCompound(DataInputStream in)
			throws IOException
	{
		return readCompound(in, NBTReadLimiter.UNLIMITED);
	}

	public static NBTRoot<NBTTagCompound> readCompound(DataInput in, NBTReadLimiter limiter)
			throws IOException
	{
		if (in instanceof ByteBufInputStream)
		{
			in = new DataInputStream(new LimitStream((InputStream) in, limiter));
		}

		NBTRoot<? extends NBTBase> root = readTag(in, 0, limiter);

		if (root.getTag() instanceof NBTTagCompound)
			//noinspection unchecked
			return (NBTRoot<NBTTagCompound>) root;

		throw new IOException("Root tag must be UNLIMITED named compound tag");
	}

	public static NBTRoot<? extends NBTBase> read(InputStream in)
			throws IOException
	{
		DataInputStream dataIn = new DataInputStream(new BufferedInputStream(
				new GZIPInputStream(in)));

		return read(dataIn, NBTReadLimiter.UNLIMITED);
	}

	public static NBTRoot<? extends NBTBase> read(DataInputStream in)
			throws IOException
	{
		return read(in, NBTReadLimiter.UNLIMITED);
	}

	public static NBTRoot<? extends NBTBase> read(DataInput in, NBTReadLimiter limiter)
			throws IOException
	{
		if (in instanceof ByteBufInputStream)
		{
			in = new DataInputStream(new LimitStream((InputStream) in, limiter));
		}

		return readTag(in, 0, limiter);
	}

	private static NBTRoot<? extends NBTBase> readTag(DataInput in, int depth, NBTReadLimiter limiter)
			throws IOException
	{
		byte id = in.readByte();
		if (id == 0)
			return NBTRoot.NONE;

		String title = in.readUTF();
		NBTBase nbt = NBTType.newInstance(id);

		if (nbt == null)
			throw new NBTException("Cannot find NBTType with windowID " + id);

		nbt.load(in, depth, limiter);
		return new NBTRoot<>(title, nbt);
	}

	public static void write(NBTBase tag, String title, OutputStream out)
			throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(
				new GZIPOutputStream(out)));

		writeTag(tag, title, dataOut);
	}

	private static void writeTag(NBTBase nbt, String title, DataOutput out)
			throws IOException
	{
		out.writeByte(nbt.getTypeId());
		if (nbt.getType() != NBTType.END)
		{
			out.writeUTF(title);
			nbt.write(out);
		}
	}
}