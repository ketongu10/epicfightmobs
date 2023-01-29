package maninthehouse.epicfight.capabilities.entity;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;

public class DataKeys {
	public static final DataParameter<Float> STUN_ARMOR = new DataParameter<Float> (252, DataSerializers.FLOAT);
	public static final DataParameter<Float> STAMINA = new DataParameter<Float> (253, DataSerializers.FLOAT);

	public static final DataParameter<Boolean> PARRYING = new DataParameter<Boolean> (254, DataSerializers.BOOLEAN);

	public static final DataParameter<Boolean> CHARGINGING = new DataParameter<Boolean> (251, DataSerializers.BOOLEAN);
}