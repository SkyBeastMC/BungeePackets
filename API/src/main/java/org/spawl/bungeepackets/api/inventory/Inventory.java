package org.spawl.bungeepackets.api.inventory;

import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by SkyBeast on 05/02/17.
 */
public interface Inventory
{
	int PLAYER_INVENTORY_SIZE = 36;

	static boolean closeInventories(ProxiedPlayer user)
	{
		throw new UnsupportedOperationException("close inventory");
	}

	void setSlots(int slots);

	int getSlots();

	void setTitle(String title);

	String getTitle();

	void setEntityID(int entityID);

	int getEntityID();

	void setType(InventoryType type);

	InventoryType getType();

	ItemStack getItem(int slot);

	default ItemStack getPlayerItem(int playerSlot)
	{
		return getItem(playerSlot + PLAYER_INVENTORY_SIZE);
	}

	void setItem(int slot, ItemStack stack);

	default void setPlayerItem(int playerSlot, ItemStack stack)
	{
		setItem(playerSlot + PLAYER_INVENTORY_SIZE, stack);
	}

	int addItem(ItemStack stack);

	default void setItem(int slot, ItemStack stack, ClickHandler handler)
	{
		setItem(slot, stack);
		setClickHandler(slot, handler);
	}

	default void setPlayerItem(int playerSlot, ItemStack stack, ClickHandler handler)
	{
		setItem(playerSlot + PLAYER_INVENTORY_SIZE, stack, handler);
	}

	default int addItem(ItemStack stack, ClickHandler handler)
	{
		int slot = addItem(stack);
		if (slot != -1)
			setClickHandler(slot, handler);
		return slot;
	}

	default void removeItem(ItemStack stack)
	{
		removeItem(stack, true);
	}

	void removeItem(ItemStack stack, boolean removeHandler);

	default void removeItem(int slot)
	{
		removeItem(slot, true);
	}

	void removeItem(int slot, boolean removeHandler);

	default void removePlayerItem(int slot)
	{
		removePlayerItem(slot, true);
	}

	default void removePlayerItem(int playerSlot, boolean removeHandler)
	{
		removeItem(playerSlot + PLAYER_INVENTORY_SIZE, removeHandler);
	}

	void setClickHandler(int slot, ClickHandler handler);

	default void setPlayerClickHandler(int playerSlot, ClickHandler handler)
	{
		setClickHandler(playerSlot + PLAYER_INVENTORY_SIZE, handler);
	}

	void removeClickHandler(int slot);

	default void removePlayerClickHandler(int playerSlot)
	{
		removeClickHandler(playerSlot + PLAYER_INVENTORY_SIZE);
	}

	void removeClickHandlers();

	void setCloseHandler(CloseHandler handler);

	void removeCloseHandler();

	boolean open(ProxiedPlayer user);

	void updateItem(ProxiedPlayer user, int slot);

	default void updatePlayerItem(ProxiedPlayer user, int playerSlot)
	{
		updateItem(user, playerSlot + PLAYER_INVENTORY_SIZE);
	}

	void updateItems(ProxiedPlayer user);

	boolean closeInventory(ProxiedPlayer user);

	@FunctionalInterface
	interface ClickHandler
	{
		void onClick(ProxiedPlayer player, int slot, ItemStack clicked, boolean rightClick, boolean shiftClick);
	}

	@FunctionalInterface
	interface CloseHandler
	{
		void onClose(ProxiedPlayer player, boolean clientDid);
	}
}
