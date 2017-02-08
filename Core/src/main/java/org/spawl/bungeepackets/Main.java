package org.spawl.bungeepackets;

import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants;
import org.spawl.bungeepackets.api.event.ProtocolMapping;
import org.spawl.bungeepackets.encoder.CustomDecoder;
import org.spawl.bungeepackets.encoder.CustomEncoder;
import org.spawl.bungeepackets.inventory.InventoryManager;
import org.spawl.bungeepackets.protocol.client.InCloseWindow;
import org.spawl.bungeepackets.protocol.client.InWindowClick;
import org.spawl.bungeepackets.protocol.server.OutCloseWindow;
import org.spawl.bungeepackets.protocol.server.OutOpenWindow;
import org.spawl.bungeepackets.protocol.server.OutSetSlot;
import org.spawl.bungeepackets.protocol.server.OutWindowItems;

import java.lang.reflect.Field;

public class Main extends Plugin implements Listener
{
	private static final BungeePacketsImpl IMPL = new BungeePacketsImpl();
	private static final Field CHANNEL_FIELD;
	public static final String DECODER = "BungeePackets-decoder";
	public static final String ENCODER = "BungeePackets-encoder";

	static
	{
		try
		{
			CHANNEL_FIELD = UserConnection.class.getDeclaredField("ch");
			CHANNEL_FIELD.setAccessible(true);
		}
		catch (ReflectiveOperationException e)
		{
			throw new RuntimeException("Cannot access channel packet field", e);
		}
	}

	@Override
	public void onEnable()
	{
		BungeePacketsImpl.registerPacket(Protocol.GAME.TO_SERVER, InCloseWindow.class,
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_8, 0x0D),
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_9, 0x08));
		BungeePacketsImpl.registerPacket(Protocol.GAME.TO_SERVER, InWindowClick.class,
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_9, 0x07));
		//BungeePacketsImpl.registerPacket(Protocol.GAME.TO_SERVER, 4, InFlying.InPosition.class);
		//BungeePacketsImpl.registerPacket(Protocol.GAME.TO_SERVER, 5, InFlying.InLook.class);
		//BungeePacketsImpl.registerPacket(Protocol.GAME.TO_SERVER, 6, InFlying.InPositionLook.class);

		BungeePacketsImpl.registerPacket(Protocol.GAME.TO_CLIENT, OutCloseWindow.class,
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_8, 0x2E),
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_9, 0x12));
		BungeePacketsImpl.registerPacket(Protocol.GAME.TO_CLIENT, OutOpenWindow.class,
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_8, 0x2D),
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_9, 0x13));
		BungeePacketsImpl.registerPacket(Protocol.GAME.TO_CLIENT, OutSetSlot.class,
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_8, 0x2F),
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_9, 0x16));
		BungeePacketsImpl.registerPacket(Protocol.GAME.TO_CLIENT, OutWindowItems.class,
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_8, 0x30),
				new ProtocolMapping(ProtocolConstants.MINECRAFT_1_9, 0x14));
		//BungeePacketsImpl.registerPacket(Protocol.GAME.TO_CLIENT, 41, OutNamedSoundEffect.class);
		//BungeePacketsImpl.registerPacket(Protocol.GAME.TO_CLIENT, 42, OutWorldParticles.class);

		getProxy().getPluginManager().registerListener(this, InventoryManager.INSTANCE);
		getProxy().getPluginManager().registerListener(this, this);

		BungeePacketsImpl.registerPacketListener(InventoryManager.INSTANCE);
	}

	@EventHandler
	public void onServerConnected(ServerConnectedEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		ServerConnection server = (ServerConnection) event.getServer();
		if (player == null || server == null) return;

		inject(server.getCh(), player, event.getServer());
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent event)
	{
		ProxiedPlayer player = event.getPlayer();

		inject(getChannelWrapper(player), player, null);
	}

	private static ChannelWrapper getChannelWrapper(ProxiedPlayer player)
	{
		try
		{
			return (ChannelWrapper) CHANNEL_FIELD.get(player);
		}
		catch (ReflectiveOperationException e)
		{
			throw new RuntimeException("Cannot access a player's ChannelWrapper", e);
		}
	}

	private static void inject(ChannelWrapper wrapper, ProxiedPlayer player, Server server)
	{
		wrapper.getHandle().pipeline()
				.addAfter(PipelineUtils.PACKET_DECODER, DECODER, new CustomDecoder(server, player))
				.addAfter(PipelineUtils.PACKET_ENCODER, ENCODER, new CustomEncoder(server, player));
	}
}
