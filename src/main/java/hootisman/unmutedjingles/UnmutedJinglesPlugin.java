package hootisman.unmutedjingles;

import com.google.inject.Provides;
import javax.inject.Inject;

import hootisman.unmutedjingles.jingles.JingleData;
import hootisman.unmutedjingles.jingles.JingleInfo;
import hootisman.unmutedjingles.jingles.JingleManager;
import javax.inject.Named;


import jaco.mp3.player.MP3Player;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.annotations.Varbit;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
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
	private EventBus eventBus;

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

	//my workaround for not playing varbit-based jingles once logged in
	private int currentTime;

	@Override
	protected void startUp() throws Exception
	{
		currentTime = -1;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged e){
		if (e.getGameState() == GameState.LOGGED_IN){
			//start timer
			currentTime = 0;
		}
	}

	@Subscribe
	public void onPluginChanged(PluginChanged e){

	}


	@Subscribe
	public void onVarbitChanged(VarbitChanged e){
		if (client.getMusicVolume() != 0 || isTimerTicking()) return;

		//TODO: move to jinglemanager
		String fileName = null;
		if (e.getVarbitId() == VarbitID.LEAGUE_TOTAL_TASKS_COMPLETED){
			fileName = "task_leagues";
		}
		if (JingleData.LEAGUES_RELIC_VARBITS.contains(e.getVarbitId())){
			fileName = "relic_leagues";
		}

		if(fileName != null){
			jingleManager.playJingle(JingleInfo.of(fileName, JingleInfo.Type.LEAGUES));
		}

	}

	@Subscribe
	public void onConfigChanged(ConfigChanged e){
		log.debug(getName());
		if (e.getGroup().equals("unmutedjingles") && e.getKey().equals("jingleTest")){
			try{
				jingleManager.playJingle(JingleInfo.of("woodcutting_unlocks", JingleInfo.Type.LEVEL));
			}catch (Exception ex){
				log.debug("Failed test audio");
			}

		}
	}

	@Subscribe
	public void onGameTick(GameTick e){
		if (isTimerTicking()){
			currentTime++;
			if (currentTime >= 3){
				currentTime = -1;
				log.debug("Timer disabled");
			}
		}
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

	boolean isTimerTicking(){
		return currentTime != -1;
	}

	//debugging commands
	@Subscribe
	public void onCommandExecuted(CommandExecuted e)
	{
		//if (!developerMode) return;

		String task = String.join(" ", e.getArguments());
		String skill = null;
		String skill2 = null;
		int level = 42;
		int level2 = 42;
		String command = e.getCommand();

		if (command.equals("uj-multi-level")){
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
		}else if (command.equals("uj-level")){
			skill = StringUtils.capitalize(task.split(" ")[0].toLowerCase());
			try
			{
				level = Integer.parseInt(task.split(" ")[1]);
			}
			catch (Exception ex)
			{
				// ignore
			}

			String msgTest = jingleManager.isLevelUpWidgetDisabled() ?
					"Congratulations, you've just advanced your " + skill + " level. You are now level " + level + "." :
					"Your "+ skill +" level is now " + level + ".";
			log.debug(msgTest);
			jingleManager.queueLevelJingle(msgTest);
		}else if (command.equals("uj-taskl")){
			VarbitChanged fake = new VarbitChanged();
			fake.setVarbitId(VarbitID.LEAGUE_TOTAL_TASKS_COMPLETED);
			fake.setValue(2);
			eventBus.post(fake);
		}else if (command.equals("uj-relicl")){
			VarbitChanged fake = new VarbitChanged();
			fake.setVarbitId(VarbitID.LEAGUE_RELIC_SELECTION_0);
			fake.setValue(2);
			eventBus.post(fake);
		}else if (command.equals("uj-priority")){
			VarbitChanged fake = new VarbitChanged();
			fake.setVarbitId(VarbitID.LEAGUE_TOTAL_TASKS_COMPLETED);
			fake.setValue(2);
			eventBus.post(fake);

			skill = StringUtils.capitalize(task.split(" ")[0].toLowerCase());
			try
			{
				level = Integer.parseInt(task.split(" ")[1]);
			}
			catch (Exception ex)
			{
				// ignore
			}

			String msgTest = jingleManager.isLevelUpWidgetDisabled() ?
					"Congratulations, you've just advanced your " + skill + " level. You are now level " + level + "." :
					"Your "+ skill +" level is now " + level + ".";
			log.debug(msgTest);
			jingleManager.queueLevelJingle(msgTest);
		}

	}
}
