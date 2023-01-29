package maninthehouse.epicfight.client.model.compatibility.WizardEquipment;

import com.windanesz.ancientspellcraft.AncientSpellcraft;
import com.windanesz.ancientspellcraft.entity.living.EntityClassWizard;
import com.windanesz.ancientspellcraft.entity.living.EntityEvilClassWizard;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.EntityWizard;
import electroblob.wizardry.item.ItemWizardArmour;
import electroblob.wizardry.registry.WizardryItems;
import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static maninthehouse.epicfight.main.LoaderConstants.ANCIENT_SPELLCRAFT;

public class ItemBeard extends ItemArmor {

    private int texindex;

    public ItemBeard(ArmorMaterial material, int renderIndex, EntityEquipmentSlot armourType, int texture){
        super(material, renderIndex, armourType);
        texindex = texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armourSlot, net.minecraft.client.model.ModelBiped original){
        return new ModelBeard();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
        if (ANCIENT_SPELLCRAFT && entity instanceof EntityEvilClassWizard) {
            int i = ((EntityEvilClassWizard)entity).getArmourClass().ordinal();
            int k = ((EntityEvilClassWizard)entity).textureIndex;
            return EpicFightMod.MODID+":textures/entity/evil_class_wizard/evil_"+((ItemWizardArmour.ArmourClass.values()[i].name().toLowerCase()+"_"+k)+".png");
        } else if (entity instanceof EntityWizard) {
            return EpicFightMod.MODID+":textures/entity/wizard/wizard_"+((EntityWizard)entity).textureIndex+".png";
        } else if (entity instanceof EntityEvilWizard) {
            return EpicFightMod.MODID+":textures/entity/evil_wizard/evil_wizard_" + ((EntityEvilWizard)entity).textureIndex + ".png";
        }
        return EpicFightMod.MODID+":textures/entity/evil_class_wizard/evil_sage_" + this.texindex + ".png";

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(entityequipmentslot);

        if (itemstack1.isEmpty())
        {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.setCount(0);
            this.texindex = (texindex+1) % 6;
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }

}
