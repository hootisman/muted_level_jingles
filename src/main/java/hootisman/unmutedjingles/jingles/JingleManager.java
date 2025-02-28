package hootisman.unmutedjingles.jingles;

import hootisman.unmutedjingles.UnmutedJinglesConfig;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.music.MusicConfig;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.Objects;

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

    private LinkedList<Jingle> jingleQueue;


    public JingleManager(){
        jingleTick = -1;
        jingleQueue = new LinkedList<>();
    }



    public void queueJingle(Skill skill, int level){
        if (jingleQueue.isEmpty() && !isMusicMuted()) return;

        int duration = JingleData.JINGLE_DURATIONS.get(skill).apply(level);
        log.debug("*j* adding " + skill +" jingle of " + duration);
        jingleQueue.add(Jingle.of(duration, false));

        if (!isWindowClosed()){
            //there is an open window
            return;
        }

        startJingle();
    }

    private void startJingle(){
        Jingle jingle = jingleQueue.getFirst();

        muteMusic(false);
        log.debug("*j* unmuting jingle for " + jingle.duration + " ticks...");
        jingleTick = 0;
        jingle.setJinglePlaying(true);
    }

    public void endJingle(){
        muteMusic(true);
        log.debug("*j* jingle ended, muting...");
        jingleTick = -1;
        jingleQueue.remove();
    }

    public void tickJingle(){
        if (jingleQueue.isEmpty()) return;

        if (isWindowClosed() && !jingleQueue.getFirst().isJinglePlaying) startJingle();

        if (jingleTick > jingleQueue.getFirst().duration - 1){
            endJingle();
        }else if (jingleTick != -1){
            jingleTick += 1;
            log.debug("*G* " + jingleTick);
        }
    }

    void muteMusic(boolean shouldMute){
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
        return Objects.requireNonNull(client.getWidget(161, 16)).getNestedChildren().length == 0;
    }

    public boolean isMusicMuted(){
        return client.getMusicVolume() == 0;
    }

    @AllArgsConstructor(access = AccessLevel.PROTECTED, staticName = "of")
    private static class Jingle {
        int duration;
        @Setter
        boolean isJinglePlaying;    //when music is unmuted we might need to know if its because of user or jingle
    }
}
