package org.spawl.bungeepackets.api;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Protocol;
import org.spawl.bungeepackets.api.event.PacketListener;
import org.spawl.bungeepackets.api.event.ProtocolMapping;
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
		if (impl != null)
			throw new IllegalStateException("Cannot have multiple implementations");
		impl = this;
	}

	public static void sendPacket(ProxiedPlayer connection, DefinedPacket packet)
	{
		connection.unsafe().sendPacket(packet);
	}

	public static void registerPacket(Protocol.DirectionData direction, Class<? extends DefinedPacket> packet,
	                                  ProtocolMapping... protocols)
	{
		impl.registerPacketImpl(direction, packet, protocols);
	}

	public static void registerPacketListener(PacketListener listener)
	{
		impl.registerPacketListenerImpl(listener);
	}

	public static ItemStack readItemStack(ByteBuf buf)
	{
		return impl.readItemStackImpl(buf);
	}

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

	public static Inventory inventoryOf(String title, InventoryType type, int slots)
	{
		return impl.inventoryOfImpl(title, type, slots, 0);
	}

	public static Inventory inventoryOf(String title, InventoryType type, int slots, int entityID)
	{
		return impl.inventoryOfImpl(title, type, slots, entityID);
	}

	protected abstract void registerPacketImpl(Protocol.DirectionData direction, Class<? extends DefinedPacket> packet,
	                                           ProtocolMapping... protocols);

	protected abstract void registerPacketListenerImpl(PacketListener listener);

	protected abstract ItemStack readItemStackImpl(ByteBuf buf);

	protected abstract ItemStack itemStackOfImpl(Material material, int amount, int data, NBTTagCompound tag);

	protected abstract Inventory inventoryOfImpl(String title, InventoryType type, int slots, int entityID);
}

