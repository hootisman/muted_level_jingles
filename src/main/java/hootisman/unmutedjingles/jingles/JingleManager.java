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

    //Taken from the runelite screenshot plugin, thanks guys!
    public static final Pattern LEVEL_UP_PATTERN = Pattern.compile(".*Your ([a-zA-Z]+) (?:level is|are)? now (\\d+)\\.");
    public static final Pattern LEVEL_UP_MESSAGE_PATTERN = Pattern.compile("Congratulations, you've (just advanced your (?<skill>[a-zA-Z]+) level\\. You are now level (?<level>\\d+)|reached the highest possible (?<skill99>[a-zA-Z]+) level of 99)\\.");
    public static final String SOUNDS_PATH = "sounds/";

    public boolean widgetLevelUp;
    private LinkedList<Jingle> jingleQueue;


    public JingleManager(){
        widgetLevelUp = false;
        jingleQueue = new LinkedList<>();
    }

    public void startFirstJingle(){
        startJingle(jingleQueue.getFirst());
    }

    public void startLastJingle(){
        startJingle(jingleQueue.getLast());
    }

    private void startJingle(Jingle jingle){
        log.debug("*STARTJIN* starting jingle");

        try{
            audioPlayer.play(jingle.file, (float) config.jingleGain() - 50);
        }catch (Exception ex){
            log.debug("*STARTJIN* Jingle couldn't be played!");
        }
        jingleQueue.remove(jingle);
    }

    //checks every game tick to update the queue
    public void tickJingle(){
        if (jingleQueue.isEmpty() && !widgetLevelUp) return;

        if (isLevelPopupDisabled()){
            //Tries to start the most recent level up using Level Up chat message (not the widget)
            startLastJingle();
            jingleQueue.clear();
        }else {
            //Tries to get the Level Up widget (not the chat message)
            //Also from Screenshot Plugin
            Widget levelUpWidget = null;
            if(client.getWidget(InterfaceID.LevelupDisplay.TEXT2) != null){
                levelUpWidget = client.getWidget(InterfaceID.LevelupDisplay.TEXT2);
            }else if (client.getWidget(InterfaceID.Objectbox.TEXT) != null &&
                    !Text.removeTags(client.getWidget(InterfaceID.Objectbox.TEXT).getText()).contains("High level gamble")){
                levelUpWidget = client.getWidget(InterfaceID.Objectbox.TEXT);
            }

            if (levelUpWidget != null){
                Matcher m = LEVEL_UP_PATTERN.matcher(levelUpWidget.getText());
                queueJingleWithWidget(m);
                startFirstJingle();
            }
            widgetLevelUp = false;
        }

    }

    public boolean isLevelPopupDisabled(){
        return client.getVarbitValue(VarbitID.OPTION_LEVEL_UP_MESSAGE) == 1;
    }

    private void queueJingle(Skill skill, int level){
        log.debug("*QUEJIN* adding " + skill +" jingle");

        String path = SOUNDS_PATH + skill.name().toLowerCase() + (JingleData.UNLOCK_LEVELS.get(skill).contains(level) ? "_unlocks" : "") + ".wav";
        log.debug("*QUEJIN* Full path: " + path);

        File soundFile = new File(path);
        jingleQueue.add(Jingle.of(soundFile));

    }

    //nonskill jingles
    private void queueOtherJingle(String type){
        String path = "";
        switch (type){
            case("COMBAT"):
                path = SOUNDS_PATH + "combat" + ".wav";
                break;
            default:
                break;
        }

        File soundFile = new File(path);
        jingleQueue.add(Jingle.of(soundFile));
    }

    public void queueJingleWithChatMsg(Matcher m){
        if (m.matches()){
            String skill_str = m.group("skill").toUpperCase();
            String level_str = m.group("level");

            log.debug(skill_str);
            log.debug(level_str);

            try{
                if (skill_str.equals("COMBAT")){
                    queueOtherJingle("COMBAT");
                }else{
                    queueJingle(Skill.valueOf(skill_str), Integer.parseInt(level_str));
                }
            }catch (Exception ex){
                log.debug("*QUEJIN* Failed to queue jingle (chat msg)");
                jingleQueue.clear();
            }

        }
    }
    public void queueJingleWithWidget(Matcher m){
        if (m.matches()){
            String skill_str = m.group(1).toUpperCase();
            String level_str = m.group(2);

            log.debug(skill_str);
            log.debug(level_str);

            try{
                if (skill_str.equals("COMBAT")){
                    queueOtherJingle("COMBAT");
                }else{
                    queueJingle(Skill.valueOf(skill_str), Integer.parseInt(level_str));
                }
            }catch (Exception ex){
                log.debug("*QUEJIN* Failed to queue jingle (widget)");
                jingleQueue.clear();
            }
        }
    }

    public void clearJingleQueue(){
        jingleQueue.clear();
    }

    @AllArgsConstructor(access = AccessLevel.PROTECTED, staticName = "of")
    private static class Jingle {
        File file;
    }
}
