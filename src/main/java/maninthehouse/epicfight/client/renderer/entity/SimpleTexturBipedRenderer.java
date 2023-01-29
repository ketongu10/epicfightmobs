package maninthehouse.epicfight.client.renderer.entity;

import com.windanesz.ancientspellcraft.entity.living.EntityClassWizard;
import de.teamlapen.vampirism.entity.hunter.EntityAdvancedHunter;
import de.teamlapen.vampirism.entity.hunter.EntityHunterBase;
import de.teamlapen.vampirism.entity.hunter.EntityHunterTrainer;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import maninthehouse.epicfight.capabilities.entity.LivingData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowmage.ancientwarfare.npc.entity.NpcBase;

import static maninthehouse.epicfight.main.LoaderConstants.ANCIENT_WARFARE;

@SideOnly(Side.CLIENT)
public class SimpleTexturBipedRenderer<E extends EntityLivingBase, T extends LivingData<E>> extends BipedRenderer<E, T> {
	public final ResourceLocation textureLocation;
	
	public SimpleTexturBipedRenderer(String texturePath) {
		textureLocation = new ResourceLocation(texturePath);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(E entityIn) {
		if (ANCIENT_WARFARE) {
			if (entityIn instanceof NpcBase) {
			return ((NpcBase) entityIn).getTexture();
			}
		}
		return textureLocation;
	}
}
