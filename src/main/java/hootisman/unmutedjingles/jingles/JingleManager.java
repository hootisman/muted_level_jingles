package hootisman.unmutedjingles.jingles;

import hootisman.unmutedjingles.UnmutedJinglesConfig;
import javax.inject.Named;

import jaco.mp3.player.MP3Player;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
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
	@Named("developerMode")
	boolean developerMode;

    //Taken from the runelite screenshot plugin, thanks guys!
    //widget level up message
    public static final Pattern LEVEL_UP_PATTERN = Pattern.compile(".*Your ([a-zA-Z]+) (?:level is|are)? now (\\d+)\\.");

    //chat level up message
    public static final Pattern LEVEL_UP_MESSAGE_PATTERN = Pattern.compile("Congratulations, you've (just advanced your (?<skill>[a-zA-Z]+) level\\. You are now level (?<level>\\d+)|reached the highest possible (?<skill99>[a-zA-Z]+) level of 99)\\.");
    public static final String SOUNDS_PATH = "sounds/";

    //true when a level up widget shows up, otherwise false
    public boolean widgetLevelUp;

    private MP3Player player;

    public JingleManager(){
        widgetLevelUp = false;
    }

    public void tickJingle(){
        if (!widgetLevelUp || isLevelUpWidgetDisabled()) return;

        //widgets enabled + detected level up
        //Also from Screenshot Plugin
        Widget levelUpWidget = null;
        if (client.getWidget(InterfaceID.LevelupDisplay.TEXT2) != null) {
            levelUpWidget = client.getWidget(InterfaceID.LevelupDisplay.TEXT2);
        } else if (client.getWidget(InterfaceID.Objectbox.TEXT) != null &&
                !Text.removeTags(client.getWidget(InterfaceID.Objectbox.TEXT).getText()).contains("High level gamble")) {
            //probably the combat level widget? idk
            levelUpWidget = client.getWidget(InterfaceID.Objectbox.TEXT);
        }

        if (levelUpWidget != null) {
            queueLevelJingle(levelUpWidget.getText());
        }
        widgetLevelUp = false;

    }

    //true if levelup widget is disabled, otherwise false
    public boolean isLevelUpWidgetDisabled(){
        return client.getVarbitValue(VarbitID.OPTION_LEVEL_UP_MESSAGE) == 1;
    }

    //takes a chat/widget text, parses with regex, then tries to play a jingle
    public void queueLevelJingle(String message){
        Pattern regex = (isLevelUpWidgetDisabled() ? JingleManager.LEVEL_UP_MESSAGE_PATTERN : JingleManager.LEVEL_UP_PATTERN);
        Matcher m = regex.matcher(message);
        if (!m.matches()) return;

        String skill = (isLevelUpWidgetDisabled() ? m.group("skill") : m.group(1)).toUpperCase();
        String level = isLevelUpWidgetDisabled() ? m.group("level") : m.group(2);
        log.debug("Skill " + skill + " Level " + level);

        try {
            String fileName;
            if (skill.equals("COMBAT")){
                fileName = "combat";
            }else {
                Skill s = Skill.valueOf(skill);
                int l = Integer.parseInt(level);
                log.debug("Skill level parse successful");
                fileName = skill.toLowerCase() + (JingleData.UNLOCK_LEVELS.get(s).contains(l) ? "_unlocks" : "");
                log.debug("file name " + fileName);
            }
            playJingle(fileName);
        }catch (Exception ex){
            log.debug("Failed to play level jingle");
        }
    }

    public void playJingle(String fileName){
        File mp3file = new File(SOUNDS_PATH + fileName + ".mp3");
        if (player != null) player.stop();
        player = new MP3Player(mp3file);
        player.setRepeat(false);
        player.setVolume(config.jingleGain());
        player.play();
    }
}
