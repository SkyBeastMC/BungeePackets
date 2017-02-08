package org.spawl.bungeepackets.protocol.server;

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
public class OutOpenWindow extends DefinedPacket {
	
	private int id;
	private String windowType;
	private String title;
	private int slots;
	/** The entity ID is only used for entities like horses. */
	private int entityID;
	
	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
		id = buf.readByte();
		windowType = DefinedPacket.readString(buf);
		title = DefinedPacket.readString(buf);
		slots = buf.readUnsignedByte();
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
		buf.writeByte(id);
		writeString(windowType, buf);
		writeString(title, buf);
		buf.writeByte(slots);
	}

	@Override
	public void handle(AbstractPacketHandler handler) throws Exception
	{

	}

}
