package maninthehouse.epicfight.events.ModdedEvents;

import maninthehouse.epicfight.main.EpicFightMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;


public class ModsEventHandler {

	public static void registerEvents() {
		if (EBWIZARDRY) {
			MinecraftForge.EVENT_BUS.register(WizardryEvents.class);
		}
	}
}
