package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.ActionAnimation;
import maninthehouse.epicfight.animation.types.StaticAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NPCAttackPatterns {
    public static Map<Integer, Integer> COMPARISON= new HashMap<Integer, Integer>();
    public static Map<List<String>, Integer> WEAPON_INDEX= new HashMap<List<String>, Integer>();
    public static Map<Integer, List<ActionAnimation>> ATTACK_PATTERN= new HashMap<Integer, List<ActionAnimation>>();
    public static Map<Integer, Map<LivingMotion, StaticAnimation>> MOTION= new HashMap<Integer, Map<LivingMotion, StaticAnimation>>();

    public static void fillMaps() {
        //WEAPON_INDEX.put(new List("ancientwarfarenpc:wooden_halberd"));
    }
}
