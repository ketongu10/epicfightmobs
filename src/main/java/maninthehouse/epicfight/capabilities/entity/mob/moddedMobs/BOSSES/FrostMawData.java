package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.BOSSES;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.NoAnimationData;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import net.minecraft.entity.Entity;

public class FrostMawData extends NoAnimationData<EntityFrostmaw> implements IRangedAttackMobCapability {

    public FrostMawData() {super();}

    protected void initAttributes() {
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(6.0F);
    }

    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source;
        if (orgEntity.getAnimation() == orgEntity.ICE_BALL_ANIMATION) {
            source = new IndirectDamageSourceExtended("ice_ball", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.LONG);
            source.setImpact(5.0F);
        } else {
            source = new IndirectDamageSourceExtended("scream", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.HOLD);
            source.setImpact(1.5F);
        }

        return source;
    }

    @Override
    public IExtendedDamageSource getMeleeDamageSource(Entity damageCarrier) {
        if (this.orgEntity.getAnimation() == orgEntity.SLAM_ANIMATION){
            return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.LONG, IExtendedDamageSource.DamageType.PHYSICAL, 0);//maybe knockdown is better
        }
        return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.LONG, IExtendedDamageSource.DamageType.PHYSICAL, 0);
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
