package org.spawl.bungeepackets.inventory;

import gnu.trove.TCollections;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.Getter;
import lombok.NonNull;
import org.spawl.bungeepackets.api.inventory.Enchantment;
import org.spawl.bungeepackets.api.inventory.ItemStack;
import org.spawl.bungeepackets.api.inventory.Material;
import org.spawl.bungeepackets.api.nbt.*;
import org.spawl.bungeepackets.skin.MojangAPI;
import org.spawl.bungeepackets.skin.PlayerProperties;

import java.io.IOException;
import java.util.*;

/**
 * Created by SkyBeast on 05/02/17.
 */
@Getter
public class ItemStackImpl implements ItemStack
{
	@SuppressWarnings("unchecked")
	private static final TObjectIntMap EMPTY_OBJECT_INT_MAP = TCollections.unmodifiableMap(new TObjectIntHashMap<>());

	private static final String NAME = "Name";
	private static final String DISPLAY = "display";
	private static final String LORE = "Lore";
	private static final String ENCHANTMENTS = "ench";
	private static final String ENCHANTMENTS_ID = "id";
	private static final String ENCHANTMENTS_LVL = "lvl";
	private static final String HIDEFLAGS = "HideFlags";
	private static final String UNBREAKABLE = "Unbreakable";
	private static final String SKULL_OWNER = "SkullOwner";

	@NonNull
	private Material material;
	private int amount;
	private int data;
	private NBTTagCompound tag;

	public ItemStackImpl(@NonNull ByteBuf buf)
	{
		short id = buf.readShort();

		if (id >= 0)
		{
			int amount = buf.readByte();
			int data = buf.readShort();

			material = Material.getMaterial(id);

			if (material == null)
				throw new IllegalArgumentException("Material null for ID " + id);

			this.amount = amount;
			this.data = data;

			NBTBase nbt;
			try
			{
				nbt = NBTCompressedStreamTools.read(new ByteBufInputStream(buf),
						new NBTReadLimiter(262144)).getTag(); //=2097152bit
			}
			catch (IOException e)
			{
				throw new IllegalArgumentException("Error while reading NBT of item " + this, e);
			}

			if (nbt.getType() == NBTType.COMPOUND)
				tag = (NBTTagCompound) nbt;
		}
	}

	public ItemStackImpl(@NonNull Material material, int amount, int data, NBTTagCompound tag)
	{
		setMaterial(material).setAmount(amount).setData(data).setTag(tag);
	}

	@Override
	public void write(@NonNull ByteBuf buf)
	{
		/*if (material == null)
			buf.writeShort(-1);
		else
		{*/

		buf.writeShort(material.getId());
		buf.writeByte(amount);
		buf.writeShort(data);

		if (tag == null || tag.isEmpty())
			buf.writeByte(0);
		else
		{
			try
			{
				NBTCompressedStreamTools.write(tag, "", new ByteBufOutputStream(buf));
			}
			catch (IOException e)
			{
				throw new IllegalArgumentException("Error while reading NBT of item " + this, e);
			}
		}

		//}
	}

	@Override
	public boolean isTagSet()
	{
		return tag != null;
	}

	@Override
	public UUID getSkullOwner()
	{
		if (!isTagSet())
			return null;

		NBTTagCompound skull = tag.getCompound(SKULL_OWNER);
		if (skull == null)
			return null;

		String uuid = skull.getString("Id");
		return uuid == null ? null : UUID.fromString(uuid);
	}

	@Override
	public boolean hasLore()
	{
		if (!isTagSet())
			return false;

		NBTTagList lore = tag.getList(LORE);
		return lore != null && !lore.isEmpty();
	}

	@Override
	public List<? super String> getLore()
	{
		if (!isTagSet())
			return Collections.emptyList();

		NBTTagList lore = tag.getList(LORE);

		if (lore == null)
			return Collections.emptyList();

		List<String> parsed = new ArrayList<>();

		lore.forEach(nbt -> parsed.add(((NBTTagString) nbt).getValue()));

		return parsed;
	}

	@Override
	@SuppressWarnings("unchecked")
	public TObjectIntMap<? super Enchantment> getEnchantments()
	{
		if (!isTagSet())
			return EMPTY_OBJECT_INT_MAP;

		NBTTagList lore = tag.getList(ENCHANTMENTS);

		if (lore == null)
			return EMPTY_OBJECT_INT_MAP;

		TObjectIntMap<Enchantment> parsed = new TObjectIntHashMap<>();

		lore.forEach(nbt ->
		{
			NBTTagCompound com = (NBTTagCompound) nbt;
			parsed.put(Enchantment.get(com.getInt(ENCHANTMENTS_ID, 0)), com.getInt(ENCHANTMENTS_LVL));
		});

		return parsed;
	}

	@Override
	public ItemStack setMaterial(@NonNull Material material)
	{
		this.material = material;
		return this;
	}

	@Override
	public ItemStack setAmount(int amount)
	{
		if (amount < 0)
			throw new IllegalArgumentException("Cannot set an ItemStack to a negative amount");

		this.amount = amount;
		return this;
	}

	@Override
	public ItemStack setData(int data)
	{
		if (data < 0)
			throw new IllegalArgumentException("Cannot set an ItemStack to a negative data");

		this.data = data;
		return this;
	}

	@Override
	public ItemStack setTag(NBTTagCompound tag)
	{
		this.tag = tag;
		return this;
	}

	@Override
	public ItemStack removeTags()
	{
		if (isTagSet())
			tag.clear();

		return this;
	}

	@Override
	public ItemStack setSkullOwner(@NonNull UUID owner)
	{
		createTag();
		PlayerProperties properties = MojangAPI.requestSkin(owner);
		tag.set(SKULL_OWNER, properties.toNBT());

		return this;
	}

	@Override
	public ItemStack setSkullOwner(@NonNull String owner)
	{
		setSkullOwner(MojangAPI.getUUID(owner));

		return this;
	}

	@Override
	public ItemStack clearSkullOwner()
	{
		if (isTagSet())
			tag.remove(SKULL_OWNER);

		return this;
	}

	@Override
	public ItemStack setLore(@NonNull List<? extends String> lore)
	{
		NBTTagList nbt = createList(LORE);
		nbt.clear();

		lore.forEach(str -> nbt.add(new NBTTagString(str)));

		return this;
	}

	@Override
	public ItemStack setLore(@NonNull String... lore)
	{
		NBTTagList nbt = createList(LORE);
		nbt.clear();

		for (String str : lore)
			nbt.add(new NBTTagString(str));

		return this;
	}

	@Override
	public ItemStack addLore(@NonNull List<? extends String> lore)
	{
		NBTTagList nbt = createList(LORE);

		lore.forEach(str -> nbt.add(new NBTTagString(str)));

		return this;
	}

	@Override
	public ItemStack addLore(@NonNull String... lore)
	{
		NBTTagList nbt = createList(LORE);

		for (String str : lore)
			nbt.add(new NBTTagString(str));

		return this;
	}

	@Override
	public ItemStack clearLore()
	{
		if (isTagSet())
			tag.remove(LORE);

		return this;
	}

	@Override
	public ItemStack setEnchantments(@NonNull TObjectIntMap<? extends Enchantment> enchantments)
	{

		NBTTagList nbt = createList(ENCHANTMENTS);
		nbt.clear();

		enchantments.forEachEntry((enchantment, level) ->
		{
			NBTTagCompound ench = new NBTTagCompound();
			ench.setShort(ENCHANTMENTS_ID, (short) enchantment.getId());
			ench.setShort(ENCHANTMENTS_LVL, (short) level);

			nbt.add(ench);
			return true;
		});

		return this;
	}

	@Override
	public ItemStack addEnchantment(@NonNull Enchantment enchantment, int level)
	{
		if (level < 0)
			throw new IllegalArgumentException("Cannot set an enchantment to a negative level");

		NBTTagList nbt = createList(ENCHANTMENTS);

		NBTTagCompound ench = new NBTTagCompound();
		ench.setShort(ENCHANTMENTS_ID, (short) enchantment.getId());
		ench.setShort(ENCHANTMENTS_LVL, (short) level);

		nbt.add(ench);

		return this;
	}

	@Override
	public ItemStack clearEnchantments()
	{
		if (isTagSet())
			tag.remove(ENCHANTMENTS);

		return this;
	}

	@Override
	public ItemStack setFakeGlow()
	{
		return addEnchantment(Enchantment.FAKE_GLOW, 1);
	}

	@Override
	public ItemStack setDisplay(@NonNull String display)
	{
		createTag();
		tag.set(NAME, new NBTTagString(display));
		return this;
	}

	private NBTTagList createList(@NonNull String key)
	{
		createTag();

		NBTTagList list = tag.getList(key);

		if (list == null)
		{
			list = new NBTTagList();
			tag.set(key, list);
		}

		return list;
	}

	private void createTag()
	{
		if (tag == null)
			tag = new NBTTagCompound();
	}
}
