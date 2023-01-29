package maninthehouse.epicfight.capabilities.entity.mob;

import java.util.ArrayList;
import java.util.List;

import maninthehouse.epicfight.animation.types.ActionAnimation;
import maninthehouse.epicfight.animation.types.AnimationProperty;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.animation.types.attack.AttackAnimation;
import maninthehouse.epicfight.gamedata.Animations;

public class MobAttackPatterns {
	public static List<AttackAnimation> BIPED_ARMED_ONEHAND = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_ARMED_SPEAR = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_ARMED_KATANA = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_ARMED_AXE = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_ARMED_AXE2 = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_ARMED_SPEAR2 = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_ARMED_SWORD = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_ARMED_SWORD2 = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_MOUNT_SWORD = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_MOUNT_SPEAR = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> BIPED_UNARMED = new ArrayList<AttackAnimation> ();

	public static List<AttackAnimation> VINDICATOR_PATTERN = new ArrayList<AttackAnimation> ();

	public static List<AttackAnimation> ENDERMAN_PATTERN1 = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> GIANT_PATTERN = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> HUGE_ARMED = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> HUGE_ARMED2 = new ArrayList<AttackAnimation> ();
	static List<AttackAnimation> ENDERMAN_PATTERN2 = new ArrayList<AttackAnimation> ();
	static List<AttackAnimation> ENDERMAN_PATTERN3 = new ArrayList<AttackAnimation> ();
	static List<AttackAnimation> ENDERMAN_PATTERN4 = new ArrayList<AttackAnimation> ();

	static List<AttackAnimation> GOLEM_PATTERN1 = new ArrayList<AttackAnimation> ();
	static List<AttackAnimation> GOLEM_PATTERN2 = new ArrayList<AttackAnimation> ();
	static List<AttackAnimation> GOLEM_PATTERN3 = new ArrayList<AttackAnimation> ();
	static List<AttackAnimation> SPIDER_PATTERN = new ArrayList<AttackAnimation> ();
	static List<AttackAnimation> SPIDER_JUMP_PATTERN = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> WITHER_SKELETON_PATTERN = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> ZOMBIE_NORAML = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> HUNTER_SWORD = new ArrayList<AttackAnimation> ();
	public static List<AttackAnimation> HUNTER_AXE = new ArrayList<AttackAnimation> ();
	public static List<ActionAnimation> PARRY_SPECIAL_ATTACK1 = new ArrayList<ActionAnimation> ();
	public static List<ActionAnimation> PARRY_SPECIAL_ATTACK2 = new ArrayList<ActionAnimation> ();
	public static List<ActionAnimation> PARRY_SPECIAL_ATTACK3 = new ArrayList<ActionAnimation> ();

	public static List<ActionAnimation> ORC_SPECIAL_ATTACK1 = new ArrayList<ActionAnimation> ();
	public static List<ActionAnimation> ORC_SPECIAL_ATTACK2 = new ArrayList<ActionAnimation> ();
	public static List<ActionAnimation> ORC_SPECIAL_ATTACK3 = new ArrayList<ActionAnimation> ();
	public static List<AttackAnimation> ORC_FLAG = new ArrayList<AttackAnimation> ();

	public static List<AttackAnimation> BIPED_CLAWED = new ArrayList<AttackAnimation> ();

	
	public static void setVariousMobAttackPatterns() {

		/**FOR HALBERDS**/
		BIPED_ARMED_SPEAR2.add((AttackAnimation) Animations.SPEAR_TWOHAND_AUTO_1);
		BIPED_ARMED_SPEAR2.add((AttackAnimation) Animations.SPEAR_TWOHAND_AUTO_2);
		BIPED_ARMED_SPEAR2.add((AttackAnimation) Animations.GREATSWORD_DASH);
		/**AXE**/
		BIPED_ARMED_AXE.add((AttackAnimation) Animations.AXE_AUTO1);
		BIPED_ARMED_AXE.add((AttackAnimation) Animations.AXE_AUTO2);
		//BIPED_ARMED_AXE.add((AttackAnimation) Animations.SWEEPING_EDGE);
		/**HUNTER**/
		HUNTER_SWORD.add((AttackAnimation) Animations.LONGSWORD_AUTO_1);
		HUNTER_SWORD.add((AttackAnimation) Animations.LONGSWORD_AUTO_2);
		HUNTER_SWORD.add((AttackAnimation) Animations.LONGSWORD_AUTO_3);
		HUNTER_SWORD.add((AttackAnimation) Animations.DAGGER_AUTO_3);
		HUNTER_AXE.add((AttackAnimation) Animations.AXE_AUTO1);
		HUNTER_AXE.add((AttackAnimation) Animations.AXE_AUTO2);
		//HUNTER_AXE.add((AttackAnimation) Animations.AXE_AIRSLASH);
		/**FOR SKILLFUL GUYS**/
		PARRY_SPECIAL_ATTACK1.add((ActionAnimation) Animations.BIPED_STEP_BACKWARD);
		PARRY_SPECIAL_ATTACK1.add((ActionAnimation) Animations.SWORD_DASH);

		PARRY_SPECIAL_ATTACK2.add((ActionAnimation) Animations.LETHAL_SLICING);
		PARRY_SPECIAL_ATTACK2.add((ActionAnimation) Animations.LETHAL_SLICING_ONCE);
		PARRY_SPECIAL_ATTACK2.add((ActionAnimation) Animations.SWEEPING_EDGE);

		PARRY_SPECIAL_ATTACK3.add((ActionAnimation) Animations.BIPED_STEP_BACKWARD);
		PARRY_SPECIAL_ATTACK3.add((ActionAnimation)  Animations.TACHI_DASH);

		/**FOR GIANTS**/
		ORC_SPECIAL_ATTACK1.add((ActionAnimation) Animations.HUGE_ZOMBIE_ATTACK3);
		ORC_SPECIAL_ATTACK1.add((ActionAnimation) Animations.HUGE_GREATSWORD_AUTO_1);
		ORC_SPECIAL_ATTACK1.add((ActionAnimation) Animations.HUGE_AXE_AUTO2);

		ORC_SPECIAL_ATTACK2.add((ActionAnimation) Animations.HUGE_KNEE);
		ORC_SPECIAL_ATTACK2.add((ActionAnimation) Animations.HUGE_FIST_AUTO2);
		ORC_SPECIAL_ATTACK2.add((ActionAnimation) Animations.HUGE_GREATSWORD_AUTO_1);

		ORC_SPECIAL_ATTACK3.add((ActionAnimation) Animations.LETHAL_SLICING);
		ORC_SPECIAL_ATTACK3.add((ActionAnimation) Animations.HUGE_LETHAL_SLICING_ONCE);

		GIANT_PATTERN.add((AttackAnimation) Animations.HUGE_KNEE);
		GIANT_PATTERN.add((AttackAnimation) Animations.ENDERMAN_GRASP);
		GIANT_PATTERN.add((AttackAnimation) Animations.ZOMBIE_ATTACK3);

		HUGE_ARMED.add((AttackAnimation) Animations.HUGE_AXE_AUTO1);
		HUGE_ARMED.add((AttackAnimation) Animations.HUGE_GREATSWORD_AUTO_2);
		HUGE_ARMED.add((AttackAnimation) Animations.HUGE_GREATSWORD_AUTO_1);
		HUGE_ARMED.add((AttackAnimation) Animations.HUGE_AXE_AUTO2);

		HUGE_ARMED2.add((AttackAnimation) Animations.HUGE_AXE_AUTO1);
		HUGE_ARMED2.add((AttackAnimation) Animations.HUGE_SWORD_DUAL_AUTO_2);
		HUGE_ARMED2.add((AttackAnimation) Animations.HUGE_AXE_AUTO2);
		HUGE_ARMED2.add((AttackAnimation) Animations.HUGE_ZOMBIE_ATTACK3);


		/**FOR ORCS**/
		ORC_FLAG.add((AttackAnimation) Animations.LONGSWORD_AIR_SLASH);
		ORC_FLAG.add((AttackAnimation) Animations.GREATSWORD_AUTO_1);
		ORC_FLAG.add((AttackAnimation) Animations.GREATSWORD_AUTO_2);

		BIPED_ARMED_AXE2.add((AttackAnimation) Animations.AXE_AUTO1);
		BIPED_ARMED_AXE2.add((AttackAnimation) Animations.SWORD_DUAL_AUTO_2);
		BIPED_ARMED_AXE2.add((AttackAnimation) Animations.AXE_AUTO2);
		BIPED_ARMED_AXE2.add((AttackAnimation) Animations.ZOMBIE_ATTACK3);





		BIPED_UNARMED.add((AttackAnimation) Animations.FIST_AUTO_1);
		BIPED_UNARMED.add((AttackAnimation) Animations.FIST_AUTO_2);
		BIPED_UNARMED.add((AttackAnimation) Animations.FIST_AUTO_3);

		/**FOR VAMPIRES**/
		BIPED_CLAWED.add((AttackAnimation) Animations.DAGGER_DUAL_AUTO_2);
		BIPED_CLAWED.add((AttackAnimation) Animations.DAGGER_DUAL_AUTO_3);


		BIPED_ARMED_SWORD2.add((AttackAnimation) Animations.SWORD_DUAL_AUTO_1);
		BIPED_ARMED_SWORD2.add((AttackAnimation) Animations.SWORD_DUAL_AUTO_2);
		BIPED_ARMED_SWORD2.add((AttackAnimation) Animations.SWORD_DUAL_AUTO_3);
		BIPED_ARMED_SWORD2.add((AttackAnimation) Animations.SWORD_DUAL_DASH);

		BIPED_ARMED_KATANA.add((AttackAnimation) Animations.KATANA_AUTO_1);
		BIPED_ARMED_KATANA.add((AttackAnimation) Animations.KATANA_AUTO_2);
		BIPED_ARMED_KATANA.add((AttackAnimation) Animations.KATANA_AUTO_3);

		BIPED_ARMED_ONEHAND.add((AttackAnimation) Animations.BIPED_ARMED_MOB_ATTACK1);
		BIPED_ARMED_ONEHAND.add((AttackAnimation) Animations.BIPED_ARMED_MOB_ATTACK2);
		BIPED_ARMED_SWORD.add((AttackAnimation) Animations.SWORD_AUTO_3);
		BIPED_ARMED_SWORD.add((AttackAnimation) Animations.SWORD_AUTO_1);
		BIPED_ARMED_SWORD.add((AttackAnimation) Animations.SWORD_AUTO_2);
		BIPED_ARMED_SWORD.add((AttackAnimation) Animations.SWORD_DASH);
		BIPED_ARMED_SPEAR.add((AttackAnimation) Animations.SPEAR_ONEHAND_AUTO);
		BIPED_ARMED_SPEAR.add((AttackAnimation) Animations.SPEAR_DASH);
		BIPED_MOUNT_SWORD.add((AttackAnimation) Animations.SWORD_MOUNT_ATTACK);
		BIPED_MOUNT_SPEAR.add((AttackAnimation) Animations.SPEAR_MOUNT_ATTACK);

		ENDERMAN_PATTERN1.add((AttackAnimation) Animations.ENDERMAN_KNEE);
		ENDERMAN_PATTERN2.add((AttackAnimation) Animations.ENDERMAN_KICK_COMBO);
		ENDERMAN_PATTERN3.add((AttackAnimation) Animations.ENDERMAN_KICK1);
		ENDERMAN_PATTERN4.add((AttackAnimation) Animations.ENDERMAN_KICK2);
		GOLEM_PATTERN1.add((AttackAnimation)Animations.GOLEM_ATTACK1);
		GOLEM_PATTERN2.add((AttackAnimation)Animations.GOLEM_ATTACK2);
		GOLEM_PATTERN3.add((AttackAnimation)Animations.GOLEM_ATTACK3);
		GOLEM_PATTERN3.add((AttackAnimation)Animations.GOLEM_ATTACK4);
		SPIDER_PATTERN.add((AttackAnimation) Animations.SPIDER_ATTACK);
		SPIDER_JUMP_PATTERN.add((AttackAnimation) Animations.SPIDER_JUMP_ATTACK);
		VINDICATOR_PATTERN.add((AttackAnimation) Animations.VINDICATOR_SWING_AXE1);
		VINDICATOR_PATTERN.add((AttackAnimation) Animations.VINDICATOR_SWING_AXE2);
		VINDICATOR_PATTERN.add((AttackAnimation) Animations.VINDICATOR_SWING_AXE3);
		WITHER_SKELETON_PATTERN.add((AttackAnimation) Animations.WITHER_SKELETON_ATTACK1);
		WITHER_SKELETON_PATTERN.add((AttackAnimation) Animations.WITHER_SKELETON_ATTACK2);
		WITHER_SKELETON_PATTERN.add((AttackAnimation) Animations.WITHER_SKELETON_ATTACK3);
		ZOMBIE_NORAML.add((AttackAnimation) Animations.ZOMBIE_ATTACK1);
		ZOMBIE_NORAML.add((AttackAnimation) Animations.ZOMBIE_ATTACK2);
		ZOMBIE_NORAML.add((AttackAnimation) Animations.ZOMBIE_ATTACK3);
	}
}