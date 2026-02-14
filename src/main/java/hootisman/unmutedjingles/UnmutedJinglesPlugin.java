package hootisman.unmutedjingles;

import com.google.inject.Provides;
import javax.inject.Inject;

import hootisman.unmutedjingles.jingles.JingleManager;
import javax.inject.Named;


import jaco.mp3.player.MP3Player;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.music.MusicPlugin;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.regex.Matcher;

@Slf4j
@PluginDescriptor(
	name = "Unmuted Jingles"
)
@PluginDependency(MusicPlugin.class)
public class UnmutedJinglesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private MusicPlugin musicPlugin;

	@Inject
	private PluginManager pluginManager;

	@Inject
	private UnmutedJinglesConfig config;

	@Inject
	private JingleManager jingleManager;

	@Inject
	@Named("developerMode")
	boolean developerMode;

	@Override
	protected void startUp() throws Exception
	{

	}

	@Subscribe
	public void onPluginChanged(PluginChanged e){

	}


	@Subscribe
	public void onConfigChanged(ConfigChanged e){
		log.debug(getName());
		if (e.getGroup().equals("unmutedjingles") && e.getKey().equals("jingleTest")){
			try{
				jingleManager.playJingle("woodcutting_unlocks");
			}catch (Exception ex){
				log.debug("Failed test audio");
			}

		}
	}

	@Subscribe
	public void onGameTick(GameTick e){
		jingleManager.tickJingle();
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e){
		if (client.getMusicVolume() != 0 || jingleManager.isLevelUpWidgetDisabled()) return;

		//If a level up display widget shows up, flag that it showed up; jingle queue handled in jinglemanager
		if (e.getGroupId() == InterfaceID.LEVELUP_DISPLAY){
			jingleManager.widgetLevelUp = true;
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage e){
		if(client.getMusicVolume() != 0 || !jingleManager.isLevelUpWidgetDisabled()) return;

		//If a level up chat message shows up, start queueing a jingle
		jingleManager.queueLevelJingle(e.getMessage());
	}

	@Provides
	UnmutedJinglesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(UnmutedJinglesConfig.class);
	}

	//debugging commands
	@Subscribe
	public void onCommandExecuted(CommandExecuted e)
	{
		if (!developerMode) return;

		String task = String.join(" ", e.getArguments());
		String skill = null;
		String skill2 = null;
		int level = 42;
		int level2 = 42;

		switch(e.getCommand()){
			case "qmlj":
				skill = StringUtils.capitalize(task.split(" ")[0].toLowerCase());
				skill2 = StringUtils.capitalize(task.split(" ")[2].toLowerCase());
				try
				{
					level = Integer.parseInt(task.split(" ")[1]);
					level2 = Integer.parseInt(task.split(" ")[3]);
				}
				catch (Exception ex)
				{
					// ignore
				}
				String msgTest0 = "Congratulations, you've just advanced your " + skill + " level. You are now level " + level + ".";
				String msgTest1 = "Congratulations, you've just advanced your " + skill2 + " level. You are now level " + level2 + ".";

				jingleManager.queueLevelJingle(msgTest0);
				jingleManager.queueLevelJingle(msgTest1);
				break;
			case "qlj":
				skill = StringUtils.capitalize(task.split(" ")[0].toLowerCase());
				try
				{
					level = Integer.parseInt(task.split(" ")[1]);
				}
				catch (Exception ex)
				{
					// ignore
				}

				String msgTest = "Congratulations, you've just advanced your " + skill + " level. You are now level " + level + ".";
				log.debug(msgTest);
				jingleManager.queueLevelJingle(msgTest);

				break;

			case "pj":
				jingleManager.playJingle(task);
				break;

			default:
				break;

		}
	}
}
