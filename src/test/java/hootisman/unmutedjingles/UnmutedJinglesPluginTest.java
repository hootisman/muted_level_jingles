package hootisman.unmutedjingles;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class UnmutedJinglesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(UnmutedJinglesPlugin.class);
		RuneLite.main(args);
	}
}