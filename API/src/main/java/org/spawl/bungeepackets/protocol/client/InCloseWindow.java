package org.spawl.bungeepackets.protocol.client;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InCloseWindow extends DefinedPacket
{
	private int windowID;

	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		windowID = buf.readByte();
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		buf.writeByte(windowID);
	}

	@Override
	public void handle(AbstractPacketHandler handler) throws Exception
	{
	}

}
