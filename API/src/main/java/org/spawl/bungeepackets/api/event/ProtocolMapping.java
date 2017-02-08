package org.spawl.bungeepackets.api.event;

import lombok.Data;

/**
 * Created by SkyBeast on 08/02/17.
 */
@Data
public class ProtocolMapping
{
	private final int protocol;
	private final int packetID;
}
