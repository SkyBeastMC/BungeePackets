package org.spawl.bungeepackets.api.nbt;

import lombok.EqualsAndHashCode;
import lombok.experimental.Delegate;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
public class NBTTagCompound extends NBTBase implements Map<String, NBTBase>
{
	public static final NBTType TYPE = NBTType.COMPOUND;
	@Delegate private final Map<String, NBTBase> map = new HashMap<>();

	@Override
	void write(DataOutput out)
			throws IOException
	{
		for (Map.Entry<String, NBTBase> entry : map.entrySet())
			writeField(entry.getKey(), entry.getValue(), out);

		out.writeByte(0);
	}

	private static void writeField(String name, NBTBase nbt, DataOutput out)
			throws IOException
	{
		out.writeByte(nbt.getTypeId());
		if (nbt.getType() == NBTType.END) return;
		out.writeUTF(name);
		nbt.write(out);
	}

	@Override
	void load(DataInput in, int depth, NBTReadLimiter limiter)
			throws IOException
	{
		if (depth > 512)
			throw new RuntimeException("Tried to increment NBT tag with too high complexity, depth > 512");

		map.clear();
		byte id;
		while ((id = in.readByte()) != 0)
		{
			String str = in.readUTF();
			limiter.increment(2 * str.length());
			NBTBase nbt = readTag(id, in, depth + 1, limiter);
			map.put(str, nbt);
		}
	}

	private static NBTBase readTag(byte id, DataInput in, int depth,
	                               NBTReadLimiter limiter)
			throws IOException
	{
		NBTBase nbt = NBTType.newInstance(id);
		if (nbt == null)
			throw new NBTException("Cannot find NBTType with windowID " + id);

		nbt.load(in, depth, limiter);
		return nbt;
	}

	@Override
	public NBTType getType()
	{
		return TYPE;
	}

	public void set(String key, NBTBase value)
	{
		map.put(key, value);
	}

	public void setByte(String key, byte value)
	{
		map.put(key, new NBTTagByte(value));
	}

	public void setShort(String key, short value)
	{
		map.put(key, new NBTTagShort(value));
	}

	public void setInt(String key, int value)
	{
		map.put(key, new NBTTagInt(value));
	}

	public void setLong(String key, long value)
	{
		map.put(key, new NBTTagLong(value));
	}

	public void setFloat(String key, float value)
	{
		map.put(key, new NBTTagFloat(value));
	}

	public void setDouble(String key, double value)
	{
		map.put(key, new NBTTagDouble(value));
	}

	public void setString(String key1, String value)
	{
		map.put(key1, new NBTTagString(value));
	}

	public void setByteArray(String key, byte[] value)
	{
		map.put(key, new NBTTagByteArray(value));
	}

	public void setIntArray(String key, int[] value)
	{
		map.put(key, new NBTTagIntArray(value));
	}

	public void setBoolean(String key, boolean value)
	{
		setByte(key, (byte) (value ? 1 : 0));
	}

	public NBTBase get(String key)
	{
		return map.get(key);
	}

	public NBTBase get(String key, NBTBase defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue : nbt;
	}

	public NBTType getTagType(String key)
	{
		NBTBase nbt = map.get(key);
		if (nbt != null)
			return nbt.getType();

		return null;
	}

	public boolean hasKey(String key)
	{
		return map.containsKey(key);
	}

	public boolean hasKeyOfType(String key, NBTType id)
	{
		NBTType i = getTagType(key);
		return i == id || (id.getID() == 99 && NBTType.isNumber(id));
	}


	public Byte getByte(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.BYTE ? ((NBTTagByte) nbt).getValue() : null);
	}

	public byte getByte(String key, byte defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.BYTE ? ((NBTTagByte) nbt).getValue() : defaultValue);
	}

	public Short getShort(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.SHORT ? ((NBTTagShort) nbt).getValue() : null);
	}

	public short getShort(String key, short defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.SHORT ? ((NBTTagShort) nbt).getValue() : defaultValue);
	}

	public Integer getInt(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.INT ? ((NBTTagInt) nbt).getValue() : null);
	}

	public int getInt(String key, int defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.INT ? ((NBTTagInt) nbt).getValue() : defaultValue);
	}

	public Long getLong(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.LONG ? ((NBTTagLong) nbt).getValue() : null);
	}

	public long getLong(String key, long defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.LONG ? ((NBTTagLong) nbt).getValue() : defaultValue);
	}

	public Float getFloat(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.FLOAT ? ((NBTTagFloat) nbt).getValue() : null);
	}

	public float getFloat(String key, float defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.FLOAT ? ((NBTTagFloat) nbt).getValue() : defaultValue);
	}

	public Double getDouble(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.DOUBLE ? ((NBTTagDouble) nbt).getValue() : null);
	}

	public double getDouble(String key, double defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.DOUBLE ? ((NBTTagDouble) nbt).getValue() : defaultValue);
	}

	public byte[] getByteArray(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.BYTE_ARRAY ? ((NBTTagByteArray) nbt).getValue() : null);
	}

	public byte[] getByteArray(String key, byte[] defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.BYTE_ARRAY ? ((NBTTagByteArray) nbt).getValue() : defaultValue);
	}

	public String getString(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.STRING ? ((NBTTagString) nbt).getValue() : null);
	}

	public String getString(String key, String defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.STRING ? ((NBTTagString) nbt).getValue() : defaultValue);
	}

	public NBTTagList getList(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.LIST ? (NBTTagList) nbt : null);
	}

	public NBTTagList getList(String key, NBTTagList defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.LIST ? (NBTTagList) nbt : defaultValue);
	}

	public NBTTagCompound getCompound(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.COMPOUND ? (NBTTagCompound) nbt : null);
	}

	public NBTTagCompound getCompound(String key, NBTTagCompound defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.COMPOUND ? (NBTTagCompound) nbt : defaultValue);
	}

	public int[] getIntArray(String key)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? null :
				(nbt.getType() == NBTType.INT_ARRAY ? ((NBTTagIntArray) nbt).getValue() : null);
	}

	public int[] getIntArray(String key, int[] defaultValue)
	{
		NBTBase nbt = map.get(key);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.INT_ARRAY ? ((NBTTagIntArray) nbt).getValue() : defaultValue);
	}

	public Boolean getBoolean(String key)
	{
		Byte b = getByte(key);
		return b == null ? null : b != 0;
	}

	public boolean getBoolean(String key, boolean defaultValue)
	{
		Byte b = getByte(key);
		return b == null ? defaultValue : b != 0;
	}

	public void remove(String key)
	{
		map.remove(key);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("{");
		for (Map.Entry<String, NBTBase> entry : map.entrySet())
			builder.append(entry.getKey())
					.append(':')
					.append(',');

		return builder.append('}').toString();
	}
}