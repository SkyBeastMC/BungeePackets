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
public class InFlying extends DefinedPacket
{

	private double x, y, z;
	private float yaw, pitch;
	private boolean f, hasPos, hasLook;

	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		f = buf.readByte() != 0;
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		buf.writeByte(f ? 1 : 0);
	}

	@Override
	public void handle(AbstractPacketHandler handler) throws Exception
	{
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class InLook extends InFlying
	{

		public InLook()
		{
			super.hasLook = true;
		}

		@Override
		public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
		{
			super.yaw = buf.readFloat();
			super.pitch = buf.readFloat();
			super.read(buf, direction, protocolVersion);
		}

		@Override
		public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
		{
			buf.writeFloat(super.yaw);
			buf.writeFloat(super.pitch);
			super.write(buf, direction, protocolVersion);
		}

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class InPosition extends InFlying
	{

		public InPosition()
		{
			super.hasPos = true;
		}

		@Override
		public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
		{
			super.x = buf.readDouble();
			super.y = buf.readDouble();
			super.z = buf.readDouble();
			super.read(buf, direction, protocolVersion);
		}

		@Override
		public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
		{
			buf.writeDouble(super.x);
			buf.writeDouble(super.y);
			buf.writeDouble(super.z);
			super.write(buf, direction, protocolVersion);
		}

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class InPositionLook extends InFlying
	{

		public InPositionLook()
		{
			super.hasPos = true;
			super.hasLook = true;
		}

		@Override
		public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
		{
			super.x = buf.readDouble();
			super.y = buf.readDouble();
			super.z = buf.readDouble();
			super.yaw = buf.readFloat();
			super.pitch = buf.readFloat();
			super.read(buf, direction, protocolVersion);
		}

		@Override
		public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
		{
			buf.writeDouble(super.x);
			buf.writeDouble(super.y);
			buf.writeDouble(super.z);
			buf.writeFloat(super.yaw);
			buf.writeFloat(super.pitch);
			super.write(buf, direction, protocolVersion);
		}

	}

}
