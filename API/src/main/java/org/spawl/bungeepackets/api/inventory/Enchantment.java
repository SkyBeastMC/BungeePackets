package org.spawl.bungeepackets.api.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Enchantment
{
	PROTECTION_ENVIRONMENTAL(0),
	PROTECTION_FIRE(1),
	PROTECTION_FALL(2),
	PROTECTION_EXPLOSIONS(3),
	PROTECTION_PROJECTILE(4),
	OXYGEN(5),
	WATER_WORKER(6),
	THORNS(7),
	DEPTH_STRIDER(8),
	DAMAGE_ALL(16),
	DAMAGE_UNDEAD(17),
	DAMAGE_ARTHROPODS(18),
	KNOCKBACK(19),
	FIRE_ASPECT(20),
	LOOT_BONUS_MOBS(21),
	DIG_SPEED(32),
	SILK_TOUCH(33),
	DURABILITY(34),
	LOOT_BONUS_BLOCKS(35),
	ARROW_DAMAGE(48),
	ARROW_KNOCKBACK(49),
	ARROW_FIRE(50),
	ARROW_INFINITE(51),
	LUCK(61),
	LURE(62),
	FAKE_GLOW(100);

	private final int id;

	public static Enchantment get(int id) {
		for(Enchantment enchantment : values())
			if(enchantment.getId() == id)
				return enchantment;

		return null;
	}

}
