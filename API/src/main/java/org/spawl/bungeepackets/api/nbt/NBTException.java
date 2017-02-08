package org.spawl.bungeepackets.api.nbt;

import java.io.IOException;

/**
 * Created by SkyBeast on 04/02/17.
 */
public class NBTException extends IOException
{
	public NBTException() {}

	public NBTException(String var1) {super(var1);}

	public NBTException(String var1, Throwable var2) {super(var1, var2);}

	public NBTException(Throwable var1) {super(var1);}
}
