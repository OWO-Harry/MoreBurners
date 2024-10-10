package net.dragonegg.moreburners.config;


import net.dragonegg.moreburners.compat.embers.EmbersCompat;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientConfig {

    public static ConfigValue<List<? extends String>> EMBER_BURNER_BLOCK_COVERED;
    public static ConfigValue<Integer> HEAT_BAR_LENGTH;

    public static void register(ModLoadingContext modLoadingContext) {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ClientConfig instance = new ClientConfig(builder);
        ForgeConfigSpec config = builder.build();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT,config);
    }

    public static boolean isCovered(Block block) {
        Iterator var1 = ((List)EMBER_BURNER_BLOCK_COVERED.get()).iterator();
        String pass;
        String[] ids = block.getDescriptionId().split("\\.");
        String id = ids[1] + ":" + ids[2];
        do {
            if (!var1.hasNext()) {
                return false;
            }
            pass = (String)var1.next();
        } while(!pass.equals(id));

        return true;
    }

    private ClientConfig(ForgeConfigSpec.Builder builder) {

        builder.comment("Settings for all burners' parameters").push("parameters");
        HEAT_BAR_LENGTH = builder.comment("The length of the heat bar in create goggle gui of burners.").define("bar_length",18);

        if (ModList.get().isLoaded("embers")) {
            builder.comment("Settings for ember burner's parameters").push("ember_burner");
            List<String> preferences = new ArrayList<>();
            EMBER_BURNER_BLOCK_COVERED = builder.comment("Blocks that cover ember burner's flame particles.").defineList("blockCovered", preferences, (a) -> true);
            builder.pop();
        }

        builder.pop();
    }
}
