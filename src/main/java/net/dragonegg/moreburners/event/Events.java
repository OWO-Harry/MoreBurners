package net.dragonegg.moreburners.event;

import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.content.block.entity.ElectricBurnerBlockEntity;
import net.dragonegg.moreburners.content.block.entity.HeatConverterBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;


public class Events {

    public static void register(IEventBus bus) {
        bus.addListener(Events::attachBeCaps);
    }

    private static void attachBeCaps(RegisterCapabilitiesEvent event) {
        ElectricBurnerBlockEntity.registerCapabilities(event);

        if (MoreBurners.loadedPNE()) {
            HeatConverterBlockEntity.registerCapabilities(event);
        }
    }

}
