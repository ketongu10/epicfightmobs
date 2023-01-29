package maninthehouse.epicfight.client.renderer.entity;

import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.client.renderer.layer.EyeLayer;
import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class WitherSkeletonRenderer<E extends EntityLivingBase, T extends LivingData<E>> extends SimpleTexturBipedRenderer<E, T> {

    private static final ResourceLocation EYE_TEXTURE = new ResourceLocation(EpicFightMod.MODID+":textures/entity/wither_skeleton_eyes.png");

    public WitherSkeletonRenderer(String texture) {
        super(texture);
        //this.layers.add(new EyeLayer<>(EYE_TEXTURE));
    }
}
