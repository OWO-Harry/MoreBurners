package net.dragonegg.moreburners.content.client;

import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.content.block.HeatConverterBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(
        modid = MoreBurners.MODID,
        bus = EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class ColorHandlers {

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {

        if(ModList.get().isLoaded("pneumaticcraft")) {
            HeatConverterBlock block = PneumaticCraftCompat.HEAT_CONVERTER.get();
            event.register(block::getTintColor, block);
        }

    }

}
