package org.spawl.bungeepackets.api.event;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.protocol.DefinedPacket;

/**
 * Created by SkyBeast on 05/02/17.
 */
public interface PacketListener
{
	default boolean onBungeeToPlayer(DefinedPacket packet, ProxiedPlayer user)
	{
		return false;
	}

	default boolean onBungeeToServer(DefinedPacket packet, ProxiedPlayer user, Server server)
	{
		return false;
	}

	default boolean onPlayerToBungee(DefinedPacket packet, ProxiedPlayer user)
	{
		return false;
	}

	default boolean onServerToBungee(DefinedPacket packet, ProxiedPlayer user, Server server)
	{
		return false;
	}
}
