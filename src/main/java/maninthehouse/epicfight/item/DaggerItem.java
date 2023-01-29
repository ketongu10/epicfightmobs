package maninthehouse.epicfight.item;

import maninthehouse.epicfight.capabilities.item.CapabilityItem;
import maninthehouse.epicfight.capabilities.item.ModWeaponCapability;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Colliders;
import maninthehouse.epicfight.gamedata.Skills;
import maninthehouse.epicfight.gamedata.Sounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;

public class DaggerItem extends WeaponItem{

    public DaggerItem(ToolMaterial materialIn) {
        super(materialIn, 2+(int)materialIn.getAttackDamage(), -1.6F);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return false;
    }

    @Override
    public void setWeaponCapability() {
        capability = new ModWeaponCapability(CapabilityItem.WeaponCategory.DAGGER,(playerdata)->{
            CapabilityItem item = playerdata.getHeldItemCapability(EnumHand.OFF_HAND);
            if(item != null && item.getWeaponCategory() == CapabilityItem.WeaponCategory.DAGGER) {
                return CapabilityItem.WieldStyle.TWO_HAND;
            } else {
                return CapabilityItem.WieldStyle.ONE_HAND;
            }
        }, null, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.sword, CapabilityItem.HandProperty.GENERAL);
        capability.addStyleCombo(CapabilityItem.WieldStyle.ONE_HAND, Animations.DAGGER_AUTO_1, Animations.DAGGER_AUTO_2, Animations.DAGGER_AUTO_3, Animations.DAGGER_AIR_SLASH, Animations.KATANA_SHEATHING_DASH);
        capability.addStyleCombo(CapabilityItem.WieldStyle.TWO_HAND, Animations.DAGGER_DUAL_AUTO_1, Animations.DAGGER_DUAL_AUTO_2, Animations.DAGGER_DUAL_AUTO_3, Animations.DAGGER_DUAL_AUTO_4, Animations.DAGGER_DUAL_AIR_SLASH, Animations.DAGGER_DUAL_DASH);
        capability.addStyleCombo(CapabilityItem.WieldStyle.MOUNT, Animations.SWORD_MOUNT_ATTACK);
        capability.addStyleSpecialAttack(CapabilityItem.WieldStyle.ONE_HAND, Skills.EVISCERATE);
        capability.addStyleSpecialAttack(CapabilityItem.WieldStyle.TWO_HAND, Skills.EVISCERATE);
    }
}
