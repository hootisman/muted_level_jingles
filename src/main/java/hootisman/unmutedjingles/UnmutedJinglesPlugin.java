package hootisman.unmutedjingles;

import com.formdev.flatlaf.util.StringUtils;
import com.google.inject.Provides;
import javax.inject.Inject;

import hootisman.unmutedjingles.jingles.JingleData;
import hootisman.unmutedjingles.jingles.JingleManager;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.audio.AudioPlayer;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.music.MusicConfig;
import net.runelite.client.plugins.music.MusicPlugin;
import net.runelite.client.plugins.screenshot.ScreenshotPlugin;

import java.io.File;
import java.util.List;
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

	//todo: there should be some sort of level check beforehand
	@Subscribe
	public void onStatChanged(StatChanged e){
		if(client.getMusicVolume() != 0) return;


		//log.debug("*stat* " + e.toString());
		//queueJingle(e.getSkill(), e.getLevel());
	}

	//todo: delete
	//attempts to queue a jingle
	private void queueJingle(Skill skill, int level)
	{
		int oldLevel = JingleData.SKILL_LEVELS.get(skill);

		//level never changed
		if (oldLevel == level) return;

		if (JingleData.isLevelInited(skill)){
			//jingleManager.queueJingle(skill, level);
		}

		JingleData.SKILL_LEVELS.put(skill, level);
	}


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
			case "jtcp":
				String testparsee = String.join(" ", e.getArguments());
				log.debug("\"" + testparsee + "\"");
				Matcher m = JingleManager.LEVEL_UP_MESSAGE_PATTERN.matcher(testparsee);
				jingleManager.queueJingleFromMatcher(m);
				break;
			case "jtwp":
				String testparsee2 = String.join(" ", e.getArguments());
				log.debug("\"" + testparsee2 + "\"");
				Matcher m2 = JingleManager.LEVEL_UP_PATTERN.matcher(testparsee2);
				jingleManager.queueJingleFromMatcher(m2);
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



	@Subscribe
	public void onGameTick(GameTick e){
		jingleManager.tickJingle();

		/*
		List<MidiRequest> reqs = client.getActiveMidiRequests();
		if (!reqs.isEmpty()){
			reqs.forEach(req -> {
				log.info("*G* req " + req.getArchiveId() + " " + req.isJingle());
			});

		}
		 */
	}


	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e){
		/*
		if (client.getMusicVolume() != 0 || jingleManager.isLevelPopupDisabled()) return;



		if (e.getGroupId() == InterfaceID.LEVELUP_DISPLAY){

		}
*/

	}

	@Subscribe
	public void onChatMessage(ChatMessage e){
		if(client.getMusicVolume() != 0 || !jingleManager.isLevelPopupDisabled()) return;

		Matcher m = JingleManager.LEVEL_UP_MESSAGE_PATTERN.matcher(e.getMessage());
		jingleManager.queueJingleFromMatcher(m);
	}



	@Provides
	UnmutedJinglesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(UnmutedJinglesConfig.class);
	}
}
