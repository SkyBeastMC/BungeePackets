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
public class OutNamedSoundEffect extends DefinedPacket
{
	private String name;
	private int x;
	private int y;
	private int z;
	private float volume;
	private int pitch;

	public void setVolume(float volume)
	{
		if (volume > 1)
			volume = 1;
		this.volume = volume;
	}

	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		name = readString(buf);
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		volume = buf.readFloat();
		pitch = buf.readUnsignedByte();
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		writeString(name, buf);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeFloat(volume);
		buf.writeByte(pitch);
	}

	@Override
	public void handle(AbstractPacketHandler handler) throws Exception
	{

	}

}
