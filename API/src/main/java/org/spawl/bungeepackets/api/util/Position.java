package org.spawl.bungeepackets.api.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position
{
	private double x, y, z;
	private float yaw, pitch;

	public Position(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
