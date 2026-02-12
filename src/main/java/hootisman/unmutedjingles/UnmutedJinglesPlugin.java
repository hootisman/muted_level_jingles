package hootisman.unmutedjingles;

import com.google.inject.Provides;
import javax.inject.Inject;

import hootisman.unmutedjingles.jingles.JingleManager;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.audio.AudioPlayer;
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
	private AudioPlayer audioPlayer;

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
			log.debug("jingletest pressed!");
			try{
				File file = new File("sounds/woodcutting_unlocks.wav");
				audioPlayer.play(file, (float) config.jingleGain() - 50);
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
		if (client.getMusicVolume() != 0 || jingleManager.isLevelPopupDisabled()) return;

		//If a level up display widget shows up, flag that it showed up; jingle queue handled in jinglemanager
		if (e.getGroupId() == InterfaceID.LEVELUP_DISPLAY){
			log.debug("*WIDLOD* level up via widget!");
			jingleManager.widgetLevelUp = true;
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage e){
		if(client.getMusicVolume() != 0 || !jingleManager.isLevelPopupDisabled()) return;

		//If a level up chat message shows up, start queueing a jingle
		Matcher m = JingleManager.LEVEL_UP_MESSAGE_PATTERN.matcher(e.getMessage());
		jingleManager.queueJingleWithChatMsg(m);
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged e){
		log.debug(e.toString());
		if (e.getVarbitId() == VarbitID.OPTION_LEVEL_UP_MESSAGE){
			jingleManager.clearJingleQueue();
		}
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

		switch(e.getCommand()){
			/*
			case "qj":
				var task = String.join(" ", e.getArguments());
				Skill s = Skill.valueOf(task.split(" ")[0].toUpperCase());
				int level = 42;
				try
				{
					level = Integer.parseInt(task.split(" ")[1]);
				}
				catch (Exception ex)
				{
					// ignore
				}
				if (s != null)
				{
					queueJingle(s, level);
				}
				break;

			 */
			case "testjin":
				log.debug("testing jingle");
				//JingleData.SKILL_LEVELS.put(Skill.WOODCUTTING, -1);
				Matcher m = JingleManager.LEVEL_UP_MESSAGE_PATTERN.matcher("Congratulations, you've just advanced your Woodcutting level. You are now level 42.");
				jingleManager.queueJingleWithChatMsg(m);
				break;

			case "testcombat":
				log.debug("testing combat jingle");
				Matcher m1 = JingleManager.LEVEL_UP_MESSAGE_PATTERN.matcher("Congratulations, you've just advanced your Combat level. You are now level 42.");
				jingleManager.queueJingleWithChatMsg(m1);
				break;

			case "pj":
				//play a file using audioplayer
				var soundName = String.join(" ", e.getArguments());

				File sound = new File("sounds/" + soundName + ".wav");
				log.debug("playing jingle");
				log.debug(sound.getAbsolutePath());

				try{
					audioPlayer.play(sound, (float) 1.0);
					//audioPlayer.play(new File("sounds/combat.wav"), (float) 1.0);
				}catch(Exception ex){}


			default:
				break;
		}
	}
}
