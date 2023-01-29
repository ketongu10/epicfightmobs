package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.BOSSES;

import chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.NoAnimationData;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import net.minecraft.entity.Entity;

public class MutantEndermanData extends NoAnimationData<MutantEndermanEntity> implements IRangedAttackMobCapability {

    public MutantEndermanData() {super();}

    protected void initAttributes() {
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(3.5F);
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.ARMOR_NEGATION).setBaseValue(15.0F);
    }

    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source = new IndirectDamageSourceExtended("block", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.LONG);
        source.setImpact(5.0F);
        return source;
    }

        @Override
    public IExtendedDamageSource getMeleeDamageSource(Entity damageCarrier) {
        switch(this.orgEntity.getAttackID()){
            case 2:
                return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.LONG, IExtendedDamageSource.DamageType.PHYSICAL, 0);//maybe knockdown is better
            case 8:
                return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.HOLD, IExtendedDamageSource.DamageType.PHYSICAL, 0);
            default:
                return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.SHORT, IExtendedDamageSource.DamageType.PHYSICAL, 0);
        }
    }

    @Override
    public float getDefenceIgnore() {
        return (float) this.getAttributeValue(ModAttributes.ARMOR_NEGATION);
    }
    @Override
    public float getImpact() {
        return (float) this.getAttributeValue(ModAttributes.IMPACT);
    }


    @Override
    protected void registerAttributes() {
        this.registerIfAbsent(ModAttributes.IMPACT);
        this.registerIfAbsent(ModAttributes.ARMOR_NEGATION);
    }

}

