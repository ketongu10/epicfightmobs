package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import io.netty.buffer.ByteBuf;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.animation.types.StaticAnimation;
import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.entity.ai.EntityAIArcher;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.network.server.STCMobInitialSetting;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumDifficulty;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFactionArcherElite;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFactionSiegeEngineer;

public class FactionEngineerData extends BipedMobData<NpcFactionSiegeEngineer> implements IRangedAttackMobCapability {
    public FactionEngineerData() {super(Faction.NATURAL);
    }
    @Override
    public void onEntityJoinWorld(NpcFactionSiegeEngineer entityIn) {
        super.onEntityJoinWorld(entityIn);
        this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(0));
    }
    /**@Override
    protected void initAI(){

    }**/

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.registerIfAbsent(ModAttributes.MAX_STUN_ARMOR);
    }

    @Override
    protected void initAnimator(AnimatorClient animatorClient) {
        super.commonBipedCreatureAnimatorInit(animatorClient);
        super.initAnimator(animatorClient);
    }

    @Override
    public void updateMotion() {
        super.commonCreatureUpdateMotion();
    }



    @Override
    public <M extends Model> M getEntityModel(Models<M> modelDB) {
        if (this.orgEntity.getSkinSettings().isAlexModel()) {
            return modelDB.ENTITY_BIPED_SLIM_ARM;
        }
        if (this.orgEntity.getFaction().equals("orc")) {
            return modelDB.ENTITY_ORC;
        }
        return modelDB.ENTITY_BIPED;
    }
    @Override
    public VisibleMatrix4f getModelMatrix(float partialTicks) {
        float h = orgEntity.height/1.8F;
        float w = orgEntity.width/0.6F;
        if (this.orgEntity.getFaction().equals("orc")) {
            h*=1.1;
        }
        VisibleMatrix4f mat = super.getModelMatrix(partialTicks);
        return VisibleMatrix4f.scale(new Vec3f(w, h, w), mat, mat);
    }

    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source = new IndirectDamageSourceExtended("AWMissile", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.LONG);
        source.setImpact(100.0F);

        return source;
    }



}
