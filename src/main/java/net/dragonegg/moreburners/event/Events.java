package net.dragonegg.moreburners.event;

import me.desht.pneumaticcraft.api.PNCCapabilities;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.content.block.entity.HeatConverterBlockEntity;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;


public class Events {

    public static void register(IEventBus bus) {
        bus.addListener(Events::attachBeCaps);
    }

    private static void attachBeCaps(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                BlockRegistry.ELECTRIC_BURNER_ENTITY.get(),
                (be, context) -> be.energy
        );

        if (MoreBurners.loadedPNE()) {
            event.registerBlockEntity(
                    PNCCapabilities.HEAT_EXCHANGER_BLOCK,
                    PneumaticCraftCompat.HEAT_CONVERTER_ENTITY.get(),
                    HeatConverterBlockEntity::getHeatExchanger
            );
        }
    }

}
