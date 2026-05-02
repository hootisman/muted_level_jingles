package hootisman.unmutedjingles;

import net.runelite.client.config.*;

@ConfigGroup("unmutedjingles")
public interface UnmutedJinglesConfig extends Config
{

	@ConfigSection(
			name = "Enable/Disable",
			description = "Enable or disable certain jingles",
			closedByDefault = true,
			position = 10
	)
	String enableSection = "Enable/Disable";

	@ConfigSection(
			name = "Priority",
			description = "Priority over which jingle plays when 2 or more overlap. Lower = higher priority",
			closedByDefault = true,
			position = 11
	)
	String prioritySection = "Priority";

	@ConfigSection(
			name = "Debug",
			description = "Debugging tools",
			closedByDefault = true,
			position = 12
	)
	String debugSection = "Debug";

	@Range(max=100)
	@ConfigItem(
			keyName = "jingleGain",
			name = "Jingle Volume",
			description = "Volume when jingles are played",
			position = 1
	)
	default int jingleGain() {return 50;}

	@ConfigItem(
			keyName = "altSailingJingle",
			name = "Alternate Sailing Level Up Jingle",
			description = "If enabled, will play alternate sailing level up jingle",
			position = 2
	)
	default boolean altSailingJingle()
	{
		return false;
	}

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
			keyName = "combatEnabled",
			name = "Combat Task Jingle",
			description = "Enable combat achievement jingle",
			section = enableSection
	)
	default boolean combatEnabled() {return true;}

	@Range(max=100)
	@ConfigItem(
			keyName = "levelPriority",
			name = "Level Up",
			description = "Priority for level up jingle",
			section = prioritySection
	)
	default int levelPriority() {return 2;}

	@Range(max=100)
	@ConfigItem(
			keyName = "leaguesPriority",
			name = "Leagues",
			description = "Priority for all leagues jingles",
			section = prioritySection
	)
	default int leaguesPriority() {return 1;}

	@Range(max=100)
	@ConfigItem(
			keyName = "combatPriority",
			name = "Combat Tasks",
			description = "Priority for combat achievement jingle",
			section = prioritySection
	)
	default int combatPriority() {return 3;}


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

	@ConfigItem(
			keyName = "sailingJingleTest",
			name = "Sailing Jingle Test",
			description = "When pressed, will play a sailing level up jingle; Used to test volume",
			section = debugSection
	)
	default boolean sailingJingleTest()
	{
		return false;
	}
}
