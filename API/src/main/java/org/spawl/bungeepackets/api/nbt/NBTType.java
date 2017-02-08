package org.spawl.bungeepackets.api.nbt;

import lombok.AllArgsConstructor;

import java.util.function.Supplier;

/**
 * Created by SkyBeast on 04/02/17.
 */
@AllArgsConstructor
public enum NBTType
{
	END(() -> NBTTagEnd.INSTANCE),
	BYTE(NBTTagByte::new),
	SHORT(NBTTagShort::new),
	INT(NBTTagInt::new),
	LONG(NBTTagLong::new),
	FLOAT(NBTTagFloat::new),
	DOUBLE(NBTTagDouble::new),
	BYTE_ARRAY(NBTTagByteArray::new),
	STRING(NBTTagString::new),
	LIST(NBTTagList::new),
	COMPOUND(NBTTagCompound::new),
	INT_ARRAY(NBTTagIntArray::new);

	private final Supplier<? extends NBTBase> newInstance;

	public byte getID()
	{
		return (byte) ordinal();
	}

	public NBTBase newInstance()
	{
		return newInstance.get();
	}


	public static NBTBase newInstance(byte id)
	{
		if (!know(id))
			return null;

		return values()[id].newInstance();
	}

	public static boolean know(byte id)
	{
		return id > 0 && id < values().length;
	}

	public static boolean isNumber(byte id)
	{
		return id >= 0 && id <= 6;
	}

	public static boolean isNumber(NBTType type)
	{
		return isNumber(type.getID());
	}

	public static NBTType get(byte id) {
		if (!know(id))
			return null;

		return values()[id];
	}
}
