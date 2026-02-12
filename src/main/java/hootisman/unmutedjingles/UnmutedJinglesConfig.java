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

	@Range(max=56)
	@ConfigItem(
			keyName = "jingleGain",
			name = "Jingle Gain/Volume",
			description = "Volume when jingles are played"
	)
	default double jingleGain() {return 47.0;}
}
