package hootisman.unmutedjingles.jingles;

import hootisman.unmutedjingles.UnmutedJinglesConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.music.MusicConfig;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class JingleManager {

    @Inject
    private Client client;

    @Setter
    @Nullable
    private MusicConfig musicPluginConfig;

    @Inject
    private ClientThread clientThread;

    @Inject
    private UnmutedJinglesConfig config;

    private int jingleTick;

    private boolean isJingleQueued;

    public JingleManager(){
        jingleTick = -1;
        isJingleQueued = false;

    }

    public void startJingle(){
        if (client.getMusicVolume() != 0) return;

        if (!isWindowClosed()){
            //there is an open window
            if (!isJingleQueued) isJingleQueued = true;
            return;
        }

        log.info("*j* unmuting for jingle...");

        toggleMusicVolume(false);
        jingleTick = 0;
        isJingleQueued = false;
    }

    public void endJingle(){
        log.info("*j* jingle ended, muting...");

        toggleMusicVolume(true);
        jingleTick = -1;

    }

    public void tickJingle(){
        if(isJingleQueued) startJingle();

        if(jingleTick > 10){
            endJingle();
        }else if (jingleTick != -1){
            jingleTick += 1;
            log.info("*G* " + jingleTick);
        }
    }

    void toggleMusicVolume(boolean shouldMute){
        int val;
        if (musicPluginConfig == null || !musicPluginConfig.granularSliders()){
            val = shouldMute ? 0 : config.jingleVolume();
            client.setMusicVolume(val);
        }else{
            val = shouldMute ? -config.jingleVolume() : config.jingleVolume();
            musicPluginConfig.setMusicVolume(val);
        }

    }

    //if widget S161.16 has 1 or more children, then return true
    public boolean isWindowClosed() {
        return client.getWidget(161, 16).getNestedChildren().length == 0;
    }

}
