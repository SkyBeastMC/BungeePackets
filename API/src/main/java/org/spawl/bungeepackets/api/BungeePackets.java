package org.spawl.bungeepackets.api;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Protocol;
import org.spawl.bungeepackets.api.effect.ParticleEffectType;
import org.spawl.bungeepackets.api.event.PacketListener;
import org.spawl.bungeepackets.api.inventory.Inventory;
import org.spawl.bungeepackets.api.inventory.InventoryType;
import org.spawl.bungeepackets.api.inventory.ItemStack;
import org.spawl.bungeepackets.api.inventory.Material;
import org.spawl.bungeepackets.api.nbt.NBTTagCompound;

/**
 * Created by SkyBeast on 04/02/17.
 */
public abstract class BungeePackets
{
	private static BungeePackets impl;

	protected BungeePackets()
	{
		if (impl == null)
			throw new IllegalStateException("Cannot have multiple implementations");
		impl = this;
	}

	public static void registerPacket(Protocol.DirectionData data, int id, Class<? extends DefinedPacket> packet)
	{
		impl.registerPacketImpl(data, id, packet);
	}

	protected abstract void registerPacketImpl(Protocol.DirectionData data, int id, Class<? extends DefinedPacket>
			packet);

	public static void registerPacketListener(PacketListener listener)
	{
		impl.registerPacketImpl(listener);
	}

	protected abstract void registerPacketImpl(PacketListener listener);

	public static boolean playEffect(ProxiedPlayer p, ParticleEffectType effect, float x, float y, float z,
	                                 float offsetX, float offsetY, float offsetZ, float speed, int amount)
	{
		return false;
	}

	public static void sendPacket(ProxiedPlayer connection, DefinedPacket packet)
	{
		connection.unsafe().sendPacket(packet);
	}

	public static ItemStack readItemStack(ByteBuf buf)
	{
		return impl.readItemStackImpl(buf);
	}

	protected abstract ItemStack readItemStackImpl(ByteBuf buf);

	public static ItemStack itemStackOf(Material material)
	{
		return impl.itemStackOfImpl(material, 1, 0, null);
	}

	public static ItemStack itemStackOf(Material material, int amount)
	{
		return impl.itemStackOfImpl(material, amount, 0, null);
	}

	public static ItemStack itemStackOf(Material material, int amount, int data)
	{
		return impl.itemStackOfImpl(material, amount, data, null);
	}

	public static ItemStack itemStackOf(Material material, int amount, int data, NBTTagCompound tag)
	{
		return impl.itemStackOfImpl(material, amount, data, tag);
	}

	protected abstract ItemStack itemStackOfImpl(Material material, int amount, int data, NBTTagCompound tag);

	public static Inventory inventoryOf(String title, InventoryType type, int slots)
	{
		return impl.inventoryOfImpl(title, type, slots, 0);
	}

	public static Inventory inventoryOf(String title, InventoryType type, int slots, int entityID)
	{
		return impl.inventoryOfImpl(title, type, slots, entityID);
	}

	protected abstract Inventory inventoryOfImpl(String title, InventoryType type, int slots, int entityID);
}

