package org.spawl.bungeepackets.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.protocol.DefinedPacket;
import org.spawl.bungeepackets.BungeePacketsImpl;

import java.util.List;

@AllArgsConstructor
public class CustomEncoder extends MessageToMessageEncoder<DefinedPacket>
{
	private final Server server;
	private final ProxiedPlayer player;

	@Override
	protected void encode(ChannelHandlerContext context, DefinedPacket packet, List<Object> out)
			throws Exception
	{
		//CustomPacket send event. Either the server the client is connecting to is sending the packet or Bungee is.

		boolean cancel = server == null ?
				BungeePacketsImpl.dispatchBungeeToPlayer(packet, player) :
				BungeePacketsImpl.dispatchBungeeToServer(packet, player, server);

		if (!cancel)
			out.add(packet);
	}

}
