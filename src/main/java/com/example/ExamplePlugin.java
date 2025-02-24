package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import javax.sound.midi.*;


import java.util.Arrays;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Unmute Level Jingles"
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ExampleConfig config;

	private int jingleTick;

	private boolean isJingleQueued;

	@Override
	protected void startUp() throws Exception
	{
		byte[] data = 	client.getIndex(11).loadData(28, 0);
		jingleTick = 0;
		isJingleQueued = false;


		log.info("***Start");
		log.info(Arrays.toString(data));
		log.info("***End");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Is music muted? " + client.getMusicVolume());
		log.info("Example stopped!");
	}

	@Subscribe
	public void onStatChanged(StatChanged e){
		log.info("*s* "+ e.toString());

		Skill skill = e.getSkill();
		int level = e.getLevel();
		int listedLevel = PlayerStats.SKILL_LEVELS.get(skill);

		//level never changed
		if (listedLevel == level) return;

		/*if ...
			- not during init phase
			- music is muted
			- widget 161.16 has no open window (bank window, trading window, etc.)
		 */
		if (listedLevel != -1 && client.getMusicVolume() == 0 ){
			log.info("*s* leveled up while muted!");
            if (client.getWidget(161, 16).getNestedChildren().length == 0){
				startJingle();
			} else {
				log.info("*s* a window is open, jingle delayed");
				isJingleQueued = true;
			}
        }

		PlayerStats.SKILL_LEVELS.put(skill, level);
	}

	private void startJingle(){
		if (client.getMusicVolume() == 0){
			log.info("*j* unmuting for jingle...");
			clientThread.invoke(() -> {
				client.setMusicVolume(50);
				jingleTick = 1;
				isJingleQueued = false;
			});
		}
	}

	private void endJingle(){
		log.info("*j* jingle ended, muting...");
		client.setMusicVolume(0);
		jingleTick = 0;
	}

	@Subscribe
	public void onGameTick(GameTick e){
		if(isJingleQueued && client.getWidget(161,16).getNestedChildren().length == 0){
			startJingle();
		}

		if(jingleTick > 10){
			endJingle();
		}else if (jingleTick != 0){
			jingleTick += 1;
			log.info("*G* " + jingleTick);
		}
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
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
