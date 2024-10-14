package net.dragonegg.moreburners.config;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

public class ClientConfig {

    public ClientConfig() {
    }

    public static ConfigValue<List<? extends String>> EMBER_BURNER_BLOCK_COVERED;
    public static ConfigValue<Integer> HEAT_BAR_LENGTH;

    public static void registerClientConfig() {

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Settings for all burners' parameters").push("parameters");
        HEAT_BAR_LENGTH = builder.comment("The length of the heat bar in create goggle gui of burners.").define("bar_length",18);

        if (ModList.get().isLoaded("embers")) {
            builder.comment("Settings for ember burner's parameters").push("ember_burner");
            List<String> preferences = new ArrayList<>();
            EMBER_BURNER_BLOCK_COVERED = builder.comment("Blocks that cover ember burner's flame particles.").defineList("blockCovered", preferences, (a) -> true);
            builder.pop();
        }

        builder.pop();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());
    }
}
