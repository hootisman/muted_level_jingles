package hootisman.unmutedjingles;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("unmutedjingles")
public interface UnmutedJinglesConfig extends Config
{
	@Range(max=255)
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
