package org.spawl.bungeepackets.api.effect;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by SkyBeast on 04/02/17.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticleEffect
{
	private static final int[] EMPTY_DATA = new int[0];

	private final ParticleEffectType type;
	private final int[] data;

	private ParticleEffect(ParticleEffectType type)
	{
		this.type = type;
		data = EMPTY_DATA;
	}

	/**
	 * Used to get UNLIMITED block break animation.
	 *
	 * @param id   the windowID
	 * @param data the value
	 * @return the particle effect
	 */
	public static ParticleEffect getBlockCrack(int id, int data)
	{
		return new ParticleEffect(ParticleEffectType.BLOCK_CRACK, new int[]{id + (data << 12)});
	}

	/**
	 * Used to get UNLIMITED icon crack particle for items.
	 *
	 * @param id   the windowID
	 * @param data the value
	 * @return the particle effect
	 */
	public static ParticleEffect getIconCrack(int id, int data)
	{
		return new ParticleEffect(ParticleEffectType.ICON_CRACK, new int[]{id, data});
	}

	/**
	 * Used to get UNLIMITED dust particle from armor stands being broken.
	 *
	 * @param id the windowID
	 * @return the particle effect
	 */
	public static ParticleEffect getBlockDust(int id)
	{
		return new ParticleEffect(ParticleEffectType.BLOCK_DUST, new int[]{id});
	}
}
