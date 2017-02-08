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
public class OutWindowItems extends DefinedPacket
{
	private int windowID;
	private ItemStack[] items;

	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		windowID = buf.readUnsignedByte();
		int i = buf.readShort();
		items = new ItemStack[i];
		for (int j = 0; j < i; j++)
			items[j] = ItemStack.read(buf);
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		buf.writeByte(windowID);
		buf.writeShort(items.length);
		for (ItemStack stack : items)
			stack.write(buf);
	}

	@Override
	public void handle(AbstractPacketHandler handler) throws Exception
	{

	}

}
