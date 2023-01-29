package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.BOSSES;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.NoAnimationData;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import net.minecraft.entity.Entity;

public class WroughtnautData extends NoAnimationData<EntityWroughtnaut> {
    public WroughtnautData() {super();}

    protected void initAttributes() {
        this.orgEntity.getAttributeMap().getAttributeInstance(ModAttributes.IMPACT).setBaseValue(5.0F);
    }

    @Override
    public IExtendedDamageSource getMeleeDamageSource(Entity damageCarrier) {
        if(this.orgEntity.getAnimation() == this.orgEntity.STOMP_ATTACK_ANIMATION) {
            return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.SHORT, IExtendedDamageSource.DamageType.PHYSICAL, 0);//maybe knockdown is better
        }
        return IExtendedDamageSource.causeMobDamage(orgEntity, IExtendedDamageSource.StunType.LONG, IExtendedDamageSource.DamageType.PHYSICAL, 0);

    }

    @Override
    public float getDefenceIgnore() {
        return (float) 0;
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
