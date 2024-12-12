package net.dragonegg.moreburners;

import net.dragonegg.moreburners.compat.embers.EmbersCompat;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.config.ClientConfig;
import net.dragonegg.moreburners.config.CommonConfig;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.registry.ItemRegistry;
import net.dragonegg.moreburners.registry.SoundRegistry;
import net.dragonegg.moreburners.registry.TabRegistry;
import net.dragonegg.moreburners.util.BoilerHeaterRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(MoreBurners.MODID)
public class MoreBurners {

    public static final String MODID = "moreburners";

    public MoreBurners() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        BlockRegistry.BLOCKS.register(modEventBus);
        BlockRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        TabRegistry.CREATIVE_TABS.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);

        CommonConfig.registerCommonConfig();
        ClientConfig.registerClientConfig();
        MinecraftForge.EVENT_BUS.register(this);

        if (ModList.get().isLoaded("embers")) {
            EmbersCompat.init();
        }

        if (ModList.get().isLoaded("pneumaticcraft")) {
            PneumaticCraftCompat.init();
        }

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        BoilerHeaterRegistry.registerBoilerHeaters();

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            //PonderIndex.register();
        }
    }

}
