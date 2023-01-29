package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.BOSSES;

import chumbanotz.mutantbeasts.entity.mutant.MutantSkeletonEntity;
import maninthehouse.epicfight.animation.AnimatorServer;
import maninthehouse.epicfight.capabilities.entity.CapabilityEntity;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.NoAnimationData;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;

import java.util.ArrayList;

public class MutantSkeletonData extends NoAnimationData<MutantSkeletonEntity> implements IRangedAttackMobCapability {
    public MutantSkeletonData(){
        super();
    }
    public void MutantSkeletonModifiers() {

    }



    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source;
        if (this.orgEntity.getAnimationID() == 2) {
            source = new IndirectDamageSourceExtended("arrow", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.LONG);
            source.setImpact(5.0F);
        } else {
            source = new IndirectDamageSourceExtended("arrow", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.SHORT);
            source.setImpact(1.5F);
        }

        return source;
    }

    @Override
    public IExtendedDamageSource getMeleeDamageSource(Entity damageCarrier) {
        if (this.orgEntity.getAnimationID() == 1) {
            return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.LONG, IExtendedDamageSource.DamageType.PHYSICAL, 0);//maybe knockdown is better
        }
        return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.HOLD, IExtendedDamageSource.DamageType.PHYSICAL, 0);
    }


    protected void initAttributes() {
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(2.5F);
    }
    @Override
    protected void registerAttributes() {
        this.registerIfAbsent(ModAttributes.IMPACT);
    }




}
