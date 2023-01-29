package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import chumbanotz.mutantbeasts.entity.mutant.MutantSkeletonEntity;
import maninthehouse.epicfight.capabilities.entity.CapabilityEntity;
import maninthehouse.epicfight.entity.ai.attribute.ModAttributes;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;


/**For modded creatures with cool models**/
public abstract class NoAnimationData<T extends EntityLiving> extends CapabilityEntity<T> {

    @Override
    public void onEntityJoinWorld(T entityIn) {
        this.initAttributes();
    }
    @Override
    public void onEntityConstructed(T entityIn) {

        System.out.println("+++++++++++++Entity got cap ");
        super.onEntityConstructed(entityIn);
        this.registerAttributes();}

    protected void initAttributes() {}
    protected void registerAttributes() {}

    protected void registerIfAbsent(IAttribute attribute) {
        AbstractAttributeMap attributeMap = this.orgEntity.getAttributeMap();
        if (attributeMap.getAttributeInstance(attribute) == null) {
            attributeMap.registerAttribute(attribute);
        }
    }

    public IExtendedDamageSource getMeleeDamageSource(Entity damageCarrier){ return null;}

    public double getAttributeValue(IAttribute attribute) {
        return this.orgEntity.getAttributeMap().getAttributeInstance(attribute).getAttributeValue();
    }
    @Override
    public float getDefenceIgnore() {
        return 0;
    }
    @Override
    public float getImpact() {
        return 0;
    }
    @Override
    public void update() {}
    @Override
    protected void updateOnClient(){}
    @Override
    protected void updateOnServer(){}
    @Override
    public VisibleMatrix4f getModelMatrix(float partialTicks){return null;}

}
