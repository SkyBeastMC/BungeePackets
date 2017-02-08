package org.spawl.bungeepackets.api.nbt;

import lombok.Data;

/**
 * Created by SkyBeast on 04/02/17.
 */
@Data
public class NBTRoot<T extends NBTBase>
{
	public static final NBTRoot<NBTTagEnd> NONE = new NBTRoot<>(null, NBTTagEnd.INSTANCE);

	private final String title;
	private final T tag;
}
