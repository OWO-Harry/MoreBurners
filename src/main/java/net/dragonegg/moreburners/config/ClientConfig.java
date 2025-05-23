package net.dragonegg.moreburners.config;

import net.dragonegg.moreburners.MoreBurners;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = MoreBurners.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig {

    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec CLIENT_CONFIG;

    public static ConfigValue<Integer> HEAT_BAR_LENGTH;
    public static ConfigValue<List<? extends String>> EMBER_BURNER_BLOCK_COVERED;

    static {
        CLIENT_BUILDER.comment("Settings for all burners' parameters").push("parameters");
        HEAT_BAR_LENGTH = CLIENT_BUILDER.comment("The length of the heat bar in create goggle gui of burners.").define("bar_length",18);
        CLIENT_BUILDER.pop();

        if (MoreBurners.loadedEmber()) {
            CLIENT_BUILDER.comment("Settings for ember burner's parameters").push("ember_burner");
            List<String> preferences = new ArrayList<>();
            EMBER_BURNER_BLOCK_COVERED = CLIENT_BUILDER.comment("Blocks that cover ember burner's flame particles.").defineList("blockCovered", preferences, a -> true);
        }

        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        MoreBurners.loadConfig(CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MoreBurners.MODID + "-client.toml"));
    }

}
