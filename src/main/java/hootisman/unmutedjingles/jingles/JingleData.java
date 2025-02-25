package hootisman.unmutedjingles.jingles;

import net.runelite.api.Skill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JingleData {
    // ***** Constants *****

    static final Set<Integer> ATTACK_UNLOCKS =
            Stream.of(5, 10, 15, 20, 30, 40, 42, 50, 55,
                    60, 65, 70, 75, 77, 78, 80, 82, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> COOKING_UNLOCKS =
            Stream.of(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                    20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
                    37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52,
                    53, 54, 55, 57, 58, 59, 60, 62, 64, 65, 67, 68, 70, 72, 73, 75,
                    80, 82, 84, 85, 88, 90, 91, 92, 95, 96, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> CRAFTING_UNLOCKS =
            Stream.of(3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
                    18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33,
                    34, 35, 36, 37, 38, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
                    51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66,
                    67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 82, 83,
                    84, 85, 86, 87, 88, 89, 90, 92, 95, 98, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> DEFENCE_UNLOCKS =
            Stream.of(5, 10, 20, 25, 30, 35, 40, 42, 45, 50,
                    55, 60, 65, 70, 75, 78, 80, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> FARMING_UNLOCKS =
            Stream.of(2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
                    19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34,
                    35, 36, 38, 39, 40, 42, 44, 45, 46, 47, 48, 49, 50, 51, 53, 54,
                    55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 67, 68, 69, 70, 72,
                    73, 74, 75, 76, 79, 81, 83, 84, 85, 90, 91, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> FIREMAKING_UNLOCKS =
            Stream.of(4, 5, 11, 12, 15, 16, 20, 21, 25, 26, 30, 33, 35, 40,
                    42, 43, 45, 47, 49, 50, 52, 55, 60, 62, 65, 66, 70, 75, 80, 85, 90, 95, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> FISHING_UNLOCKS =
            Stream.of(5, 7, 10, 15, 16, 20, 23, 25, 28, 29, 30, 33, 34, 35,
                    38, 39, 40, 43, 45, 46, 47, 48, 50, 53, 55, 56, 58, 60, 61, 62,
                    65, 68, 70, 71, 73, 75, 76, 79, 80, 81, 82, 85, 87, 90, 91, 96, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> FLETCHING_UNLOCKS =
            Stream.of(3, 5, 7, 9, 10, 11, 15, 17, 18, 20, 22, 24, 25, 26,
                    27, 30, 32, 33, 35, 37, 38, 39, 40, 41, 42, 43, 45, 46, 47, 48,
                    49, 50, 51, 52, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 65, 67,
                    69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 80, 81, 82, 84, 85, 87,
                    90, 92, 95, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> HERBLORE_UNLOCKS =
            Stream.of(3, 4, 5, 6, 8, 9, 10, 11, 12, 14, 15, 18, 19, 20, 22,
                    24, 25, 26, 29, 30, 31, 33, 34, 36, 37, 38, 39, 40, 42, 44, 45,
                    47, 48, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
                    65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
                    81, 82, 83, 84, 85, 86, 87, 88, 90, 91, 92, 94, 97, 98, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> MAGIC_UNLOCKS =
            Stream.of(3, 4, 5, 6, 7, 9, 11, 13, 14, 15, 16, 17, 19, 20, 21,
                    23, 24, 25, 27, 28, 29, 30, 31, 33, 34, 35, 37, 38, 39, 40, 41,
                    42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
                    58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73,
                    74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89,
                    90, 91, 92, 93, 94, 95, 96, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> MINING_UNLOCKS =
            Stream.of(5, 6, 10, 11, 14, 15, 17, 20, 21, 22, 25, 30, 31, 35,
                    37, 38, 39, 40, 41, 42, 43, 45, 50, 52, 55, 57, 60, 61, 64, 65,
                    67, 68, 70, 71, 72, 75, 78, 80, 81, 82, 85, 90, 92, 97, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> PRAYER_UNLOCKS =
            Stream.of(3, 4, 7, 8, 9, 10, 12, 13, 16, 19, 20, 22, 24, 25, 26,
                    27, 28, 30, 31, 32, 34, 36, 37, 40, 42, 43, 44, 45, 46, 47, 48,
                    49, 50, 52, 54, 55, 56, 60, 62, 63, 64, 65, 68, 70, 72, 74, 75,
                    76, 77, 80, 84, 85, 88, 90, 92, 96, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> RANGED_UNLOCKS =
            Stream.of(5, 10, 16, 19, 20, 21, 25, 26, 28, 30, 31, 35, 36, 37,
                    39, 40, 42, 45, 46, 47, 50, 55, 60, 61, 62, 64, 65, 66, 70, 75, 77, 80, 85, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> RUNECRAFT_UNLOCKS =
            Stream.of(2, 5, 6, 9, 10, 11, 13, 14, 15, 19, 20, 22, 23, 25,
                    26, 27, 28, 30, 33, 35, 38, 40, 42, 44, 46, 48, 49, 50, 52, 54,
                    55, 56, 57, 59, 60, 65, 66, 70, 74, 75, 76, 77, 78, 79, 81, 82,
                    84, 85, 88, 90, 91, 92, 95, 98, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> SLAYER_UNLOCKS =
            Stream.of(5, 7, 10, 15, 17, 18, 20, 22, 25, 30, 32, 33, 35, 37,
                    38, 39, 40, 42, 44, 45, 47, 48, 50, 52, 55, 56, 57, 58, 60, 62,
                    63, 65, 66, 68, 69, 70, 72, 75, 77, 80, 82, 83, 84, 85, 87, 90,
                    91, 92, 93, 95, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> THIEVING_UNLOCKS =
            Stream.of(2, 5, 10, 13, 14, 15, 16, 17, 20, 21, 22, 23, 25, 27,
                    28, 30, 31, 32, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45,
                    46, 47, 48, 49, 50, 51, 52, 53, 55, 56, 57, 58, 59, 60, 61, 62,
                    64, 65, 66, 70, 71, 72, 75, 78, 80, 81, 82, 84, 85, 90, 91, 93, 94, 95, 99).collect(Collectors.toUnmodifiableSet());
    static final Set<Integer> WOODCUTTING_UNLOCKS =
            Stream.of(6, 10, 11, 12, 15, 19, 20, 21, 24, 27, 29, 30, 31, 35,
                    36, 40, 41, 42, 44, 45, 48, 50, 54, 55, 56, 57, 60, 61, 62, 65,
                    68, 70, 71, 72, 75, 84, 90, 96, 99).collect(Collectors.toUnmodifiableSet());




    // ***** Mutable *****
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

    //todo: test all durations
    //(duration in seconds)/0.6
    public static Map<Skill, Function<Integer, Integer>> JINGLE_DURATIONS = Map.ofEntries(
            Map.entry(Skill.AGILITY, level -> 10),
            Map.entry(Skill.ATTACK, getRegularUnlocksFunction(6,13,ATTACK_UNLOCKS)),
            Map.entry(Skill.CONSTRUCTION, level -> 10),
            Map.entry(Skill.COOKING, getRegularUnlocksFunction(7,6,COOKING_UNLOCKS)),
            Map.entry(Skill.CRAFTING, getRegularUnlocksFunction(16,10,CRAFTING_UNLOCKS)),
            Map.entry(Skill.DEFENCE, getRegularUnlocksFunction(9,12,DEFENCE_UNLOCKS)),
            Map.entry(Skill.FARMING, getRegularUnlocksFunction(7,12,FARMING_UNLOCKS)),
            Map.entry(Skill.FIREMAKING, getRegularUnlocksFunction(10,10,FIREMAKING_UNLOCKS)),
            Map.entry(Skill.FISHING, getRegularUnlocksFunction(11,9,FISHING_UNLOCKS)),
            Map.entry(Skill.FLETCHING, getRegularUnlocksFunction(9,8,FLETCHING_UNLOCKS)),
            Map.entry(Skill.HERBLORE, getRegularUnlocksFunction(5,10,HERBLORE_UNLOCKS)),
            Map.entry(Skill.HITPOINTS, level -> level < 50 ? 5 : 8),
            Map.entry(Skill.HUNTER, level -> 12),
            Map.entry(Skill.MAGIC, getRegularUnlocksFunction(11,11,MAGIC_UNLOCKS)),
            Map.entry(Skill.MINING, getRegularUnlocksFunction(10,10,MINING_UNLOCKS)),
            Map.entry(Skill.PRAYER, getRegularUnlocksFunction(12,12,PRAYER_UNLOCKS)),
            Map.entry(Skill.RANGED, getRegularUnlocksFunction(8,9,RANGED_UNLOCKS)),
            Map.entry(Skill.RUNECRAFT, getRegularUnlocksFunction(13,13,RUNECRAFT_UNLOCKS)),
            Map.entry(Skill.SLAYER, getRegularUnlocksFunction(10,15,SLAYER_UNLOCKS)),
            Map.entry(Skill.SMITHING, level -> 9),     //every level is an unlock
            Map.entry(Skill.STRENGTH, level -> level < 50 ? 10 : 8),
            Map.entry(Skill.THIEVING, getRegularUnlocksFunction(11,8,THIEVING_UNLOCKS)),
            Map.entry(Skill.WOODCUTTING, getRegularUnlocksFunction(10,11,WOODCUTTING_UNLOCKS))
    );

    //true if level was set at game start
    public static boolean isLevelInited(Skill skill){
        return SKILL_LEVELS.get(skill) != -1;
    }

    static Function<Integer, Integer> getRegularUnlocksFunction(int defaultDuration, int unlockDuration, Set<Integer> levelsWithUnlocks){
        return level -> levelsWithUnlocks.contains(level) ? defaultDuration : unlockDuration;
    }
}
