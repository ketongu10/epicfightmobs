package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.BOSSES;

import chumbanotz.mutantbeasts.entity.mutant.MutantSnowGolemEntity;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.NoAnimationData;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import net.minecraft.entity.Entity;

public class MutantSnowGolemData extends NoAnimationData<MutantSnowGolemEntity> implements IRangedAttackMobCapability {
    public MutantSnowGolemData(){
        super();
    }

    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source = new IndirectDamageSourceExtended("ice_block", this.orgEntity, damageCarrier, IExtendedDamageSource.StunType.LONG);
        source.setImpact(5.0F);
        return source;
    }

}
