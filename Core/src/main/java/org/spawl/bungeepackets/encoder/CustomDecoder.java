package org.spawl.bungeepackets.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.protocol.PacketWrapper;
import org.spawl.bungeepackets.BungeePacketsImpl;

import java.util.List;

@AllArgsConstructor
public class CustomDecoder extends MessageToMessageDecoder<PacketWrapper>
{
	private final Server server;
	private final ProxiedPlayer player;

	@Override
	protected void decode(ChannelHandlerContext context, PacketWrapper wrapper, List<Object> out)
			throws Exception
	{
		//CustomPacket receive event. This can either be from the server or client!

		if (wrapper.packet == null)
		{
			out.add(wrapper);
			return;
		}

		boolean cancel = server == null ?
				BungeePacketsImpl.dispatchPlayerToBungee(wrapper.packet, player) :
				BungeePacketsImpl.dispatchServerToBungee(wrapper.packet, player, server);

		if (!cancel)
			out.add(wrapper);
	}
}
