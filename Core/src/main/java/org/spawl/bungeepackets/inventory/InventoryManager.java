package org.spawl.bungeepackets.inventory;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import org.spawl.bungeepackets.api.event.PacketFilter;
import org.spawl.bungeepackets.api.event.PacketListener;
import org.spawl.bungeepackets.api.inventory.Inventory;
import org.spawl.bungeepackets.protocol.client.InCloseWindow;
import org.spawl.bungeepackets.protocol.client.InWindowClick;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SkyBeast on 05/02/17.
 */
public class InventoryManager implements Listener, PacketListener
{
	public static final InventoryManager INSTANCE = new InventoryManager();
	private static final Map<ProxiedPlayer, InventoryImpl> OPEN = new HashMap<>();

	private InventoryManager() {}

	public static InventoryImpl getOpenInventory(ProxiedPlayer user)
	{
		return OPEN.get(user);
	}

	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		resetOpen(player);
	}

	@EventHandler
	public void onConnect(ServerConnectEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		resetOpen(player);
	}

	public static void resetOpen(ProxiedPlayer player)
	{
		InventoryImpl inventory = OPEN.get(player);
		if (inventory != null)
			inventory.dispatchCloseEvent(player, false);

		removeOpen(player);
	}

	private static void removeOpen(ProxiedPlayer player)
	{
		OPEN.remove(player);
	}

	public static boolean hasInventoryOpen(ProxiedPlayer player)
	{
		return OPEN.containsKey(player);
	}

	public static boolean hasInventoryOpen(ProxiedPlayer player, Inventory inventory)
	{
		Inventory inv = OPEN.get(player);
		return inv != null && inv.equals(inventory);
	}

	public static void open(ProxiedPlayer user, InventoryImpl inv)
	{
		OPEN.put(user, inv);
	}

	@Override
	@PacketFilter({InWindowClick.class, InCloseWindow.class})
	public boolean onPlayerToBungee(DefinedPacket packet, ProxiedPlayer user)
	{
		InventoryImpl inv = getOpenInventory(user);
		if (inv == null)
			return false;

		if (packet instanceof InWindowClick)
			handleWindowClick((InWindowClick) packet, user, inv);
		else if (packet instanceof InCloseWindow)
			handleCloseWindow((InCloseWindow) packet, user, inv);

		return true;
	}

	private static void handleCloseWindow(InCloseWindow packet, ProxiedPlayer user, InventoryImpl inv)
	{
		removeOpen(user);
		inv.dispatchCloseEvent(user, true);
	}

	private static void handleWindowClick(InWindowClick packet, ProxiedPlayer user, InventoryImpl inv)
	{
		inv.dispatchClickEvent(user, packet);
	}
}
