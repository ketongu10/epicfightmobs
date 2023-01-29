package maninthehouse.epicfight.client.model.compatibility.WitcherEquipment;// Made with Blockbench 4.4.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelHair extends ModelBiped {
	ModelRenderer bone;
	ModelRenderer bone2;
	ModelRenderer bone3;
	ModelRenderer bone4;
	ModelRenderer bone5;
	ModelRenderer bone6;
	ModelRenderer bone7;
	ModelRenderer bone8;
	ModelRenderer bone9;
	ModelRenderer bone10;
	ModelRenderer bone11;
	ModelRenderer bone12;
	ModelRenderer bone13;
	ModelRenderer bone14;
	ModelRenderer bone15;

	public ModelHair() {
		super(0.0F, 0.0F, 64, 64);


		bone = new ModelRenderer(this, 0, 31).addBox(-2.0F, 0.0F, -1.0F, 2, 7, 1);
		bone.setRotationPoint(1.0F, -8.0F, 4.0F);
		setRotation(bone, 0.2618F, 0.0F, 0.0F);

		bone2 = new ModelRenderer(this, 6, 31).addBox(0.0F, 0.0F, -1.0F, 1, 7, 1);
		bone2.setRotationPoint(-2.0F, -7.9F, 3.8F);
		setRotation(bone2,0.2618F, -0.2618F, -0.1309F);

		bone10 = new ModelRenderer(this, 10, 31).addBox(-1.0F, 0.0F, -1.0F, 1, 7, 1);
		bone10.setRotationPoint(2.0F, -7.9F, 3.8F);
		setRotation(bone10,0.2618F, 0.2618F, 0.1309F);

		bone3 = new ModelRenderer(this, 14, 31).addBox(-1.0F, 0.0F, -1.0F, 1, 7, 1);
		bone3.setRotationPoint(-2.0F, -7.9F, 3.8F);
		setRotation(bone3,0.2618F, -0.3927F, -0.2182F);

		bone11 = new ModelRenderer(this, 18, 31).addBox(0.0F, 0.0F, -1.0F, 1, 7, 1);
		bone11.setRotationPoint(2.0F, -7.9F, 3.8F);
		setRotation(bone11,0.2618F, 0.3927F, 0.2182F);

		bone4 = new ModelRenderer(this, 22, 33).addBox(-1.0F, 2.0F, -1.0F, 1, 5, 1);
		bone4.setRotationPoint(-3.1F, -7.8F, 3.4F);
		setRotation(bone4,0.2618F, -0.48F, -0.3054F);

		bone12 = new ModelRenderer(this, 26, 33).addBox(0.0F, 2.0F, -1.0F, 1, 5, 1);
		bone12.setRotationPoint(3.1F, -7.8F, 3.4F);
		setRotation(bone12,0.2618F, 0.48F, 0.3054F);

		bone5 = new ModelRenderer(this, 0, 39).addBox(-0.3F, 0.1F, -1.3F, 1, 5, 1);
		bone5.setRotationPoint(0.3F, -1.5F, 6.0F);
		setRotation(bone5, 0.0873F, 0.0F, 0.0F);

		bone6 = new ModelRenderer(this, 4, 39).addBox(-1.3F, 0.1F, -1.1F, 1, 5, 1);
		bone6.setRotationPoint(0.3F, -1.5F, 5.9F);
		setRotation(bone6, 0.1745F, 0.0F, 0.0F);

		bone7 = new ModelRenderer(this, 8, 39).addBox(-0.5F, 0.1F, -1.1F, 1, 5, 1);
		bone7.setRotationPoint(-1.2F, -1.5F, 5.6F);
		setRotation(bone7, 0.1745F, -0.3927F, 0.0F);

		bone13 = new ModelRenderer(this, 12, 39).addBox(-0.5F, 0.1F, -1.1F, 1, 5, 1);
		bone13.setRotationPoint(1.2F, -1.5F, 5.6F);
		setRotation(bone13,  0.1745F, 0.3927F, 0.0F);

		bone8 = new ModelRenderer(this, 16, 39).addBox(-0.5F, 0.1F, -1.7F, 1, 5, 1);
		bone8.setRotationPoint(-2.2F, -1.6F, 5.6F);
		setRotation(bone8,  0.1745F, -0.6981F, 0.0F);

		bone14 = new ModelRenderer(this, 20, 39).addBox(-0.5F, 0.1F, -1.7F, 1, 5, 1);
		bone14.setRotationPoint(2.2F, -1.6F, 5.6F);
		setRotation(bone14,  0.1745F, 0.6981F, 0.0F);

		bone9 = new ModelRenderer(this, 24, 39).addBox(-1.7F, 0.1F, -0.7F, 1, 5, 0);
		bone9.setRotationPoint(-1.9F, -1.6F, 5.6F);
		setRotation(bone9,   0.2182F, -0.9163F, 0.0F);

		bone15 = new ModelRenderer(this, 28, 39).addBox(0.7F, 0.1F, -0.7F, 1, 5, 0);
		bone15.setRotationPoint(1.9F, -1.6F, 5.6F);
		setRotation(bone15,   0.2182F, 0.9163F, 0.0F);

		bone.setTextureSize(128, 64);
		bone2.setTextureSize(128, 64);
		bone3.setTextureSize(128, 64);
		bone4.setTextureSize(128, 64);
		bone5.setTextureSize(128, 64);
		bone6.setTextureSize(128, 64);
		bone7.setTextureSize(128, 64);
		bone8.setTextureSize(128, 64);
		bone9.setTextureSize(128, 64);
		bone10.setTextureSize(128, 64);
		bone11.setTextureSize(128, 64);
		bone12.setTextureSize(128, 64);
		bone13.setTextureSize(128, 64);
		bone14.setTextureSize(128, 64);
		bone15.setTextureSize(128, 64);


		this.bipedHeadwear.addChild(bone);
		this.bipedHeadwear.addChild(bone2);
		this.bipedHeadwear.addChild(bone3);
		this.bipedHeadwear.addChild(bone4);
		this.bipedHeadwear.addChild(bone5);
		this.bipedHeadwear.addChild(bone6);
		this.bipedHeadwear.addChild(bone7);
		this.bipedHeadwear.addChild(bone8);
		this.bipedHeadwear.addChild(bone9);
		this.bipedHeadwear.addChild(bone10);
		this.bipedHeadwear.addChild(bone11);
		this.bipedHeadwear.addChild(bone12);
		this.bipedHeadwear.addChild(bone13);
		this.bipedHeadwear.addChild(bone14);
		this.bipedHeadwear.addChild(bone15);
	}
	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}