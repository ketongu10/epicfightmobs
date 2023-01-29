package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import de.teamlapen.vampirism.entity.EntityGhost;
import de.teamlapen.vampirism.entity.hunter.EntityAdvancedHunter;
import de.teamlapen.vampirism.entity.hunter.EntityBasicHunter;
import de.teamlapen.vampirism.entity.hunter.EntityHunterTrainer;
import electroblob.wizardry.item.ItemScroll;
import electroblob.wizardry.registry.WizardryItems;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.MobData;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.capabilities.entity.mob.MobAttackPatterns;
import maninthehouse.epicfight.capabilities.entity.mob.VexData;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.entity.ai.EntityAIAttackPattern;
import maninthehouse.epicfight.entity.ai.EntityAIChase;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.item.ModItems;
import maninthehouse.epicfight.item.WeaponItem;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.math.MathUtils;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLContainerHolder;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.shadowmage.ancientwarfare.core.init.AWCoreItems;
import net.shadowmage.ancientwarfare.core.init.AWCoreLoot;
import net.shadowmage.ancientwarfare.npc.init.AWNPCItems;

import javax.annotation.Nullable;
import java.util.Iterator;

public class GhostData extends BipedMobData<EntityGhost> {

    private float prevPitchToTarget;
    private float pitchToTarget;
    public GhostData() {
        super(Faction.UNDEAD);
    }

    @Override
    public void onEntityJoinWorld(EntityGhost entityIn) {
        super.onEntityJoinWorld(entityIn);
        this.orgEntity.getDataManager().register(DataKeys.CHARGINGING, Boolean.valueOf(false));
        this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(1.0F));
        this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.GHOST_HOOD));
        this.orgEntity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(ModItems.GHOST_ROBE));

    }

    public boolean isCharging(){
        return this.orgEntity.getDataManager().get(DataKeys.CHARGINGING);
    }

    public void setCharging(boolean blocking){
        this.orgEntity.getDataManager().set(DataKeys.CHARGINGING, blocking);

    }
    @Override
    public void updateMotion() {
        currentMotion = LivingMotion.FLOATING;
        currentMixMotion = LivingMotion.IDLE;
    }

    @Override
    public void update() {
        this.prevPitchToTarget = this.pitchToTarget;
        super.update();
    }
    @Override
    protected void initAnimator(AnimatorClient animatorClient) {
        super.initAnimator(animatorClient);
        animatorClient.addLivingAnimation(LivingMotion.FLOATING, Animations.VEX_IDLE);
        animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.VEX_DEATH);
        animatorClient.addLivingMixAnimation(LivingMotion.IDLE, Animations.ZOMBIE_IDLE);
        animatorClient.setCurrentLivingMotionsToDefault();
    }
    @Override
    public void postInit() {
        orgEntity.tasks.addTask(0, new EntityAIAttackPattern(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.ZOMBIE_NORAML)); //BIPED_ARMED_ONEHAND //BIPED_ARMED_SPEAR2
        orgEntity.tasks.addTask(1, new EntityAIChase(this, this.orgEntity, 1.3D, false));
    }






    @Override
    public <M extends Model> M getEntityModel(Models<M> modelDB) {

        return modelDB.ENTITY_BIPED_64_32_TEX;
    }

    @Override
    public StaticAnimation getHitAnimation(IExtendedDamageSource.StunType stunType) {
        return Animations.VEX_HIT;
    }
    @Override
    public VisibleMatrix4f getHeadMatrix(float partialTicks) {
        return super.getHeadMatrix(partialTicks);
    }

    @Override
    public VisibleMatrix4f getModelMatrix(float partialTicks) {
        VisibleMatrix4f mat = super.getModelMatrix(partialTicks);

        if (this.isCharging()) {
            if (this.pitchToTarget == 0.0F && this.getAttackTarget() != null) {
                Entity target = this.getAttackTarget();
                double d0 = GhostData.this.orgEntity.posX - target.posX;
                double d1 = GhostData.this.orgEntity.posY - (target.posY + (double)target.height * 0.5D);
                double d2 = GhostData.this.orgEntity.posZ - target.posZ;
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                this.pitchToTarget = (float) (-(MathHelper.atan2(d1, d3) * (double) (180F / (float) Math.PI)));
            }
        } else {
            this.pitchToTarget = 0.0F;
        }

        VisibleMatrix4f.rotate((float)Math.toRadians(MathUtils.interpolateRotation(this.prevPitchToTarget, this.pitchToTarget, partialTicks)),
                new Vec3f(1, 0, 0), mat, mat);

        return mat;
    }

    class StopStandGoal extends EntityAIBase {
        public StopStandGoal() {
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            return GhostData.this.inaction;
        }

        @Override
        public void startExecuting() {
            GhostData.this.orgEntity.motionX = 0.0D;
            GhostData.this.orgEntity.motionY = 0.0D;
            GhostData.this.orgEntity.motionZ = 0.0D;
            GhostData.this.orgEntity.getNavigator().clearPath();
        }
    }

    class ChargeAttackGoal extends EntityAIBase {
        private int chargingCounter;

        public ChargeAttackGoal() {
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            if (GhostData.this.orgEntity.getAttackTarget() != null && !GhostData.this.inaction
                    && GhostData.this.orgEntity.getRNG().nextInt(10) == 0) {
                double distance = GhostData.this.orgEntity.getDistanceSq(GhostData.this.orgEntity.getAttackTarget());
                return distance < 50.0D;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return chargingCounter > 0;
        }

        @Override
        public void startExecuting() {
            Entity target = GhostData.this.getAttackTarget();
            GhostData.this.playAnimationSynchronize(Animations.VEX_CHARGING, 0.0F);
            GhostData.this.orgEntity.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
            GhostData.this.setCharging(true);

            double d0 = GhostData.this.orgEntity.posX - target.posX;
            double d1 = GhostData.this.orgEntity.posY - (target.posY + (double)target.height * 0.5D);
            double d2 = GhostData.this.orgEntity.posZ - target.posZ;
            double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            GhostData.this.pitchToTarget = (float)(-(MathHelper.atan2(d1, d3) * (double)(180F / (float)Math.PI)));
            this.chargingCounter = 20;
        }

        @Override
        public void resetTask() {
            GhostData.this.setCharging(false);
            GhostData.this.pitchToTarget = 0;
        }

        @Override
        public void updateTask() {
            --this.chargingCounter;
        }
    }
}
