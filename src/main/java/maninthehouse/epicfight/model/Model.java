package maninthehouse.epicfight.model;

import java.io.IOException;

import maninthehouse.epicfight.collada.ColladaModelLoader;
import net.minecraft.util.ResourceLocation;

public class Model {
	protected Armature armature;
	protected ResourceLocation location;

	public Model(ResourceLocation location) {
		this.location = location;
	}

	public void loadArmatureData() {
		try {
			this.armature = ColladaModelLoader.getArmature(location);
		} catch (IOException e) {
			System.err.println(location.getResourcePath() + " failed to load!");
		}
	}

	public void loadArmatureData(Armature armature) {
		this.armature = armature;
	}

	public Armature getArmature() {
		return armature;
	}
}