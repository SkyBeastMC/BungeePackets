package org.spawl.bungeepackets.api.nbt;

import com.google.common.base.Preconditions;
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
public class NBTTagByteArray extends NBTBase
{
	public static final NBTType TYPE = NBTType.BYTE_ARRAY;
	private byte[] value;

	@Override
	void write(DataOutput out)
			throws IOException
	{
		out.writeInt(value.length);
		out.write(value);
	}

	@Override
	void load(DataInput in, int depth, NBTReadLimiter limiter)
			throws IOException
	{
		int j = in.readInt();
		Preconditions.checkArgument(j < 16777216);

		limiter.increment(j);
		value = new byte[j];
		in.readFully(value);
	}

	@Override
	public NBTType getType()
	{
		return TYPE;
	}

	@Override
	public String toString()
	{
		return "[" + value.length + " bytes]";
	}
}