package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("example")
public interface ExampleConfig extends Config
{
	@Range(max=255)
	@ConfigItem(
		keyName = "setTestVolume",
		name = "Testing Volume",
		description = "Volume to set to when checking if next song is a jingle"
	)
	default int getTestVolume()
	{
		return 13;
	}

	@Range(min = 10, max = 500)
	@ConfigItem(
			keyName = "setTicksToCheck",
			name = "Ticks to check",
			description = "Number of client ticks to check for jingle (1 tick in 20ms)"
	)
	default int getTicksToCheck(){
		return 240;
	}
}
