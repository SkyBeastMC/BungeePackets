package org.spawl.bungeepackets.skin;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MojangAPI
{
	private static final Gson GSON = new Gson();
	private static final Pattern DASH_FINDER = Pattern.compile("-", Pattern.LITERAL);

	private MojangAPI() {}

	public static UUID getUUID(String mcName)
	{
		try
		{
			URL url = new URL("http://api.mcuuid.com/txt/uuid/" + mcName);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String raw = in.readLine();

			return uuidFromString(raw);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Cannot connect to 'http://api.mcuuid.com/' for name " + mcName, e);
		}
	}

	private static UUID uuidFromString(String raw)
	{
		try {return UUID.fromString(raw);}
		catch (IllegalArgumentException ignored) {return null;}
	}

	public static PlayerProperties requestSkin(UUID player)
	{
		try
		{
			URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" +
					DASH_FINDER.matcher(player.toString()).replaceAll(Matcher.quoteReplacement("")) +
					"?unsigned=false");

			PlayerProperties props = GSON.fromJson(new BufferedReader(new InputStreamReader(url.openStream())),
					PlayerProperties.class);
			props.setId(player);
			return props;
		}
		catch (IOException e)
		{
			throw new RuntimeException("Cannot connect to 'http://api.mcuuid.com/' for uuid " + player, e);
		}
	}
}
