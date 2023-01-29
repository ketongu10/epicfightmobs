package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.BOSSES;

import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.NoAnimationData;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class MutantZombieData extends NoAnimationData<MutantZombieEntity> {
    public MutantZombieData(){
        super();
    }


    protected void initAttributes() {
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(5.0F);
    }
    @Override
    public IExtendedDamageSource getMeleeDamageSource(Entity damageCarrier) {
         if (this.orgEntity.getAttackID() == 2) {
             return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.LONG, IExtendedDamageSource.DamageType.PHYSICAL, 0);//maybe knockdown is better
         }
        return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.HOLD, IExtendedDamageSource.DamageType.PHYSICAL, 0);
    }
    @Override
    public float getDefenceIgnore() {
        return 0;
    }
    @Override
    public float getImpact() {
        return (float) this.getAttributeValue(ModAttributes.IMPACT);
    }


    @Override
    protected void registerAttributes() {
        this.registerIfAbsent(ModAttributes.IMPACT);
    }

}
