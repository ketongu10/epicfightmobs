package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.custom;

import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.NoAnimationData;
import maninthehouse.epicfight.config.EntityConfigurationCapability;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class CustomMonsterData<T extends EntityLiving> extends NoAnimationData<T> {
    private double impact = 0;

    public CustomMonsterData(double impact) {
        this.impact = impact;
    }

    public CustomMonsterData() {}

    public void setParameters(EntityConfigurationCapability.EntityConfig config) {
        this.impact = config.impact;
    }

    public void initAttributes() {
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(this.impact);
    }
    @Override
    public IExtendedDamageSource getMeleeDamageSource(Entity damageCarrier) {

        return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.SHORT, IExtendedDamageSource.DamageType.PHYSICAL, 0);
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
