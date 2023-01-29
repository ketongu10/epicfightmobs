package maninthehouse.epicfight.capabilities.entity.mob.moddedMobs;

import maninthehouse.epicfight.capabilities.entity.CapabilityEntity;
import maninthehouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninthehouse.epicfight.utils.game.IExtendedDamageSource;
import maninthehouse.epicfight.utils.game.IndirectDamageSourceExtended;
import maninthehouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.shadowmage.ancientwarfare.vehicle.entity.VehicleBase;
import net.shadowmage.ancientwarfare.vehicle.missiles.Ammo;
import net.shadowmage.ancientwarfare.vehicle.missiles.MissileBase;

public class AWVehicleData extends CapabilityEntity<VehicleBase> implements IRangedAttackMobCapability {

    public AWVehicleData() {super();}

    @Override
    public IndirectDamageSourceExtended getRangedDamageSource(Entity damageCarrier) {
        IndirectDamageSourceExtended source = new IndirectDamageSourceExtended("AWMissile", this.orgEntity.getRidingEntity(), damageCarrier, IExtendedDamageSource.StunType.LONG);
        source.setImpact(5.0F);

        return source;
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
