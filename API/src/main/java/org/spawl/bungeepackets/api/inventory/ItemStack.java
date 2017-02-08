package org.spawl.bungeepackets.api.inventory;

import gnu.trove.map.TObjectIntMap;
import io.netty.buffer.ByteBuf;
import org.spawl.bungeepackets.api.BungeePackets;
import org.spawl.bungeepackets.api.nbt.NBTTagCompound;

import java.util.List;
import java.util.UUID;

public interface ItemStack
{
	static ItemStack read(ByteBuf buf)
	{
		return BungeePackets.readItemStack(buf);
	}

	static ItemStack of(Material material)
	{
		return BungeePackets.itemStackOf(material);
	}

	static ItemStack of(Material material, int amount)
	{
		return BungeePackets.itemStackOf(material, amount);
	}

	static ItemStack of(Material material, int amount, int data)
	{
		return BungeePackets.itemStackOf(material, amount, data);
	}

	static ItemStack of(Material material, int amount, int data, NBTTagCompound tag)
	{
		return BungeePackets.itemStackOf(material, amount, data, tag);
	}

	void write(ByteBuf buf);

	Material getMaterial();

	int getAmount();

	int getData();

	boolean isTagSet();

	NBTTagCompound getTag();

	UUID getSkullOwner();

	boolean hasLore();

	List<? super String> getLore();

	TObjectIntMap<? super Enchantment> getEnchantments();

	ItemStack setMaterial(Material material);

	ItemStack setAmount(int amount);

	ItemStack setData(int data);

	ItemStack setTag(NBTTagCompound tag);

	ItemStack removeTags();

	ItemStack setSkullOwner(UUID owner);

	ItemStack setSkullOwner(String owner);

	ItemStack clearSkullOwner();

	ItemStack setLore(List<? extends String> lore);

	ItemStack setLore(String... lore);

	ItemStack addLore(List<? extends String> lore);

	ItemStack addLore(String... lore);

	ItemStack clearLore();

	ItemStack setEnchantments(TObjectIntMap<? extends Enchantment> enchantments);

	ItemStack addEnchantment(Enchantment enchantment, int level);

	ItemStack clearEnchantments();

	ItemStack setFakeGlow();

	ItemStack setDisplay(String display);
}
