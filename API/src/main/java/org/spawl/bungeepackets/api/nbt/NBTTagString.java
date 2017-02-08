package org.spawl.bungeepackets.api.nbt;

import lombok.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NBTTagString extends NBTBase
{
	public static final NBTType TYPE = NBTType.STRING;
	private static final Pattern QUOTE_FINDER = Pattern.compile("\"", Pattern.LITERAL);

	@NonNull
	private String value;

	@Override
	void write(DataOutput out)
			throws IOException
	{
		out.writeUTF(value);
	}

	@Override
	void load(DataInput in, int depth, NBTReadLimiter limiter)
			throws IOException
	{
		value = in.readUTF();
		limiter.increment(2 * value.length());
	}

	@Override
	public NBTType getType()
	{
		return TYPE;
	}

	@Override
	public String toString()
	{
		return '"' + QUOTE_FINDER.matcher(value).replaceAll(Matcher.quoteReplacement("\\\"")) + '"';
	}

	public boolean isEmpty()
	{
		return value.isEmpty();
	}
}