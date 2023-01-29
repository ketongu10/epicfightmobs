package maninthehouse.epicfight.gamedata;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import maninthehouse.epicfight.animation.types.AnimationProperty;
import maninthehouse.epicfight.main.EpicFightMod;
import maninthehouse.epicfight.skill.*;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource.StunType;
import net.minecraft.util.ResourceLocation;

public class Skills {
	public static final Map<ResourceLocation, Skill> MODIFIABLE_SKILLS = new HashMap<ResourceLocation, Skill> ();
	public static Skill ROLL;
	public static Skill STEP_BACK;
	public static Skill GUILLOTINE_AXE;
	public static Skill SWEEPING_EDGE;
	public static Skill DANCING_EDGE;
	public static Skill SLAUGHTER_STANCE;
	public static Skill HEARTPIERCER;
	public static Skill GIANT_WHIRLWIND;
	public static Skill FATAL_DRAW;
	public static Skill KATANA_GIMMICK;
	public static Skill LIECHTENHAUER;
	public static Skill GUARD;
	public static Skill LETHAL_SLICING;
	public static Skill RELENTLESS_COMBO;
	public static Skill EVISCERATE;


	/**restriction - это дамаг, который надо насобирать!!!!!!**/
	/**не используется после версии 1.7**/
	public static void init() {
		ROLL = makeSkill("roll", (skillName) ->
				new DodgeSkill(SkillSlot.DODGE, 2.0F, 0,skillName, 30.0F ,Animations.BIPED_ROLL_FORWARD, Animations.BIPED_ROLL_BACKWARD), false);

		STEP_BACK = makeSkill("step_back", (skillName) ->
				new StepBackSkill(SkillSlot.STEP_BACK, 2.0F, 0,skillName, 20.0F ,Animations.BIPED_ROLL_FORWARD, Animations.BIPED_STEP_BACKWARD), false);
		
		SWEEPING_EDGE = makeSkill("sweeping_edge", (skillName) ->
			new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 30.0F, skillName, 40.0F, Animations.SWEEPING_EDGE)
				.addProperty(AnimationProperty.HIT_AT_ONCE, 3)
				.addProperty(AnimationProperty.DAMAGE_MULTIPLIER, 2.0F)
				.addProperty(AnimationProperty.ARMOR_NEGATION, 20.0F)
				.addProperty(AnimationProperty.STUN_TYPE, StunType.LONG).registerPropertiesToAnimation(), true);
		
		DANCING_EDGE = makeSkill("dancing_edge", (skillName) ->
			new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 30.0F, skillName, 60.0F, Animations.DANCING_EDGE)
				.addProperty(AnimationProperty.HIT_AT_ONCE, 3)
				.addProperty(AnimationProperty.IMPACT, 1.5F).registerPropertiesToAnimation(), false);
		
		GUILLOTINE_AXE = makeSkill("guillotine_axe", (skillName) ->
			new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 20.0F, skillName, 40.0F, Animations.GUILLOTINE_AXE)
				.addProperty(AnimationProperty.HIT_AT_ONCE, 1)
				.addProperty(AnimationProperty.DAMAGE_MULTIPLIER, 2.5F)
				.addProperty(AnimationProperty.ARMOR_NEGATION, 40.0F)
				.addProperty(AnimationProperty.STUN_TYPE, StunType.LONG).registerPropertiesToAnimation(), false);
		
		SLAUGHTER_STANCE = makeSkill("slaughter_stance", (skillName) ->
			new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 40.0F, skillName, 40.0F, Animations.SPEAR_SLASH)
				.addProperty(AnimationProperty.HIT_AT_ONCE, 8)
				.addProperty(AnimationProperty.DAMAGE_MULTIPLIER, 1.25F)
				.addProperty(AnimationProperty.IMPACT, 2.0F).registerPropertiesToAnimation(), false);
		
		HEARTPIERCER = makeSkill("heartpiercer", (skillName) ->
			new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 40.0F, skillName, 40.0F, Animations.SPEAR_THRUST)
				.addProperty(AnimationProperty.ARMOR_NEGATION, 20.0F)
				.addProperty(AnimationProperty.STUN_TYPE, StunType.HOLD).registerPropertiesToAnimation(), false);
		
		GIANT_WHIRLWIND = makeSkill("giant_whirlwind", (skillName) ->
			new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 60.0F, skillName, 60.0F, Animations.GIANT_WHIRLWIND), false);
		
		FATAL_DRAW = makeSkill("fatal_draw", (skillName) ->
			new FatalDrawSkill(skillName)
				.addProperty(AnimationProperty.DAMAGE_MULTIPLIER, 2.0F)
				.addProperty(AnimationProperty.ARMOR_NEGATION, 50.0F)
				.addProperty(AnimationProperty.HIT_AT_ONCE, 8)
				.addProperty(AnimationProperty.STUN_TYPE, StunType.HOLD).registerPropertiesToAnimation(), false);
 		KATANA_GIMMICK = new KatanaPassive();
		GUARD = new GuardSkill("guard");

		LETHAL_SLICING = makeSkill("lethal_slicing", (skillName) ->
				new LethalSlicingSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 30.0F,  skillName,40.0F)
						.addProperty(AnimationProperty.HIT_AT_ONCE, 2)
						.addProperty(AnimationProperty.STUN_TYPE, StunType.LONG).registerPropertiesToAnimation(), false);
		RELENTLESS_COMBO = makeSkill("relentless_combo", (skillname) ->
				new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 30.0F, skillname, 30.0F, Animations.RELENTLESS_COMBO)
						.addProperty(AnimationProperty.HIT_AT_ONCE, 1)
						.addProperty(AnimationProperty.STUN_TYPE, StunType.HOLD).registerPropertiesToAnimation(), false);

		EVISCERATE = makeSkill("eviscerate", (skillname) ->
						new EviscerateSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 30.0F, skillname, 50.0F)
								.addProperty(AnimationProperty.HIT_AT_ONCE, 1)
								.addProperty(AnimationProperty.IMPACT, 2.0F)
								.registerPropertiesToAnimation(), false);

	}
	
	public static Skill makeSkill(String skillName, Function<String, Skill> object, boolean registerSkillBook) {
		if (registerSkillBook) {
			MODIFIABLE_SKILLS.put(new ResourceLocation(EpicFightMod.MODID, skillName), object.apply(skillName));
		}
		
		return object.apply(skillName);
	}
}