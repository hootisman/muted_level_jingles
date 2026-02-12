package hootisman.unmutedjingles.jingles;

import hootisman.unmutedjingles.UnmutedJinglesConfig;
import javax.inject.Named;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.audio.AudioPlayer;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.util.Text;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Singleton
public class JingleManager {

    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private UnmutedJinglesConfig config;

    @Inject
    private AudioPlayer audioPlayer;

	@Inject
	@Named("developerMode")
	boolean developerMode;

    public static final String SOUNDS_PATH = "sounds/";

    //Taken from the runelite screenshot plugin, thanks guys!
    public static final Pattern LEVEL_UP_PATTERN = Pattern.compile(".*Your ([a-zA-Z]+) (?:level is|are)? now (\\d+)\\.");
    public static final Pattern LEVEL_UP_MESSAGE_PATTERN = Pattern.compile("Congratulations, you've (just advanced your (?<skill>[a-zA-Z]+) level\\. You are now level (?<level>\\d+)|reached the highest possible (?<skill99>[a-zA-Z]+) level of 99)\\.");



    private int jingleTick;

    private LinkedList<Jingle> jingleQueue;


    public JingleManager(){
        //jingleTick = -1;
        jingleQueue = new LinkedList<>();
    }



    private void queueJingle(Skill skill, int level){
        log.debug("*j* adding " + skill +" jingle");

        int oldLevel = JingleData.SKILL_LEVELS.get(skill);
        //level never changed
        if (oldLevel == level) return;


        String path = SOUNDS_PATH + skill.name().toLowerCase() + (JingleData.UNLOCK_LEVELS.get(skill).contains(level) ? "_unlocks" : "") + ".wav";
        File soundFile = new File(path);
        jingleQueue.add(Jingle.of(soundFile));

        JingleData.SKILL_LEVELS.put(skill, level);
    }

    public void startFirstJingle(){
        startJingle(jingleQueue.getFirst());
    }

    public void startLastJingle(){
        startJingle(jingleQueue.getLast());
    }

    private void startJingle(Jingle jingle){
        log.debug("*j* starting jingle");

        //jingleTick = 0;
        try{
            audioPlayer.play(jingle.file, (float) 1.0);
        }catch (Exception ex){
            log.debug("*j* Jingle couldn't be played!");
        }
        jingleQueue.remove(jingle);
    }


    public void endJingle(){
        log.debug("*j* jingle ended, muting...");
        //jingleTick = -1;
        jingleQueue.remove();
    }

    //checks every game tick to update the queue
    public void tickJingle(){
        if (jingleQueue.isEmpty()) return;

        if (isLevelPopupDisabled()){
            //Tries to start the most recent level up using Level Up chat message (not the widget)
            startLastJingle();
            jingleQueue.clear();
        }else {
            //Tries to get the Level Up widget (not the chat message)
            Widget levelUpWidget = null;
            if(client.getWidget(InterfaceID.LevelupDisplay.TEXT2) != null){
                levelUpWidget = client.getWidget(InterfaceID.LevelupDisplay.TEXT2);
            }else if (client.getWidget(InterfaceID.Objectbox.TEXT) != null &&
                    !Text.removeTags(client.getWidget(InterfaceID.Objectbox.TEXT).getText()).contains("High level gamble")){
                levelUpWidget = client.getWidget(InterfaceID.Objectbox.TEXT);
            }

            if (levelUpWidget != null){
                Matcher m = LEVEL_UP_PATTERN.matcher(levelUpWidget.getText());
                queueJingleFromMatcher(m);

            }
        }


        //if there was a window blocking jingle AND jingle isnt playing, then start it
        //if (isWindowClosed() && ) startJingle();

        /*
        if (jingleTick > jingleQueue.getFirst().duration - 1){
            endJingle();
        }else if (jingleTick != -1){
            jingleTick += 1;
            log.debug("*G* " + jingleTick);
        }
         */

    }


    //if widget S161.16 has 1 or more children, then return true
    //represents if theres a window blocking the level up message or not.
    public boolean isWindowClosed() {
        return developerMode || Objects.requireNonNull(client.getWidget(161, 16)).getNestedChildren().length == 0;
    }

    public boolean isLevelPopupDisabled(){
        return client.getVarbitValue(VarbitID.OPTION_LEVEL_UP_MESSAGE) == 1;
    }

    public void queueJingleFromMatcher(Matcher m){
        if (m.matches()){
            String skill_str = m.group("skill") != null ? m.group("skill").toLowerCase() : "UNKNOWN_SKILL";
            String level_str = m.group("level") != null ? m.group("level") : "0";

            log.debug(skill_str);
            log.debug(level_str);

            log.debug(Skill.valueOf(skill_str) + "");
            log.debug(Integer.parseInt(level_str) + "");

            try{
                queueJingle(Skill.valueOf(skill_str), Integer.parseInt(level_str));
            }catch (Exception ex){
                log.debug("Failed to queue jingle");
            }



        }
    }

    @AllArgsConstructor(access = AccessLevel.PROTECTED, staticName = "of")
    private static class Jingle {
        //TODO: delete
        //int duration;
        //@Setter
        //boolean isJinglePlaying;    //when music is unmuted we might need to know if its because of user or jingle

        File file;
    }
}
