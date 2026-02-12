package hootisman.unmutedjingles;

import com.google.inject.Provides;
import javax.inject.Inject;

import hootisman.unmutedjingles.jingles.JingleManager;
import javax.inject.Named;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
			case "qmultij":
				var mtask = String.join(" ", e.getArguments());
				String ms = StringUtils.capitalize(mtask.split(" ")[0].toLowerCase());
				String ms2 = StringUtils.capitalize(mtask.split(" ")[2].toLowerCase());
				int mlevel = 42;
				int mlevel2 = 42;
				try
				{
					mlevel = Integer.parseInt(mtask.split(" ")[1]);
					mlevel2 = Integer.parseInt(mtask.split(" ")[3]);
				}
				catch (Exception ex)
				{
					// ignore
				}
				String msgTest0 = "Congratulations, you've just advanced your " + ms + " level. You are now level " + mlevel + ".";
				String msgTest1 = "Congratulations, you've just advanced your " + ms2 + " level. You are now level " + mlevel2 + ".";


				Matcher match0 = JingleManager.LEVEL_UP_MESSAGE_PATTERN.matcher(msgTest0);
				Matcher match1 = JingleManager.LEVEL_UP_MESSAGE_PATTERN.matcher(msgTest1);

				log.debug(match0.matches() ? "first skill matches" : "first skill does not match");
				log.debug(match1.matches() ? "second skill matches" : "second skill does not match");

				jingleManager.queueJingleWithChatMsg(match0);
				jingleManager.queueJingleWithChatMsg(match1);
			case "qlvlj":
				var task = String.join(" ", e.getArguments());
				String s = StringUtils.capitalize(task.split(" ")[0].toLowerCase());
				int level = 42;
				try
				{
					level = Integer.parseInt(task.split(" ")[1]);
				}
				catch (Exception ex)
				{
					// ignore
				}

				String msgTest = "Congratulations, you've just advanced your " + s + " level. You are now level " + level + ".";
				Matcher match = JingleManager.LEVEL_UP_MESSAGE_PATTERN.matcher(msgTest);
				log.debug(msgTest);
				log.debug(match.matches() ? "it matches" : "it does not match");
				jingleManager.queueJingleWithChatMsg(match);

				break;

			case "pj":
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
