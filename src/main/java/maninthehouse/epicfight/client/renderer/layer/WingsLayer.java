package maninthehouse.epicfight.client.renderer.layer;

import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.utils.math.MathUtils;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import me.paulf.wings.client.flight.FlightView;
import me.paulf.wings.client.flight.FlightViews;
import me.paulf.wings.client.renderer.LayerWings;
import me.paulf.wings.server.apparatus.FlightApparatuses;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class WingsLayer<E extends EntityLivingBase, T extends LivingData<E>> extends Layer<E, T> {
    private final RenderLivingBase<?> renderer;
    private final LayerWings.TransformFunction transform;

    public WingsLayer(RenderLivingBase<?> renderer, LayerWings.TransformFunction transform) {
        this.renderer = renderer;
        this.transform = transform;
    }
    @Override
    public void renderLayer(T entitydata, E entity, VisibleMatrix4f[] poses, float partialTicks) {
        float scale = 0.0F;
        float delta = 0.0F;
        float f = MathUtils.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float f1 = MathUtils.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float f2 = f1 - f;
        float f7 = entitydata.getPitch(partialTicks);
        ItemStack stack;
        FlightView flight;
        if (!entity.isInvisible() && !(stack = FlightApparatuses.find(entity)).isEmpty() && (flight = FlightViews.get(entity)) != null) {
            flight.ifFormPresent((form) -> {
                this.renderer.bindTexture(form.getTexture());
                GlStateManager.pushMatrix();
                this.transform.apply(entity, scale);
                //GlStateManager.func_179089_o();
                form.render(delta, scale);
                if (stack.isItemEnchanted()) {
                    LayerArmorBase.renderEnchantedGlint(this.renderer, entity, new ModelBase() {
                        public void func_78088_a(Entity entityIn, float limbSwing, float limbSwingAmount, float delta, float yawHead, float pitch, float scale) {
                            form.render(delta, scale);
                        }
                    }, entity.limbSwing, entity.limbSwingAmount, delta, delta, f2, f7, scale);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                }

                //GlStateManager.func_179129_p();
                GlStateManager.popMatrix();
            });
        }
    }
}
