package hootisman.unmutedjingles;

import net.runelite.client.config.*;

@ConfigGroup("unmutedjingles")
public interface UnmutedJinglesConfig extends Config
{
	@ConfigSection(
			name = "Priority",
			description = "Priority over which jingle plays when 2 or more overlap. Lower = higher priority",
			position = 2
	)
	String prioritySection = "Priority";

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

	@Range(max=100)
	@ConfigItem(
			keyName = "levelPriority",
			name = "Level Up Priority",
			description = "Priority for level up jingle",
			section = prioritySection
	)
	default int levelPriority() {return 2;}

	@Range(max=100)
	@ConfigItem(
			keyName = "leaguesPriority",
			name = "Leagues Priority",
			description = "Priority for all leagues jingles",
			section = prioritySection
	)
	default int leaguesPriority() {return 1;}

}
