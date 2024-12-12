package net.dragonegg.moreburners.content.client;

import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.content.block.HeatConverterBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
        modid = MoreBurners.MODID,
        bus = Bus.MOD,
        value = {Dist.CLIENT}
)
public class ColorHandlers {

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {

        if(ModList.get().isLoaded("pneumaticcraft")) {
            HeatConverterBlock block = (HeatConverterBlock) PneumaticCraftCompat.HEAT_CONVERTER.get();
            event.register(block::getTintColor, block);
        }

    }

}
