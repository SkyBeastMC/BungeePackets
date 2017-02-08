package org.spawl.bungeepackets.protocol.server;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;
import org.spawl.bungeepackets.api.util.Position;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class OutPosition extends DefinedPacket
{
	private double x, y, z;
	private float yaw, pitch;
	private byte value;

	public static OutPosition from(Position position, byte value)
	{
		return new OutPosition
				(
						position.getX(),
						position.getY(),
						position.getZ(),
						position.getYaw(),
						position.getPitch(),
						value
				);
	}

	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		yaw = buf.readFloat();
		pitch = buf.readFloat();
		value = buf.readByte();
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(yaw);
		buf.writeFloat(pitch);
		buf.writeByte(value);
	}

	@Override
	public void handle(AbstractPacketHandler handler) throws Exception
	{

	}

}
