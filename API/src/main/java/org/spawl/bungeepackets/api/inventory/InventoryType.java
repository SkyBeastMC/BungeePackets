package org.spawl.bungeepackets.api.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by SkyBeast on 05/02/17.
 */
@AllArgsConstructor
@Getter
public enum InventoryType
{
	CONTAINER("minecraft:container"),
	CHEST("minecraft:chest"),
	CRAFTING_TABLE("minecraft:crafting_table"),
	FURNACE("minecraft:furnace"),
	DISPENSER("minecraft:dispenser"),
	ENCHANTING_TABLE("minecraft:enchanting_table"),
	BREWING_STAND("minecraft:brewing_stand"),
	VILLAGER("minecraft:villager"),
	BEACON("minecraft:beacon"),
	ANVIL("minecraft:anvil"),
	HOOPER("minecraft:hopper"),
	DROPPER("minecraft:dropper"),
	SHULKER_BOX("minecraft:shulker_box"),
	HORSE("EntityHorse");

	private final String id;
}
