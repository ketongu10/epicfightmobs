package maninthehouse.epicfight.animation.types.attack;

import maninthehouse.epicfight.capabilities.entity.LivingData;
import maninthehouse.epicfight.physics.Collider;
import maninthehouse.epicfight.utils.math.Vec3f;

public class MountAttackAnimation extends AttackAnimation {
	public MountAttackAnimation(int id, float convertTime, float antic, float preDelay, float contact, float recovery, Collider collider, String index, String path) {
		super(id, convertTime, antic, preDelay, contact, recovery, false, collider, index, path);
	}
	
	protected Vec3f getCoordVector(LivingData<?> entitydata) {
		return new Vec3f(0, 0, 0);
	}
}