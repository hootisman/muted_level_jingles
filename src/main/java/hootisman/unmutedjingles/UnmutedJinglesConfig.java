package hootisman.unmutedjingles;

import net.runelite.client.config.*;

@ConfigGroup("unmutedjingles")
public interface UnmutedJinglesConfig extends Config
{

	@ConfigSection(
			name = "Enable/Disable",
			description = "Enable or disable certain jingles",
			position = 1
	)
	String enableSection = "Enable/Disable";

	@ConfigSection(
			name = "Priority",
			description = "Priority over which jingle plays when 2 or more overlap. Lower = higher priority",
			position = 2
	)
	String prioritySection = "Priority";

	@ConfigSection(
			name = "Debug",
			description = "Debugging tools",
			position = 3
	)
	String debugSection = "Debug";


	@Range(max=100)
	@ConfigItem(
			keyName = "jingleGain",
			name = "Jingle Volume",
			description = "Volume when jingles are played"
	)
	default int jingleGain() {return 50;}

	@ConfigItem(
			keyName = "levelEnabled",
			name = "Level Up Jingles",
			description = "Enable Level Up jingles",
			section = enableSection
	)
	default boolean levelEnabled() {return true;}

	@ConfigItem(
			keyName = "leaguesEnabled",
			name = "Leagues Jingles",
			description = "Enable Leagues jingles",
			section = enableSection
	)
	default boolean leaguesEnabled() {return true;}

	@ConfigItem(
			keyName = "otherEnabled",
			name = "Other Jingles",
			description = "Enable Other jingles (combat achievement)",
			section = enableSection
	)
	default boolean otherEnabled() {return true;}

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

	@Range(max=100)
	@ConfigItem(
			keyName = "otherPriority",
			name = "Other Priority",
			description = "Priority for other jingles (combat achievement)",
			section = prioritySection
	)
	default int otherPriority() {return 3;}


	@ConfigItem(
			keyName = "jingleTest",
			name = "Jingle Test",
			description = "When pressed, will play a jingle; Used to test volume",
			section = debugSection
	)
	default boolean jingleTest()
	{
		return false;
	}
}
