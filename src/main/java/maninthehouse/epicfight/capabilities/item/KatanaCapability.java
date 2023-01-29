package maninthehouse.epicfight.capabilities.item;

import java.util.HashMap;
import java.util.Map;

import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.player.PlayerData;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Colliders;
import maninthehouse.epicfight.gamedata.Skills;
import maninthehouse.epicfight.gamedata.Sounds;
import maninthehouse.epicfight.skill.KatanaPassive;
import maninthehouse.epicfight.skill.SkillDataManager;
import maninthehouse.epicfight.skill.SkillSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static maninthehouse.epicfight.skill.KatanaPassive.SHEATH;

public class KatanaCapability extends ModWeaponCapability {
	private Map<LivingMotion, StaticAnimation> sheathedMotions;
	
	public KatanaCapability() {
		super(WeaponCategory.KATANA, (playerdata)->WieldStyle.TWO_HAND, Skills.KATANA_GIMMICK, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.katana, HandProperty.TWO_HANDED);
		this.addStyleAttributeSimple(WieldStyle.TWO_HAND, 0.0D, 0.6D, 1);
		this.addStyleCombo(WieldStyle.SHEATH, Animations.KATANA_SHEATHING_AUTO, Animations.KATANA_AIR_SLASH, Animations.KATANA_SHEATHING_DASH);
		this.addStyleCombo(WieldStyle.TWO_HAND, Animations.KATANA_AUTO_1, Animations.KATANA_AUTO_2, Animations.KATANA_AUTO_3, Animations.KATANA_AIR_SLASH, Animations.SWORD_DASH);
		this.addStyleCombo(WieldStyle.MOUNT, Animations.SWORD_MOUNT_ATTACK);
		this.addStyleSpecialAttack(WieldStyle.SHEATH, Skills.FATAL_DRAW);
		this.addStyleSpecialAttack(WieldStyle.TWO_HAND, Skills.FATAL_DRAW);
    	this.addLivingMotionChanger(LivingMotion.IDLE, Animations.BIPED_IDLE_UNSHEATHING);
    	this.addLivingMotionChanger(LivingMotion.WALKING, Animations.BIPED_WALK_UNSHEATHING);
    	this.addLivingMotionChanger(LivingMotion.RUNNING, Animations.BIPED_RUN_UNSHEATHING);
		this.addLivingMotionChanger(LivingMotion.BLOCKING, Animations.KATANA_GUARD);
    	this.sheathedMotions = new HashMap<LivingMotion, StaticAnimation> ();
    	this.sheathedMotions.put(LivingMotion.IDLE, Animations.BIPED_IDLE_SHEATHING);
    	this.sheathedMotions.put(LivingMotion.WALKING, Animations.BIPED_WALK_SHEATHING);
    	this.sheathedMotions.put(LivingMotion.RUNNING, Animations.BIPED_RUN_SHEATHING);
    	this.sheathedMotions.put(LivingMotion.JUMPING, Animations.BIPED_JUMP_SHEATHING);
    	this.sheathedMotions.put(LivingMotion.KNEELING, Animations.BIPED_KNEEL_SHEATHING);
    	this.sheathedMotions.put(LivingMotion.SNEAKING, Animations.BIPED_SNEAK_SHEATHING);
	}
	
	@Override
	public WieldStyle getStyle(LivingData<?> entitydata) {
		if (entitydata instanceof PlayerData) {
			PlayerData<?> playerdata = (PlayerData<?>)entitydata;
			if (playerdata.getSkill(SkillSlot.WEAPON_GIMMICK).getDataManager().hasData(KatanaPassive.SHEATH) &&
					playerdata.getSkill(SkillSlot.WEAPON_GIMMICK).getDataManager().getDataValue(KatanaPassive.SHEATH)) {
				return WieldStyle.SHEATH;
			} else {
				return WieldStyle.TWO_HAND;
			}
		} else {
			return WieldStyle.TWO_HAND;
		}
	}
	
	@Override
	public Map<LivingMotion, StaticAnimation> getLivingMotionChanges(PlayerData<?> player) {
		if(player.getSkill(SkillSlot.WEAPON_GIMMICK).getDataManager().getDataValue(SHEATH)) {
			return this.sheathedMotions;
		} else {
			return super.getLivingMotionChanges(player);
		}
	}
	
	@Override
	public boolean canUseOnMount() {
		return true;
	}


	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}
}