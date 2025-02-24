package hootisman.musiclessjingles;

import net.runelite.api.Skill;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class PlayerStats {
    public static Map<Skill, Integer> SKILL_LEVELS = new HashMap<>() {{
        put(Skill.AGILITY, -1);
        put(Skill.ATTACK, -1);
        put(Skill.CONSTRUCTION, -1);
        put(Skill.COOKING, -1);
        put(Skill.CRAFTING, -1);
        put(Skill.DEFENCE, -1);
        put(Skill.FARMING, -1);
        put(Skill.FIREMAKING, -1);
        put(Skill.FISHING, -1);
        put(Skill.FLETCHING, -1);
        put(Skill.HERBLORE, -1);
        put(Skill.HITPOINTS, -1);
        put(Skill.HUNTER, -1);
        put(Skill.MAGIC, -1);
        put(Skill.MINING, -1);
        put(Skill.PRAYER, -1);
        put(Skill.RANGED, -1);
        put(Skill.RUNECRAFT, -1);
        put(Skill.SLAYER, -1);
        put(Skill.SMITHING, -1);
        put(Skill.STRENGTH, -1);
        put(Skill.THIEVING, -1);
        put(Skill.WOODCUTTING, -1);
    }};
}
