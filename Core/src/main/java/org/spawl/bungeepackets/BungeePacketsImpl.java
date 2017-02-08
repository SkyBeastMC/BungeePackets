package org.spawl.bungeepackets;

import io.netty.buffer.ByteBuf;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Protocol;
import org.spawl.bungeepackets.api.BungeePackets;
import org.spawl.bungeepackets.api.event.PacketFilter;
import org.spawl.bungeepackets.api.event.PacketListener;
import org.spawl.bungeepackets.api.event.ProtocolMapping;
import org.spawl.bungeepackets.api.inventory.Inventory;
import org.spawl.bungeepackets.api.inventory.InventoryType;
import org.spawl.bungeepackets.api.inventory.ItemStack;
import org.spawl.bungeepackets.api.inventory.Material;
import org.spawl.bungeepackets.api.nbt.NBTTagCompound;
import org.spawl.bungeepackets.inventory.InventoryImpl;
import org.spawl.bungeepackets.inventory.ItemStackImpl;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;

/**
 * Created by SkyBeast on 04/02/17.
 */
public class BungeePacketsImpl extends BungeePackets
{
	private static final Map<Class<? extends DefinedPacket>, List<PacketListener>>
			L_BUNGEE_PLAYER_INCLUDE = new HashMap<>(),
			L_BUNGEE_SERVER_INCLUDE = new HashMap<>(),
			L_PLAYER_BUNGEE_INCLUDE = new HashMap<>(),
			L_SERVER_BUNGEE_INCLUDE = new HashMap<>();
	private static final List<PacketListener>
			L_BUNGEE_PLAYER_NONE = new ArrayList<>(),
			L_BUNGEE_SERVER_NONE = new ArrayList<>(),
			L_PLAYER_BUNGEE_NONE = new ArrayList<>(),
			L_SERVER_BUNGEE_NONE = new ArrayList<>();

	private static void addListener(PacketListener listener)
	{
		handleAddListener(listener, getFilter(listener, "onBungeeToPlayer"),
				L_BUNGEE_PLAYER_INCLUDE, L_BUNGEE_PLAYER_NONE);
		handleAddListener(listener, getFilter(listener, "onBungeeToServer"),
				L_BUNGEE_SERVER_INCLUDE, L_BUNGEE_SERVER_NONE);
		handleAddListener(listener, getFilter(listener, "onPlayerToBungee"),
				L_PLAYER_BUNGEE_INCLUDE, L_PLAYER_BUNGEE_NONE);
		handleAddListener(listener, getFilter(listener, "onServerToBungee"),
				L_SERVER_BUNGEE_INCLUDE, L_SERVER_BUNGEE_NONE);
	}

	private static void handleAddListener(PacketListener listener, PacketFilter filter,
	                                      Map<Class<? extends DefinedPacket>, List<PacketListener>> includeMap,
	                                      List<PacketListener> noneList)
	{
		if (filter == null)
			noneList.add(listener);
		else
		{
			for (Class<? extends DefinedPacket> clazz : filter.value())
			{
				List<PacketListener> list = includeMap.get(clazz);
				if (list == null)
					list = new ArrayList<>();

				list.add(listener);
				includeMap.put(clazz, list);
			}
		}
	}

	private static PacketFilter getFilter(PacketListener listener, String methodName)
	{
		try
		{
			Method method = listener.getClass().getDeclaredMethod(methodName);
			return method.getAnnotation(PacketFilter.class);
		}
		catch (NoSuchMethodException ignored)
		{
			return null;
		}
	}

	public static boolean dispatchBungeeToPlayer(DefinedPacket packet, ProxiedPlayer user)
	{
		return dispatch
				(
						packet,
						L_BUNGEE_PLAYER_INCLUDE,
						L_BUNGEE_PLAYER_NONE,
						handler -> handler.onBungeeToPlayer(packet, user)
				);
	}

	public static boolean dispatchBungeeToServer(DefinedPacket packet, ProxiedPlayer user, Server server)
	{
		return dispatch
				(
						packet,
						L_BUNGEE_SERVER_INCLUDE,
						L_BUNGEE_SERVER_NONE,
						handler -> handler.onBungeeToServer(packet, user, server)
				);
	}

	public static boolean dispatchPlayerToBungee(DefinedPacket packet, ProxiedPlayer user)
	{
		return dispatch
				(
						packet,
						L_PLAYER_BUNGEE_INCLUDE,
						L_PLAYER_BUNGEE_NONE,
						handler -> handler.onPlayerToBungee(packet, user)
				);
	}

	public static boolean dispatchServerToBungee(DefinedPacket packet, ProxiedPlayer user, Server server)
	{
		return dispatch
				(
						packet,
						L_SERVER_BUNGEE_INCLUDE,
						L_SERVER_BUNGEE_NONE,
						handler -> handler.onServerToBungee(packet, user, server)
				);
	}

	private static boolean dispatch(DefinedPacket packet,
	                                Map<Class<? extends DefinedPacket>, List<PacketListener>> includeMap,
	                                List<PacketListener> noneList,
	                                Predicate<PacketListener> method)
	{
		boolean cancel = false;

		if (handleDispatch(noneList, method))
			cancel = true;

		if (handleDispatch(includeMap.get(packet.getClass()), method))
			cancel = true;

		return cancel;
	}

	private static boolean handleDispatch(List<PacketListener> listeners, Predicate<PacketListener> method)
	{
		if (listeners == null || listeners.isEmpty())
			return false;

		boolean cancel = false;

		for (PacketListener listener : listeners)
		{
			if (callListener(listener, method))
				cancel = true;
		}

		return cancel;
	}

	private static boolean callListener(PacketListener listener, Predicate<PacketListener> method)
	{
		try
		{
			return method.test(listener);
		}
		catch (Exception e)
		{
			ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Error while dispatching packet event to" +
					" listener " + listener.getClass(), e);
		}
		return false;
	}

	private static final Class PROTOCOL_MAPPING_CLASS;
	private static final Method PROTOCOL_MAP_METHOD;
	private static final Method REGISTER_PACKET_METHOD;

	static
	{
		try
		{
			PROTOCOL_MAPPING_CLASS = Class.forName("net.md_5.bungee.protocol.Protocol$ProtocolMapping");

			PROTOCOL_MAP_METHOD = Protocol.class.getDeclaredMethod("map",
					int.class, int.class);
			PROTOCOL_MAP_METHOD.setAccessible(true);

			Class protocolMapArray = Class.forName("[L" + PROTOCOL_MAPPING_CLASS.getName() + ';');
			REGISTER_PACKET_METHOD = Protocol.DirectionData.class.getDeclaredMethod("registerPacket",
					Class.class, protocolMapArray);
			REGISTER_PACKET_METHOD.setAccessible(true);

		}
		catch (ReflectiveOperationException e)
		{
			throw new RuntimeException("Cannot access register packet method", e);
		}
	}

	private static void addPacket(Protocol.DirectionData direction,
	                              Class<? extends DefinedPacket> packet,
	                              ProtocolMapping... protocols)
	{
		try
		{
			Object protocolArray = mapProtocols(protocols);
			REGISTER_PACKET_METHOD.invoke(direction, packet, protocolArray);
		}
		catch (ReflectiveOperationException e)
		{
			throw new IllegalArgumentException("Cannot register packet " + packet, e);
		}
	}

	private static Object mapProtocols(ProtocolMapping... protocols)
			throws ReflectiveOperationException
	{
		Object maps = Array.newInstance(PROTOCOL_MAPPING_CLASS, protocols.length);
		for (int i = 0; i < protocols.length; i++)
			Array.set(maps, i, mapProtocol(protocols[i]));

		return maps;
	}

	private static Object mapProtocol(ProtocolMapping protocolMapping)
			throws ReflectiveOperationException
	{
		return PROTOCOL_MAP_METHOD.invoke(null, protocolMapping.getProtocol(), protocolMapping.getPacketID());
	}

	@Override
	protected void registerPacketListenerImpl(@NonNull PacketListener listener)
	{
		addListener(listener);
	}

	@Override
	protected void registerPacketImpl(@NonNull Protocol.DirectionData direction,
	                                  @NonNull Class<? extends DefinedPacket> packet,
	                                  @NonNull ProtocolMapping... protocols)
	{
		addPacket(direction, packet, protocols);
	}

	@Override
	protected ItemStack readItemStackImpl(@NonNull ByteBuf buf)
	{
		return new ItemStackImpl(buf);
	}

	@Override
	protected ItemStack itemStackOfImpl(@NonNull Material material, int amount, int data, NBTTagCompound tag)
	{
		return new ItemStackImpl(material, amount, data, tag);
	}

	@Override
	protected Inventory inventoryOfImpl(@NonNull String title, @NonNull InventoryType type, int slots, int entityID)
	{
		return new InventoryImpl(title, type, slots, entityID);
	}
}
