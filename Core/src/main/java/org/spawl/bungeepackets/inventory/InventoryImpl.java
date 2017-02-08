package org.spawl.bungeepackets.inventory;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Data;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import org.spawl.bungeepackets.api.inventory.Inventory;
import org.spawl.bungeepackets.api.inventory.InventoryType;
import org.spawl.bungeepackets.api.inventory.ItemStack;
import org.spawl.bungeepackets.api.inventory.Material;
import org.spawl.bungeepackets.protocol.client.InWindowClick;
import org.spawl.bungeepackets.protocol.server.OutCloseWindow;
import org.spawl.bungeepackets.protocol.server.OutOpenWindow;
import org.spawl.bungeepackets.protocol.server.OutSetSlot;
import org.spawl.bungeepackets.protocol.server.OutWindowItems;

import java.util.logging.Level;

@Data
public class InventoryImpl implements Listener, Inventory
{
	private static final OutSetSlot NULLIFY_PACKET = new OutSetSlot(-1, -1, null);
	private static final OutCloseWindow CLOSE_PACKET = new OutCloseWindow(1);
	private final TIntObjectMap<ItemStack> items = new TIntObjectHashMap<>();
	//private HashMap<Integer, ItemStack> map;
	private final TIntObjectMap<ClickHandler> handlers = new TIntObjectHashMap<>();
	//private HashMap<Integer, ClickHandler> handler;
	@NonNull
	private String title;
	@NonNull
	private InventoryType type;
	private int slots;
	private int entityID;
	private CloseHandler closeHandler;

	public InventoryImpl(@NonNull String title, @NonNull InventoryType type, int slots, int entityID)
	{
		setTitle(title);
		setType(type);
		setSlots(slots);
		setEntityID(entityID);
	}

	public static void closeInventories(@NonNull ProxiedPlayer user)
	{
		user.unsafe().sendPacket(CLOSE_PACKET);
	}

	@Override
	public void setSlots(int slots)
	{
		if(slots < 0)
			throw new IllegalArgumentException("Cannot set an inventory to a negative number of slot");

		this.slots = slots;
	}

	void dispatchCloseEvent(ProxiedPlayer user, boolean clientDid)
	{
		if (closeHandler == null)
			return;

		try
		{
			closeHandler.onClose(user, clientDid);
		}
		catch (Exception e)
		{
			ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Error while dispatching close inventory event " +
					"to handler " + closeHandler.getClass(), e);
		}
	}

	void dispatchClickEvent(ProxiedPlayer user, InWindowClick packet)
	{
		handlers.forEachEntry(
				(slot, handler) ->
				{
					if (slot != packet.getSlot())
						return true;

					try
					{
						handler.onClick(user, packet.getSlot(), packet.getItem(), packet.getButton() == 0,
								packet.getShift() == 1);
					}
					catch (Exception e)
					{
						ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Error while dispatching click " +
								"inventory event to handler " + handler.getClass(), e);
					}
					return true;
				}
		);

		user.unsafe().sendPacket(NULLIFY_PACKET);
		updateItem(user, packet.getSlot());
	}

	@Override
	public ItemStack getItem(int slot)
	{
		if (!isSlotValid(slot))
			return null;

		return items.get(slot);
	}

	@Override
	public void setItem(int slot, @NonNull ItemStack stack)
	{
		safeSet(slot);
		if (isValid(stack))
			items.put(slot, stack);
	}

	@Override
	public int addItem(@NonNull ItemStack stack)
	{
		for (int i = 0; i < maxSize(); i++)
		{
			if (!isValid(items.get(i)))
			{
				if (isValid(stack)) items.put(i, stack);
				return i;
			}
		}

		return -1;
	}

	private boolean isValid(ItemStack stack)
	{
		return stack != null && stack.getMaterial() != Material.AIR;
	}

	private void safeSet(int slot)
	{
		if (!isSlotValid(slot))
			throw new IndexOutOfBoundsException("Cannot add to index " + slot);
	}

	private boolean isSlotValid(int slot)
	{
		return slot >= 0 && slot < maxSize();
	}

	private int maxSize()
	{
		return slots + PLAYER_INVENTORY_SIZE;
	}

	@Override
	public void removeItem(@NonNull ItemStack stack, boolean removeHandler)
	{
		if (!isValid(stack))
			return;

		for (int key : items.keys())
		{
			if (items.get(key).equals(stack))
			{
				handlers.remove(key);
				if (removeHandler)
					removeClickHandler(key);

				return;
			}
		}
	}

	@Override
	public void removeItem(int slot, boolean removeHandler)
	{
		if (!isSlotValid(slot))
			return;

		items.remove(slot);
		handlers.remove(slot);
	}

	@Override
	public void setClickHandler(int slot, @NonNull ClickHandler handler)
	{
		handlers.put(slot, handler);
	}

	@Override
	public void removeClickHandler(int slot)
	{
		handlers.remove(slot);
	}

	@Override
	public void removeClickHandlers()
	{
		handlers.clear();
	}

	@Override
	public void removeCloseHandler()
	{
		closeHandler = null;
	}

	@Override
	public void updateItem(@NonNull ProxiedPlayer user, int slot)
	{
		if (!isSlotValid(slot))
			return;

		user.unsafe().sendPacket(new OutSetSlot
				(
						1,
						slot,
						items.get(slot)

				));
	}

	@Override
	public void updateItems(@NonNull ProxiedPlayer user)
	{
		user.unsafe().sendPacket(new OutWindowItems
				(
						1,
						toArray()
				));
	}

	private ItemStack[] toArray()
	{
		ItemStack[] all = new ItemStack[slots + 36];
		items.forEachEntry(
				(slot, item) ->
				{
					all[slot] = item;
					return true;
				}
		);

		return all;
	}

	@Override
	public boolean open(@NonNull ProxiedPlayer user)
	{
		user.unsafe().sendPacket(new OutOpenWindow
				(
						1,
						type.getId(),
						title,
						slots,
						0
				));

		InventoryImpl old = InventoryManager.getOpenInventory(user);
		if (old != null)
			old.dispatchCloseEvent(user, false);


		InventoryManager.open(user, this);

		return true;
	}

	@Override
	public boolean closeInventory(@NonNull ProxiedPlayer user)
	{
		if (!InventoryManager.hasInventoryOpen(user, this))
			return false;

		InventoryManager.resetOpen(user);
		closeInventories(user);
		return true;
	}
}
