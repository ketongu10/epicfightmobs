package maninthehouse.epicfight.entity.ai;

import de.teamlapen.vampirism.entity.hunter.EntityBasicHunter;
import electroblob.wizardry.entity.living.ISpellCaster;
import maninthehouse.epicfight.animation.types.ActionAnimation;
import maninthehouse.epicfight.capabilities.entity.MobData;
import maninthehouse.epicfight.capabilities.entity.mob.moddedMobs.ISkillfulParry;
import maninthehouse.epicfight.network.ModNetworkManager;
import maninthehouse.epicfight.network.server.STCPlayAnimationTarget;
import maninthehouse.epicfight.utils.game.Formulars;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

import java.util.ArrayList;
import java.util.List;

import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;

public class EntityAIParry extends EntityAIActionPattern{
    protected List<List<ActionAnimation>> patterns = new ArrayList<List<ActionAnimation>>();
    private int pattern_num = 0;
    private boolean started = false;
    private float probability;

    public EntityAIParry(MobData mobdata, EntityCreature attacker, float prob, double minDist, double maxDIst, boolean affectHorizon, List<ActionAnimation>... patterns) {
        super(mobdata, attacker,minDist,maxDIst, affectHorizon, null);
        this.probability = prob;
        for (List<ActionAnimation> pat : patterns) {
            this.patterns.add(pat);

        }

    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        return isValidTarget(entitylivingbase) && isTargetInRange(entitylivingbase) && isTargetGreater(entitylivingbase);
    }


    @Override
    public boolean shouldContinueExecuting() {
        return patterns.get(pattern_num).size() > patternIndex;// && isTargetInRange(entitylivingbase);

    }

    @Override
    protected boolean canExecuteAttack() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if ((attacker.getRNG().nextFloat()<this.probability || started) && isValidTarget(entitylivingbase) && isTargetInRange(entitylivingbase) && !entitylivingbase.isSwingInProgress) {
            ((ISkillfulParry)mobdata).setParrying(false);
        }
        return !((ISkillfulParry)mobdata).isParrying();
    }

    @Override
    public void updateTask() {
        List<ActionAnimation> pr_pattern = patterns.get(pattern_num);
        if(this.canExecuteAttack() && !mobdata.isInaction()) {
            started = true;
            ActionAnimation att = pr_pattern.get(patternIndex);
            //this.patternIndex %= pr_pattern.size();
            mobdata.getServerAnimator().playAnimation(att, 0);
            mobdata.updateInactionState();
            if (attacker.getAttackTarget() != null) {
                ModNetworkManager.sendToAllPlayerTrackingThisEntity(new STCPlayAnimationTarget(att.getId(), attacker.getEntityId(), 0, attacker.getAttackTarget().getEntityId()), attacker);
            }
            patternIndex++;
        }
    }

    @Override
    public void startExecuting() {
        ((ISkillfulParry)mobdata).setParrying(true);
        pattern_num =  attacker.getRNG().nextInt(patterns.size());
    }

    @Override
    public void resetTask() {
        ((ISkillfulParry)mobdata).setParrying(false);
        started = false;
        patternIndex = 0;
    }

    private boolean isTargetGreater(EntityLivingBase target) {
        if (target instanceof EntityPlayer || target instanceof IRangedAttackMob || (EBWIZARDRY && target instanceof ISpellCaster)) {
            return true;
        }
        float health = target.getHealth();
        Item holdItem = (target.getHeldItemMainhand().getItem());
        Item attackerWeapon = (attacker.getHeldItemMainhand().getItem());
        Double entityDamage = target.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) == null ? 5 : target.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        return Formulars.DPM(health, entityDamage, 1, holdItem) > Formulars.DPM(attacker.getHealth(), 5,1,attackerWeapon) ;
    }
}