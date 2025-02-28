package hootisman.unmutedjingles;

import com.google.inject.Provides;
import javax.inject.Inject;

import hootisman.unmutedjingles.jingles.JingleData;
import hootisman.unmutedjingles.jingles.JingleManager;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
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

import java.util.List;

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

	@Override
	protected void startUp() throws Exception
	{
		log.debug("*START* Setting music plugin; isEnabled" + pluginManager.isPluginEnabled(musicPlugin));
		jingleManager.setMusicPluginConfig(pluginManager.isPluginEnabled(musicPlugin) ? (MusicConfig) pluginManager.getPluginConfigProxy(musicPlugin) : null);
	}


	@Subscribe
	public void onPluginChanged(PluginChanged e){
		if (!e.getPlugin().equals(musicPlugin)) return;

		log.debug("*P* Setting music plugin; isLoaded" + e.isLoaded());
		jingleManager.setMusicPluginConfig(e.isLoaded() ? (MusicConfig) pluginManager.getPluginConfigProxy(musicPlugin) : null);

	}

	@Subscribe
	public void onStatChanged(StatChanged e){
		Skill skill = e.getSkill();
		int level = e.getLevel();
		int listedLevel = JingleData.SKILL_LEVELS.get(skill);

		log.debug("*stat* " + e.toString());

		//level never changed
		if (listedLevel == level) return;

		if (JingleData.isLevelInited(skill)){
			jingleManager.queueJingle(skill, level);
        }

		JingleData.SKILL_LEVELS.put(skill, level);
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

	/*
	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e){
		//todo: get other jingles to work
		log.info("*W* " + e.toString());

		if(e.getGroupId() == 193){
			Widget icon  = client.getWidget(ComponentID.DIALOG_SPRITE_SPRITE);

			if (icon != null && icon.getItemId() == 2996){
				log.info("*W* This is a brimhaven ticket!");
				startJingle();
			}
		}

		//level up window id
		//else if(e.getGroupId() == 233){
		//	startJingle();
		//}
	}

	 */


	@Provides
	UnmutedJinglesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(UnmutedJinglesConfig.class);
	}
}
