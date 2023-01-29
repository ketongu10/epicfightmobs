package maninthehouse.epicfight.skill;

import maninthehouse.epicfight.capabilities.entity.player.PlayerData;
import maninthehouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninthehouse.epicfight.client.ClientEngine;
import maninthehouse.epicfight.entity.event.MovementInputEvent;
import maninthehouse.epicfight.entity.event.PlayerEvent;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Sounds;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.Particle;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Enchantments;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

/**public class LiechtenhauerSkill extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("244c57c0-a837-11eb-bcbc-0242ac130002");

    public LiechtenhauerSkill() {
        super(SkillSlot.WEAPON_GIMMICK, 5.0F, "longsword_passive");
    }

    @Override
    public void onInitiate(SkillContainer container) {
        container.duration = this.duration + EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.SWEEPING, container.executer.getOriginalEntity());
        container.executer.getEventListener().addEventListener(EntityEventListener.Event.ON_ACTION_SERVER_EVENT, PlayerEvent.makeEvent(EVENT_UUID, (event) -> {
            if (container.isActivated) {
                if (!event.getAttackTarget().isEntityAlive()) {
                    this.setDurationSynchronize((ServerPlayerData) container.executer, container.duration + 1);
                }
            }
            return false;
        }));

        container.executer.getEventListener().addEventListener(EntityEventListener.Event.ON_ACTION_SERVER_EVENT, PlayerEvent.makeEvent(EVENT_UUID, (event) -> {
            if (container.duration > 0 && this.isExecutableState(container.executer) && this.canExecute(container.executer) &&
                    isBlockableSource(event.getAttackTarget().getLastDamageSource())) {
                DamageSource damageSource = event.getAttackTarget().getLastDamageSource();
                boolean isFront = false;
                Vec3d vector3d2 = damageSource.getDamageLocation();

                if (vector3d2 != null) {
                    Vec3d vector3d = container.executer.getOriginalEntity().getLook(1.0F);
                    Vec3d vector3d1 = vector3d2.subtractReverse(container.executer.getOriginalEntity().getPositionVector()).normalize();
                    vector3d1 = new Vec3d(vector3d1.x, 0.0D, vector3d1.z);
                    if (vector3d1.dotProduct(vector3d) < 0.0D) {
                        isFront = true;
                    }
                }

                if (isFront) {
                    this.setDurationSynchronize((ServerPlayerData) container.executer, container.duration - 1);
                    container.executer.playAnimationSynchronize(Animations.LONGSWORD_GUARD_HIT, 0);
                    container.executer.playSound(Sounds.BLADE_HIT, -0.1F, 0.2F);
                    Entity playerentity = container.executer.getOriginalEntity();
                    /**Particle.HIT_BLUNT.get().spawnParticleWithArgument((playerentity.world), HitParticleType.POSITION_MIDDLE_OF_EACH_ENTITY,
                            HitParticleType.ARGUMENT_ZERO, playerentity, damageSource.getImmediateSource());//    хз как сделать

                    float knockback = 0.25F;

                    if (damageSource instanceof IExtendedDamageSource) {
                        knockback += ((IExtendedDamageSource)damageSource).getImpact() * 0.1F;
                    }
                    if (damageSource.getImmediateSource() instanceof EntityLiving) {
                        knockback += EnchantmentHelper.getKnockbackModifier((EntityLiving)damageSource.getImmediateSource()) * 0.1F;
                    }

                    container.executer.knockBackEntity(damageSource.getImmediateSource(), knockback);
                    return true;
                }
            }

            return false;
        }));

        container.executer.getEventListener().addEventListener(MovementInputEvent, EVENT_UUID, (event) -> {
            if (container.executer.getSkill(this.slot.getIndex()).isActivated) {
                EntityPlayerSP clientPlayer = container.executer;
                clientPlayer.setSprinting(false);
                clientPlayer.sprintToggleTimer = -1;
                Minecraft mc = Minecraft.getMinecraft();
                ClientEngine.INSTANCE.inputController.setKeyBind(mc.gameSettings.keyBindSprint, false);
            }

            return false;
        });
    }

    @Override
    public void onDeleted(SkillContainer container) {
        container.executer.getEventListener().removeListener(EntityEventListener.Event.HIT_EVENT, EVENT_UUID);
        container.executer.getEventListener().removeListener(EntityEventListener.Event.DEALT_DAMAGE_POST_EVENT, EVENT_UUID);
        container.executer.getEventListener().removeListener(EntityEventListener.Event.MOVEMENT_INPUT_EVENT, EVENT_UUID);
    }

    @Override
    public void executeOnServer(ServerPlayerData executer, PacketBuffer args) {
        if (executer.getSkill(this.slot.getIndex()).isActivated) {
            super.cancelOnServer(executer, args);
            this.setConsumptionSynchronize(executer, this.consumption * ((float)executer.getSkill(this.category).duration /
                    (this.maxDuration + EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.SWEEPING, executer.getOriginalEntity()) + 1)));
            this.setDurationSynchronize(executer, 0);
            this.setStackSynchronize(executer, executer.getSkill(this.category).getStack() - 1);
            executer.modifiLivingMotions(executer.getHeldItemCapability(EnumHand.MAIN_HAND));
        } else {
            this.setDurationSynchronize(executer, this.maxDuration + EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.SWEEPING, executer.getOriginalEntity()));
            executer.getSkill(this.slot.getIndex()).execute(executer.);
            executer.modifiLivingMotions(executer.getHeldItemCapability(EnumHand.MAIN_HAND));
        }
    }

    @Override
    public void cancelOnServer(ServerPlayerData executer, PacketBuffer args) {
        super.cancelOnServer(executer, args);
        this.setConsumptionSynchronize(executer, 0);
        this.setStackSynchronize(executer, executer.getSkill(this.slot.getIndex()).getStack() - 1);
        executer.modifiLivingMotions(executer.getHeldItemCapability(EnumHand.MAIN_HAND));
    }

    @Override
    public boolean canExecute(PlayerData<?> executer) {
        if (executer.isRemote()) {
            return super.canExecute(executer);
        } else {
            return executer.getHeldItemCapability(EnumHand.MAIN_HAND).getSpecialAttack(executer) == this && executer.getOriginalEntity().getRidingEntity() == null;
        }
    }

    @Override
    public Skill registerPropertiesToAnimation() {
        return this;
    }

    private static boolean isBlockableSource(DamageSource damageSource) {
        return !damageSource.isUnblockable() && !damageSource.isExplosion();
    }
}
**/