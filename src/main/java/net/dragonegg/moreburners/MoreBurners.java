package net.dragonegg.moreburners;

import net.dragonegg.moreburners.compat.embers.EmbersCompat;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.config.ClientConfig;
import net.dragonegg.moreburners.config.CommonConfig;
import net.dragonegg.moreburners.event.Events;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.registry.ItemRegistry;
import net.dragonegg.moreburners.registry.SoundRegistry;
import net.dragonegg.moreburners.registry.TabRegistry;
import net.dragonegg.moreburners.util.BoilerHeaterRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
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

        if (loadedEmber()) {
            EmbersCompat.init();
        }

        if (loadedPNE()) {
            PneumaticCraftCompat.init();
        }

    }

    public static ResourceLocation RL(String path) {
        return new ResourceLocation(MODID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        BoilerHeaterRegistry.registerBoilerHeaters();
        Events.register(MinecraftForge.EVENT_BUS);
    }

    public static boolean loadedEmber() {
        return ModList.get().isLoaded("embers");
    }

    public static boolean loadedPNE() {
        return ModList.get().isLoaded("pneumaticcraft");
    }

    public static boolean loadedBotania() {
        return ModList.get().isLoaded("botania");
    }

}
