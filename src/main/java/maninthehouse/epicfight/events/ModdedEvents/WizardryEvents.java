package maninthehouse.epicfight.events.ModdedEvents;

import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.event.SpellCastEvent;
import maninthehouse.epicfight.capabilities.ModCapabilities;
import maninthehouse.epicfight.capabilities.entity.CapabilityEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static maninthehouse.epicfight.main.LoaderConstants.EBWIZARDRY;

public class WizardryEvents {

    @SubscribeEvent
    public static void castEvent(SpellCastEvent.Post event) {
        if (event.getSource() != SpellCastEvent.Source.DISPENSER && event.getSource() != SpellCastEvent.Source.OTHER ) {
            CapabilityEntity entitydata = event.getCaster().getCapability(ModCapabilities.CAPABILITY_ENTITY, null);
            if (event.getCaster() instanceof ISpellCaster && entitydata != null) {
                entitydata.onEntityCast(event.getSpell());
            }
        }
    }
}
