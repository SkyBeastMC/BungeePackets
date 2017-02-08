package org.spawl.bungeepackets.skin;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.spawl.bungeepackets.api.nbt.NBTTagCompound;
import org.spawl.bungeepackets.api.nbt.NBTTagString;

import java.util.List;
import java.util.UUID;

/**
 * Created by SkyBeast on 05/02/17.
 */
@Data
public class PlayerProperties
{
	@Setter(AccessLevel.PACKAGE)
	private transient UUID id;
	private final String name;
	private final List<PlayerProperty> properties;

	public NBTTagCompound toNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();

		if (getName() != null)
			compound.setString("Name", getName());
		if (getId() != null)
			compound.setString("Id", getId().toString());
		if (!properties.isEmpty())
		{
			NBTTagCompound comp = new NBTTagCompound();
			properties.forEach
					(
							property ->
							{
								NBTTagCompound prop = new NBTTagCompound();
								prop.set("Value", new NBTTagString(property.getValue()));
								prop.set("Signature", new NBTTagString(property.getSignature()));
								comp.set(property.getName(), prop);
							}
					);

			compound.set("Properties", comp);
		}

		return compound;
	}
}
