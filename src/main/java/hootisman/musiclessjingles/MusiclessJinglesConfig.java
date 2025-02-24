package hootisman.musiclessjingles;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("musiclessjingles")
public interface MusiclessJinglesConfig extends Config
{
	@Range(max=100)
	@ConfigItem(
		keyName = "jingleVolume",
		name = "Jingle Volume",
		description = "Volume when jingles are played"
	)
	default int jingleVolume()
	{
		return 50;
	}

}
