package maninthehouse.epicfight.skill;

import io.netty.buffer.Unpooled;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninthehouse.epicfight.client.events.engine.ControllEngine;
import maninthehouse.epicfight.entity.event.PlayerEventListener;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.main.EpicFightMod;
import maninthehouse.epicfight.network.ModNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.UUID;

/**public class BladeRushSkill extends SpecialAttackSkill {
    private static final SkillDataManager.SkillDataKey<Integer> COMBO_COUNT = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
    private static final UUID EVENT_UUID = UUID.fromString("444a1a6a-c2f1-11eb-8529-0242ac130003");

    public BladeRushSkill(SkillSlot index, float restriction, String skillName, float chargeIn) {
        super( index,  restriction,   skillName, chargeIn, null);
    }

    @Override
    public PacketBuffer gatherArguments(ClientPlayerData executer, ControllEngine controllEngine) {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        buf.writeBoolean(true);
        return buf;
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(COMBO_COUNT);
        container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_POST_EVENT, EVENT_UUID, (event) -> {
            int animationId = event.getDamageSource().getSkillId();
            if (animationId >= Animations.BLADE_RUSH_FIRST.getId() && animationId <= Animations.BLADE_RUSH_FINISHER.getId() && !event.getTarget().isEntityAlive()) {
                this.setStackSynchronize(event.getPlayerData(), container.stack + 1);
            }
            return false;
        });

        container.executer.getEventListener().addEventListener(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, EVENT_UUID, (event) -> {
            if (container.isActivated) {
                PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
                buf.writeBoolean(false);
                this.executeOnServer(event.getPlayerData(), buf);
                return true;
            }
            return false;
        });
    }

    @Override
    public void onRemoved(SkillContainer container) {
        container.executer.getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_POST_EVENT, EVENT_UUID);
        container.executer.getEventListener().removeListener(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, EVENT_UUID);
    }

    @Override
    public void executeOnServer(ServerPlayerData executer, PacketBuffer args) {
        if (executer.getSkill(this.).isActivated() && args.readBoolean()) {
            this.cancelOnServer(executer, null);
        } else {
            int firstComboId = Animations.BLADE_RUSH_FIRST.getId();
            int animationId = firstComboId + executer.getSkill(this.category).getDataManager().getDataValue(COMBO_COUNT);
            executer.playAnimationSynchronize(EpicFightMod.IN.animationManager.findAnimation(EpicFightMod.MODID.hashCode(), animationId), 0);
            ModNetworkManager.sendToPlayer(new STCResetBasicAttackCool(), executer.getOriginalEntity());
            executer.getSkill(this.category).getDataManager().setData(COMBO_COUNT, (animationId - firstComboId + 1) % 4);
            this.setDurationSynchronize(executer, this.maxDuration);
            this.setStackSynchronize(executer, executer.getSkill(this.category).getStack() - 1);
            executer.getSkill(this.category).activate();
        }
    }

    @Override
    public void cancelOnClient(ClientPlayerData executer, PacketBuffer args) {
        executer.getSkill(this.category).deactivate();
    }

    @Override
    public void cancelOnServer(ServerPlayerData executer, PacketBuffer args) {
        executer.getSkill(this.category).deactivate();
        executer.getSkill(this.category).getDataManager().setData(COMBO_COUNT, 0);
        ModNetworkManager.sendToPlayer(new STCSkillExecutionFeedback(this.category.getIndex(), false), executer.getOriginalEntity());
    }



    @SuppressWarnings("deprecation")
    @Override
    public void onScreen(ClientPlayerData playerdata, float resolutionX, float resolutionY) {
        if (playerdata.getSkill(this.category).isActivated()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(EpicFightMod.MODID, "textures/gui/overlay/blade_rush.png"));
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1, 1, 1, 1);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0, 0, 1).tex(0, 0).endVertex();
            bufferbuilder.pos(0, resolutionY, 1).tex(0, 1).endVertex();
            bufferbuilder.pos(resolutionX, resolutionY, 1).tex(1, 1).endVertex();
            bufferbuilder.pos(resolutionX, 0, 1).tex(1, 0).endVertex();
            tessellator.draw();
        }
    }

    @Override
    public SpecialAttackSkill registerPropertiesToAnimation() {
        ((AttackAnimation)Animations.BLADE_RUSH_FIRST).phases[0].addProperties(this.properties.get(0).entrySet());
        ((AttackAnimation)Animations.BLADE_RUSH_SECOND).phases[0].addProperties(this.properties.get(0).entrySet());
        ((AttackAnimation)Animations.BLADE_RUSH_THIRD).phases[0].addProperties(this.properties.get(0).entrySet());
        ((AttackAnimation)Animations.BLADE_RUSH_FINISHER).phases[0].addProperties(this.properties.get(1).entrySet());
        return this;
    }
}**/