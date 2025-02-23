package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

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


	private int originalVolume;

	private boolean checkingNextSong;

	private int ticksChecked;



	@Override
	protected void startUp() throws Exception
	{
		originalVolume = client.getMusicVolume();
		resetCheckMusic();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Is music muted? " + client.getMusicVolume());
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		log.info("CURRENT GAMESTATE: " + gameStateChanged.getGameState());
	}


	@Subscribe
	public void onClientTick(ClientTick tick){
		clientThread.invokeLater(() -> {
			if (checkingNextSong && ticksChecked < 120){
				ticksChecked += 1;
				log.info("Game tick " + ticksChecked);
				printRequest();
				testRequest();
			} else if (checkingNextSong) {
				resetCheckMusic();
			} else if (client.getMusicVolume() == 0) {
				checkMusic();
			}
		});
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event){
		if (event.getVarpId() == VarPlayer.MUSIC_VOLUME){

			log.info("Music volume changed!" + event.getVarpId() + " = " + event.getValue());

		}
	}

	@Subscribe(priority = -2)
	public void onGameTick(GameTick tick){

	}

	@Subscribe
	public void onPostClientTick(PostClientTick tick){
		//log.info("vol " + client.getMusicVolume());

	}

	@Subscribe
	public void onVolumeChanged(VolumeChanged event){
		if (event.getType().equals(VolumeChanged.Type.MUSIC) && client.getMusicVolume() != 0){
			//stop checking song
			resetCheckMusic();
		}
	}

	private void testRequest(){
		List<MidiRequest> requests = client.getActiveMidiRequests();
		for(MidiRequest request : requests){
			log.info("---FOUND a next song: id: " + request.getArchiveId() + " ;is jingle? " + request.isJingle());
			resetCheckMusic();
		}
	}

	private void printRequest(){
		List<MidiRequest> requests = client.getActiveMidiRequests();
		for(MidiRequest request : requests){
			log.info("midireq: id: " + request.getArchiveId() + " ;is jingle? " + request.isJingle());
		}
	}

	public void checkMusic(){
		//clientThread.invokeAtTickEnd(() -> {client.setMusicVolume(50);});
		client.runScript(3966,30,10);	//[proc,settings_set_slider] script id 3966
		checkingNextSong = true;
		log.info("***CHECKING NEXT SONG");
	}

	public void resetCheckMusic(){
		checkingNextSong = false;
		ticksChecked = 0;
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
