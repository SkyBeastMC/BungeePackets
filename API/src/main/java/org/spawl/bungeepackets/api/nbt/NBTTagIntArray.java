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
public class NBTTagIntArray extends NBTBase
{
	public static final NBTType TYPE = NBTType.INT_ARRAY;
	private int[] value;

	@Override
	void write(DataOutput out) throws IOException
	{
		out.writeInt(value.length);

		for (int i : value) out.writeInt(i);
	}

	@Override
	void load(DataInput in, int depth, NBTReadLimiter limiter)
			throws IOException
	{
		int j = in.readInt();
		Preconditions.checkArgument(j < 16777216);

		limiter.increment(4 * j);
		value = new int[j];

		for (int k = 0; k < j; k++)
			value[k] = in.readInt();
	}

	@Override
	public NBTType getType()
	{
		return TYPE;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("[");

		for (int i : value)
			builder.append(i)
					.append(',');

		return builder.append(']').toString();
	}
}