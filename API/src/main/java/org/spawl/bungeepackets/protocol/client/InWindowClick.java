package org.spawl.bungeepackets.protocol.client;

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
public class InWindowClick extends DefinedPacket
{
	private int windowID;
	private int slot;
	private int button;
	private short actionNumber;
	private ItemStack item;
	private int shift;

	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		windowID = buf.readUnsignedByte();
		slot = buf.readShort();
		button = buf.readByte();
		actionNumber = buf.readShort();
		shift = buf.readByte();

		item = ItemStack.read(buf);
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		buf.writeByte(windowID);
		buf.writeShort(slot);
		buf.writeByte(button);
		buf.writeShort(actionNumber);
		buf.writeByte(shift);

		item.write(buf);
	}

	@Override
	public void handle(AbstractPacketHandler handler) throws Exception
	{
	}

}
