package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import electroblob.wizardry.constants.SpellType;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.spell.LifeDrain;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import maninthehouse.epicfight.animation.LivingMotion;
import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.capabilities.entity.mob.AbstractIllagerData;
import maninthehouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninthehouse.epicfight.capabilities.entity.mob.Faction;
import maninthehouse.epicfight.client.animation.AnimatorClient;
import maninthehouse.epicfight.effects.ModEffects;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.gamedata.Animations;
import maninthehouse.epicfight.gamedata.Models;
import maninthehouse.epicfight.model.Model;
import maninthehouse.epicfight.utils.math.Vec3f;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFaction;
import net.shadowmage.ancientwarfare.npc.entity.faction.NpcFactionSpellcasterWizardry;

public class FactionSpellcasterData extends BipedMobData<NpcFactionSpellcasterWizardry> {

    public int isCast;

    public FactionSpellcasterData() {
        super(Faction.NATURAL);
        this.isCast = 0;

    }

    /**@Override
    protected void initAI() {}**/
    @Override
    protected void initAnimator(AnimatorClient animatorClient) {
        super.initAnimator(animatorClient);
        animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE);
        animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.BIPED_WALK);
        animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
        animatorClient.addLivingAnimation(LivingMotion.SUMMONNING, Animations.SWORD_DASH);
        animatorClient.addLivingMixAnimation(LivingMotion.SPELLCASTING, Animations.MAGIC_DASH_WAND);
        animatorClient.setCurrentLivingMotionsToDefault();
    }
    @Override
    public void updateMotion() {
        super.commonCreatureUpdateMotion();
        if (isCast>0) {
            currentMixMotion = LivingMotion.SPELLCASTING;
            isCast--;
        } else {currentMixMotion = LivingMotion.NONE;}

    }

    @Override
    protected void updateOnClient() {
        super.updateOnClient();
    }

    @Override
    protected void updateOnServer() {
        super.updateOnServer();



    }

    @Override
    public void onEntityCast(Spell spell) {
        if (spell.isContinuous) {
            setCast(60);
        } else if (spell.getType() == SpellType.MINION) {
            playAnimationSynchronize(Animations.EVOKER_CAST_SPELL, 0); //ТАК РАБОТАЕТ!!!
        } else {
            playAnimationSynchronize(Animations.MAGIC_SWING, 0); //ТАК РАБОТАЕТ!!!
        }
    }



    public void setCast(int a)
    {
        this.isCast = a;
    }
    @Override
    public void setAIAsUnarmed() {}
    @Override
    public void setAIAsArmed() {}
    @Override
    public void setAIAsMounted(Entity ridingEntity) {}
    @Override
    public <M extends Model> M getEntityModel(Models<M> modelDB) {
        if (this.orgEntity.getSkinSettings().isAlexModel()) {
            return modelDB.ENTITY_BIPED_SLIM_ARM;
        }
        if (this.orgEntity.getFaction().equals("orc")) {
            return modelDB.ENTITY_ORC;
        }
        return modelDB.ENTITY_BIPED;
    }
    @Override
    public VisibleMatrix4f getModelMatrix(float partialTicks) {
        float h = orgEntity.height/1.8F;
        float w = orgEntity.width/0.6F;
        if (this.orgEntity.getFaction().equals("orc")) {
            h*=1.1;
        }
        VisibleMatrix4f mat = super.getModelMatrix(partialTicks);
        return VisibleMatrix4f.scale(new Vec3f(w, h, w), mat, mat);
    }

}
