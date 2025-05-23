package net.dragonegg.moreburners;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.event.Events;
import net.dragonegg.moreburners.registry.*;
import net.dragonegg.moreburners.config.*;

import net.dragonegg.moreburners.util.BoilerHeaterRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;

@Mod(MoreBurners.MODID)
public class MoreBurners {

    public static final String MODID = "moreburners";

    public MoreBurners(IEventBus eventBus, ModContainer container) {

        eventBus.addListener(this::commonSetup);
        BlockRegistry.BLOCKS.register(eventBus);
        BlockRegistry.BLOCK_ENTITY_TYPES.register(eventBus);
        ItemRegistry.ITEMS.register(eventBus);
        TabRegistry.CREATIVE_TABS.register(eventBus);

        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_CONFIG);
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        Events.register(eventBus);

        if (loadedPNE()) {
            PneumaticCraftCompat.init();
        }

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        BoilerHeaterRegistry.registerBoilerHeaters();
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

    public static void loadConfig(ModConfigSpec spec, java.nio.file.Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.correct(configData);
    }

}
