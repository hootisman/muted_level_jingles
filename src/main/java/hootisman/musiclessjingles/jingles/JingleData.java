package hootisman.musiclessjingles.jingles;

import net.runelite.api.Skill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

public class JingleData {
    public static Map<Skill, Integer> SKILL_LEVELS = new HashMap<>();
    static {
        SKILL_LEVELS.put(Skill.AGILITY, -1);
        SKILL_LEVELS.put(Skill.ATTACK, -1);
        SKILL_LEVELS.put(Skill.CONSTRUCTION, -1);
        SKILL_LEVELS.put(Skill.COOKING, -1);
        SKILL_LEVELS.put(Skill.CRAFTING, -1);
        SKILL_LEVELS.put(Skill.DEFENCE, -1);
        SKILL_LEVELS.put(Skill.FARMING, -1);
        SKILL_LEVELS.put(Skill.FIREMAKING, -1);
        SKILL_LEVELS.put(Skill.FISHING, -1);
        SKILL_LEVELS.put(Skill.FLETCHING, -1);
        SKILL_LEVELS.put(Skill.HERBLORE, -1);
        SKILL_LEVELS.put(Skill.HITPOINTS, -1);
        SKILL_LEVELS.put(Skill.HUNTER, -1);
        SKILL_LEVELS.put(Skill.MAGIC, -1);
        SKILL_LEVELS.put(Skill.MINING, -1);
        SKILL_LEVELS.put(Skill.PRAYER, -1);
        SKILL_LEVELS.put(Skill.RANGED, -1);
        SKILL_LEVELS.put(Skill.RUNECRAFT, -1);
        SKILL_LEVELS.put(Skill.SLAYER, -1);
        SKILL_LEVELS.put(Skill.SMITHING, -1);
        SKILL_LEVELS.put(Skill.STRENGTH, -1);
        SKILL_LEVELS.put(Skill.THIEVING, -1);
        SKILL_LEVELS.put(Skill.WOODCUTTING, -1);
    };


    //returns # of game ticks to play jingle for;   duration(in seconds)/0.6 + 1
    public static Map<Skill, Function<Integer, Integer>> JINGLE_DURATIONS = Map.ofEntries(
            Map.entry(Skill.AGILITY, level -> 11)
/*

            Map.entry(Skill.ATTACK, ),
            Map.entry(Skill.CONSTRUCTION, -1),
            Map.entry(Skill.COOKING, -1),
            Map.entry(Skill.CRAFTING, -1),
            Map.entry(Skill.DEFENCE, -1),
            Map.entry(Skill.FARMING, -1),
            Map.entry(Skill.FIREMAKING, -1),
            Map.entry(Skill.FISHING, -1),
            Map.entry(Skill.FLETCHING, -1),
            Map.entry(Skill.HERBLORE, -1),
            Map.entry(Skill.HITPOINTS, -1),
            Map.entry(Skill.HUNTER, -1),
            Map.entry(Skill.MAGIC, -1),
            Map.entry(Skill.MINING, -1),
            Map.entry(Skill.PRAYER, -1),
            Map.entry(Skill.RANGED, -1),
            Map.entry(Skill.RUNECRAFT, -1),
            Map.entry(Skill.SLAYER, -1),
            Map.entry(Skill.SMITHING, -1),
            Map.entry(Skill.STRENGTH, -1),
            Map.entry(Skill.THIEVING, -1),
            Map.entry(Skill.WOODCUTTING, -1)
*/

    );

    Function<Integer, Integer> getRegularUnlocksFunction(int defaultDuration, int unlockDuration, HashSet<Integer> levelsWithUnlocks){
        return level -> levelsWithUnlocks.contains(level) ? defaultDuration : unlockDuration;
    }
}
