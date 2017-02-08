package org.spawl.bungeepackets.protocol.server;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;
import org.spawl.bungeepackets.api.inventory.ItemStack;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class OutSetSlot extends DefinedPacket
{
	private int windowID;
	private int slot;
	private ItemStack item;

	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		windowID = buf.readByte();
		slot = buf.readShort();
		item = ItemStack.read(buf);
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		buf.writeByte(windowID);
		buf.writeShort(slot);
		if (item == null)
			buf.writeShort(-1);
		else
			item.write(buf);

	}

	@Override
	public void handle(AbstractPacketHandler handler) throws Exception
	{

	}

}
