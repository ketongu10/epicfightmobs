package maninthehouse.epicfight.entity.ai;

import maninthehouse.epicfight.animation.types.ActionAnimation;
import maninthehouse.epicfight.animation.types.attack.AttackAnimation;
import maninthehouse.epicfight.capabilities.entity.MobData;
import net.minecraft.entity.EntityCreature;

import java.util.List;

public class EntityAIActionWithChance extends EntityAIActionPattern
{
    protected final float executeChance;

    public EntityAIActionWithChance(MobData mobdata, EntityCreature attacker, double patternMinRange, double patternMaxRange, float chance, boolean affectHorizon,
                                     List<ActionAnimation> pattern)
    {
        super(mobdata, attacker, patternMinRange, patternMaxRange, affectHorizon, pattern);
        this.executeChance = chance;
    }

    @Override
    public boolean shouldExecute()
    {
        return super.shouldExecute() && attacker.getRNG().nextFloat() < executeChance;
    }
}