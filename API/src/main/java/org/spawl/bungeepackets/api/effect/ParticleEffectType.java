package org.spawl.bungeepackets.api.effect;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("SpellCheckingInspection")
@Getter
@AllArgsConstructor
public enum ParticleEffectType
{
	EXPLODE("explode"),
	EXPLODE_LARGE("largeexplode"),
	EXPLODE_HUGE("hugeexplosion"),
	FIREWORK_SPARK("fireworksSpark"),
	BUBBLE("bubble"),
	SPLASH("splash"),
	WAKE("wake"),
	SUSPENDED("suspended"),
	DEPTH_SUSPEND("depthsuspend"),
	CRIT("crit"),
	CRIT_MAGIC("magicCrit"),
	SMOKE("smoke"),
	SMOKE_LARGE("largesmoke"),
	SPELL("spell"),
	SPELL_INSTANT("instantSpell"),
	SPELL_MOB("mobSpell"),
	SPELL_MOB_AMBIENT("mobSpellAmbient"),
	WITCH_MAGIC("witchMagic"),
	DRIP_WATER("dripWater"),
	DRIP_LAVA("dripLava"),
	VILLAGER_ANGRY("angryVillager"),
	VILLAGER_HAPPY("happyVillager"),
	TOWN_AURA("townaura"),
	NOTE("note"),
	PORTAL("portal"),
	ENCHANTMENT_TABLE("enchantmenttable"),
	FLAME("flame"),
	LAVA("lava"),
	FOOTSTEP("footstep"),
	CLOUD("cloud"),
	RED_DUST("reddust"),
	SNOWBALL_POOF("snowballpoof"),
	SNOW_SHOVEL("snowshovel"),
	SLIME("slime"),
	HEART("heart"),
	BARRIER("barrier"),
	ICON_CRACK("iconcrack"),
	BLOCK_CRACK("blockcrack"),
	BLOCK_DUST("blockdust"),
	DROPLET("droplet"),
	TAKE("take"),
	MOB_APPEARANCE("mobappearance"),
	DRAGON_BREATH("dragonbreath"),
	END_ROD("endRod"),
	DAMAGE_INDICATOR("damageIndicator"),
	SWEEP_ATTACK("sweepAttack");

	private final String name;

	public int getID()
	{
		return ordinal();
	}
}
