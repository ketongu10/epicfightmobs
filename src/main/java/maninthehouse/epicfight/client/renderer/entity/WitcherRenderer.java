package maninthehouse.epicfight.client.renderer.entity;

import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.client.events.engine.RenderEngine;
import maninthehouse.epicfight.item.ModItems;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.shadowmage.ancientwarfare.npc.entity.NpcBase;

public class WitcherRenderer<E extends EntityLivingBase, T extends LivingData<E>> extends BipedRenderer<E, T> {

    public final ResourceLocation textureLocation;

    public WitcherRenderer(String texturePath) {
        textureLocation = new ResourceLocation(texturePath);
    }


    protected ResourceLocation getEntityTexture(E entityIn) {

        return textureLocation;
    }


}
