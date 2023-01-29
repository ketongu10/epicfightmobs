package maninthehouse.epicfight.item;

import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.capabilities.item.CapabilityItem;
import maninthehouse.epicfight.capabilities.item.ModWeaponCapability;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Colliders;
import maninthehouse.epicfight.gamedata.Skills;
import maninthehouse.epicfight.gamedata.Sounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class LongswordItem extends WeaponItem {
    public LongswordItem(ToolMaterial materialIn) {
        super(materialIn, 3 + (int)materialIn.getAttackDamage(), -2.5F);
        this.setStats();
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return false;
    }

    @Override
    public void setWeaponCapability() {

    }

    public void setStats() {
        int harvestLevel = this.material.getHarvestLevel();
        capability = new ModWeaponCapability(CapabilityItem.WeaponCategory.LONGSWORD, (playerdata)-> CapabilityItem.WieldStyle.TWO_HAND,
                null, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.longsword, CapabilityItem.HandProperty.TWO_HANDED);
        capability.addStyleCombo(CapabilityItem.WieldStyle.TWO_HAND, Animations.LONGSWORD_AUTO_1,  Animations.LONGSWORD_AUTO_2, Animations.LONGSWORD_AUTO_3, Animations.LONGSWORD_AIR_SLASH, Animations.LONGSWORD_DASH);
        capability.addStyleCombo(CapabilityItem.WieldStyle.MOUNT, Animations.SWORD_MOUNT_ATTACK);
        capability.addStyleAttributeSimple(CapabilityItem.WieldStyle.TWO_HAND, 0.0D, 0.9D + harvestLevel * 0.5D, 3);
        capability.addStyleSpecialAttack(CapabilityItem.WieldStyle.TWO_HAND, Skills.LETHAL_SLICING);
        capability.addLivingMotionChanger(LivingMotion.RUNNING, Animations.BIPED_RUN_HELDING_WEAPON);
        capability.addLivingMotionChanger(LivingMotion.WALKING, Animations.BIPED_RUN_HELDING_WEAPON);
        capability.addLivingMotionChanger(LivingMotion.BLOCKING, Animations.LONGSWORD_GUARD);

    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }


}
