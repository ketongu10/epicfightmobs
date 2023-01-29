package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import maninthehouse.epicfight.capabilities.entity.DataKeys;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFactionSiegeEngineer;
import net.shadowmage.ancientwarfare.npc.entity.vehicle.NpcSiegeEngineer;

public class PlayerEngineerData extends BipedMobData<NpcSiegeEngineer> implements IRangedAttackMobCapability {
    public PlayerEngineerData() {super(Faction.NATURAL);
    }
    @Override
    public void onEntityJoinWorld(NpcSiegeEngineer entityIn) {
        super.onEntityJoinWorld(entityIn);
        this.orgEntity.getDataManager().register(DataKeys.STUN_ARMOR, Float.valueOf(2.0F));
    }


    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.registerIfAbsent(ModAttributes.MAX_STUN_ARMOR);
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.MAX_STUN_ARMOR).setBaseValue(2.0F);
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
        return modelDB.ENTITY_BIPED;
    }

    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source = new IndirectDamageSourceExtended("vehicle_bolt", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.LONG);
        source.setImpact(1000.0F);

        return source;
    }




}
