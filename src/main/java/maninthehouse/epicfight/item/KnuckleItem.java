package maninthehouse.epicfight.item;

import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.capabilities.item.CapabilityItem;
import maninthehouse.epicfight.capabilities.item.ModWeaponCapability;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Colliders;
import maninthehouse.epicfight.gamedata.Skills;
import maninthehouse.epicfight.gamedata.Sounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class KnuckleItem extends WeaponItem{
    public KnuckleItem() {
        super(ModMaterials.KNUCKLE, 2, 0.0F);
    }
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() == Items.IRON_INGOT;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return false;
    }

    @Override
    public void setWeaponCapability() {
        capability = new ModWeaponCapability(CapabilityItem.WeaponCategory.FIST, (playerdata)->{
                CapabilityItem item = playerdata.getHeldItemCapability(EnumHand.OFF_HAND);
                if(item != null && item.getWeaponCategory() == CapabilityItem.WeaponCategory.FIST || item == null) {
                    return CapabilityItem.WieldStyle.TWO_HAND;
                } else {
                    return CapabilityItem.WieldStyle.ONE_HAND;
                }
            }, null, Sounds.WHOOSH, Sounds.BLUNT_HIT, Colliders.fist, CapabilityItem.HandProperty.GENERAL);
        capability.addStyleCombo(CapabilityItem.WieldStyle.ONE_HAND, Animations.FIST_AUTO_1, Animations.FIST_AUTO_2, Animations.FIST_AUTO_3, Animations.FIST_AIR_SLASH, Animations.FIST_DASH);
        capability.addStyleCombo(CapabilityItem.WieldStyle.TWO_HAND, Animations.FIST_AUTO_1, Animations.FIST_AUTO_2, Animations.FIST_AUTO_3, Animations.FIST_AIR_SLASH, Animations.FIST_DASH);
        capability.addStyleSpecialAttack(CapabilityItem.WieldStyle.TWO_HAND, Skills.RELENTLESS_COMBO);
        capability.addStyleSpecialAttack(CapabilityItem.WieldStyle.ONE_HAND, null);

    }

}
