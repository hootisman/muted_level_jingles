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
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Unmuted Jingles"
)
public class UnmutedJinglesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private UnmutedJinglesConfig config;

	private JingleManager jingle;

	@Override
	protected void startUp() throws Exception
	{
		jingle = new JingleManager(client, clientThread, config);
	}

	@Subscribe
	public void onStatChanged(StatChanged e){
		log.info("*s* "+ e.toString());

		Skill skill = e.getSkill();
		int level = e.getLevel();
		int listedLevel = JingleData.SKILL_LEVELS.get(skill);


		//level never changed
		if (listedLevel == level) return;

		/*if ...
			- skill level set at game start
			- music is muted
			- widget S161.16 has no open window (bank window, trading window, etc.)
		 */
		if (JingleData.isLevelInited(skill)){
			jingle.startJingle();
			/*
            if (jingle.isWindowClosed()){
				jingle.startJingle();
			} else {
				log.info("*s* a window is open, jingle delayed");
				jingle.setJingleQueued(true);
			}

			 */
        }

		JingleData.SKILL_LEVELS.put(skill, level);
	}




	@Subscribe
	public void onGameTick(GameTick e){
		jingle.tickJingle();

		/*
		List<MidiRequest> reqs = client.getActiveMidiRequests();
		if (!reqs.isEmpty()){
			reqs.forEach(req -> {
				log.info("*G* req " + req.getArchiveId() + " " + req.isJingle());
				if (!req.isJingle()){
					log.info("*G* req is not jingle! muting");
					endJingle();
				}
			});

		}

		 */
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e){
		//todo: get other jingles to work
		/*
		log.info("*W* " + e.toString());

		if(e.getGroupId() == 193){
			Widget icon  = client.getWidget(ComponentID.DIALOG_SPRITE_SPRITE);

			if (icon != null && icon.getItemId() == 2996){
				log.info("*W* This is a brimhaven ticket!");
				startJingle();
			}
		}
		*/

		//level up window id
		//else if(e.getGroupId() == 233){
		//	startJingle();
		//}
	}



	@Provides
	UnmutedJinglesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(UnmutedJinglesConfig.class);
	}
}
