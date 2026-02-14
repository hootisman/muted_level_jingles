package hootisman.unmutedjingles;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("unmutedjingles")
public interface UnmutedJinglesConfig extends Config
{
	@ConfigItem(
			keyName = "jingleTest",
			name = "Jingle Test",
			description = "When pressed, will play a jingle; Used to test volume"
	)
	default boolean jingleTest()
	{
		return false;
	}

	@Range(max=100)
	@ConfigItem(
			keyName = "jingleGain",
			name = "Jingle Volume",
			description = "Volume when jingles are played"
	)
	default int jingleGain() {return 50;}
}
