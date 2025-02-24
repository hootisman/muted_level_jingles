package hootisman.musiclessjingles;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MusiclessJinglesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MusiclessJinglesPlugin.class);
		RuneLite.main(args);
	}
}