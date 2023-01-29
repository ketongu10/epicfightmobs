package maninthehouse.epicfight.client.renderer.entity;

import com.github.alexthe666.iceandfire.client.render.entity.RenderDreadGhoul;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeClientHandler;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowmage.ancientwarfare.npc.entity.NpcBase;

import java.lang.reflect.Method;

import static maninthehouse.epicfight.main.LoaderConstants.ANCIENT_WARFARE;

@SideOnly(Side.CLIENT)
public class CustomMobRenderer <E extends EntityLivingBase, T extends LivingData<E>> extends BipedRenderer<E, T> {

    public final ResourceLocation textureLocation = new ResourceLocation(EpicFightMod.MODID + ":textures/entity/npc_default.png");

    public CustomMobRenderer() {
    }
    //RenderDreadGhoul


    @Override
    protected ResourceLocation getEntityTexture(E entityIn) {

        Render render = Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(entityIn.getClass());
        try {
            Method method = render.getClass().getDeclaredMethod("getEntityTexture", entityIn.getClass());
            method.setAccessible(true);
            return (ResourceLocation) method.invoke(render, entityIn);
        } catch (Exception e) {
            //EpicFightMod.LOGGER.warn("Failed to get renderManager for " + entityIn.getClass());
            //System.err.println(e);
        }
        try {
            Method method = render.getClass().getSuperclass().getDeclaredMethod("getEntityTexture", entityIn.getClass());
            method.setAccessible(true);
            return (ResourceLocation) method.invoke(render, entityIn);
        } catch (Exception e) {
            //EpicFightMod.LOGGER.warn("Failed to get renderManager for " + entityIn.getClass());
            //System.err.println(e);
        }
        return textureLocation;
    }
}

