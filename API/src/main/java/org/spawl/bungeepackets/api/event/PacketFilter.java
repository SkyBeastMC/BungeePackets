package org.spawl.bungeepackets.api.event;

import net.md_5.bungee.protocol.DefinedPacket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SkyBeast on 05/02/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PacketFilter
{
	Class<? extends DefinedPacket>[] value();
}
