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
public class OutWorldParticles extends DefinedPacket
{
	private int particleID;
	private boolean longDistance;
	private float x;
	private float y;
	private float z;
	private float offsetX;
	private float offsetY;
	private float offsetZ;
	private float speed;
	private int count;
	private int[] data;

	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		particleID = buf.readInt();
		longDistance = buf.readBoolean();
		x = buf.readFloat();
		y = buf.readFloat();
		z = buf.readFloat();
		offsetX = buf.readFloat();
		offsetY = buf.readFloat();
		offsetZ = buf.readFloat();
		speed = buf.readFloat();
		count = buf.readInt();
		int amount = 0;
		if (particleID == 36)
			amount = 2;
		if (particleID == 37 || particleID == 38)
			amount = 1;
		data = new int[amount];
		for (int i = 0; i < amount; i++)
			data[i] = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		buf.writeInt(particleID);
		buf.writeBoolean(longDistance);
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeFloat(z);
		buf.writeFloat(offsetX);
		buf.writeFloat(offsetY);
		buf.writeFloat(offsetZ);
		buf.writeFloat(speed);
		buf.writeInt(count);
		int amount = 0;
		if (particleID == 36)
			amount = 2;
		if (particleID == 37 || particleID == 38)
			amount = 1;
		for (int i = 0; i < amount; i++)
			writeVarInt(data[i], buf);
	}

	@Override
	public void handle(AbstractPacketHandler handler) throws Exception
	{

	}

}
