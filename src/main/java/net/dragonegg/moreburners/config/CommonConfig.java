package net.dragonegg.moreburners.config;

import net.dragonegg.moreburners.MoreBurners;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

@EventBusSubscriber(modid = MoreBurners.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonConfig {

    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec COMMON_CONFIG;

    public static ConfigValue<Double> EMBER_BURNER_MAX_CAPACITY;
    public static ConfigValue<Double> EMBER_BURNER_EMBER_COST;
    public static ConfigValue<Double> EMBER_BURNER_HEATING_RATE;
    public static ConfigValue<Double> EMBER_BURNER_COOLING_RATE;
    public static ConfigValue<Double> EMBER_BURNER_RATE_BELLOWS_MULTIPLIER;
    public static ConfigValue<Double> EMBER_BURNER_MAX_HEAT;
    public static ConfigValue<Double> EMBER_BURNER_MAX_HEAT_BELLOWS_1;
    public static ConfigValue<Double> EMBER_BURNER_MAX_HEAT_BELLOWS_2;
    public static ConfigValue<Integer> ELECTRIC_BURNER_MAX_CAPACITY;
    public static ConfigValue<Integer> ELECTRIC_BURNER_ENERGY_COST;
    public static ConfigValue<Double> ELECTRIC_BURNER_ENERGY_MULTIPLIER_1;
    public static ConfigValue<Double> ELECTRIC_BURNER_ENERGY_MULTIPLIER_2;
    public static ConfigValue<Double> ELECTRIC_BURNER_MAX_HEAT;
    public static ConfigValue<Double> ELECTRIC_BURNER_UPGRADED_MAX_HEAT;
    public static ConfigValue<Double> ELECTRIC_BURNER_HEATING_RATE;
    public static ConfigValue<Double> ELECTRIC_BURNER_COOLING_RATE;
    public static ConfigValue<Double> PNE_SEETHING_TEMP;
    public static ConfigValue<Double> PNE_KINDLED_TEMP;
    public static ConfigValue<Double> PNE_FADING_TEMP;
    public static ConfigValue<Double> PNE_SMOULDERING_TEMP;
    public static ConfigValue<Double> PNE_TEMP_COST;
    public static ConfigValue<Double> PNE_HEAT_CONVERTER_TEMP_COST;
    public static ConfigValue<Integer> EXOFLAME_BOOST_RATE;
    public static ConfigValue<Integer> EXOFLAME_SEETHING_BOOST_RATE;

    public static ConfigValue<Double> SEETHING_HEAT;
    public static ConfigValue<Double> KINDLED_HEAT;
    public static ConfigValue<Double> FADING_HEAT;
    public static ConfigValue<Double> SMOULDERING_HEAT;

    static {
        COMMON_BUILDER.comment("Settings for all burners' parameters").push("parameters");
        SEETHING_HEAT = COMMON_BUILDER.comment("The minimum heat value required to become seething. (super heating)").define("seething_heat", 340.0);
        KINDLED_HEAT = COMMON_BUILDER.comment("The minimum heat value required to become kindled. (heating)").define("kindled_heat", 200.0);
        FADING_HEAT = COMMON_BUILDER.comment("The minimum heat value required to become fading. (heating)").define("fading_heat", 160.0);
        SMOULDERING_HEAT = COMMON_BUILDER.comment("The minimum heat value required to become smouldering.").define("smouldering_heat", 80.0);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Settings for electric burner's parameters").push("electric_burner");
        ELECTRIC_BURNER_MAX_CAPACITY = COMMON_BUILDER.comment("The max amount of energy capacity.").define("max_capacity", 50000);
        ELECTRIC_BURNER_ENERGY_COST = COMMON_BUILDER.comment("The amount of energy consumed per tick.").define("energy_cost", 80);
        ELECTRIC_BURNER_ENERGY_MULTIPLIER_1 = COMMON_BUILDER.comment("The value to multiply on the energy consumed per tick when heating.").define("energy_multiplier_1", 1.5);
        ELECTRIC_BURNER_ENERGY_MULTIPLIER_2 = COMMON_BUILDER.comment("The value to multiply on the energy consumed per tick when super heating.").define("energy_multiplier_2", 2.0);
        ELECTRIC_BURNER_HEATING_RATE = COMMON_BUILDER.comment("The amount of heat gained per second when consuming energy.").define("heating_rate", 2.0);
        ELECTRIC_BURNER_COOLING_RATE = COMMON_BUILDER.comment("The amount of heat lost per second when not consuming energy.").define("cooling_rate", 1.0);
        ELECTRIC_BURNER_MAX_HEAT = COMMON_BUILDER.comment("The maximum heat value the electric burner can reach before upgraded.").define("max_heat", 300.0);
        ELECTRIC_BURNER_UPGRADED_MAX_HEAT = COMMON_BUILDER.comment("The maximum heat value the electric burner can reach after upgraded.").define("upgraded_max_heat", 400.0);
        COMMON_BUILDER.pop();

        if (MoreBurners.loadedEmber()) {
            COMMON_BUILDER.comment("Settings for ember burner's parameters").push("ember_burner");
            EMBER_BURNER_MAX_CAPACITY = COMMON_BUILDER.comment("The max amount of ember capacity.").define("max_capacity", 6000.0);
            EMBER_BURNER_EMBER_COST = COMMON_BUILDER.comment("The amount of ember consumed per tick.").define("ember_cost", 1.0);
            EMBER_BURNER_HEATING_RATE = COMMON_BUILDER.comment("The amount of heat gained per second when consuming ember.").define("heating_rate", 1.5);
            EMBER_BURNER_COOLING_RATE = COMMON_BUILDER.comment("The amount of heat lost per second when not consuming ember.").define("cooling_rate", 1.0);
            EMBER_BURNER_RATE_BELLOWS_MULTIPLIER = COMMON_BUILDER.comment("The value to multiply on the heating rate and the cooling rate when ember burner is adjacent to atmospheric bellows.").define("rate_multiplier", 2.0);
            EMBER_BURNER_MAX_HEAT = COMMON_BUILDER.comment("The maximum heat value the ember burner can reach without atmospheric bellows.").define("max_heat", 300.0);
            EMBER_BURNER_MAX_HEAT_BELLOWS_1 = COMMON_BUILDER.comment("The maximum heat value the ember burner adjacent to atmospheric bellows can reach.").define("max_heat_1", 400.0);
            EMBER_BURNER_MAX_HEAT_BELLOWS_2 = COMMON_BUILDER.comment("The maximum heat value the ember burner one blocks away from atmospheric bellows can reach.").define("max_heat_2", 340.0);
            COMMON_BUILDER.pop();
        }

        if(MoreBurners.loadedPNE()) {
            COMMON_BUILDER.comment("Pneumatic Craft Compat Configs").push("pne_compat");
            PNE_SEETHING_TEMP = COMMON_BUILDER.comment("The minimum temperature required to become seething. (super heating)").define("seething_temp", 1200.0);
            PNE_KINDLED_TEMP = COMMON_BUILDER.comment("The minimum temperature required to become kindled. (heating)").define("kindled_temp", 450.0);
            PNE_FADING_TEMP = COMMON_BUILDER.comment("The minimum temperature required to become fading. (heating)").define("fading_temp", 400.0);
            PNE_SMOULDERING_TEMP = COMMON_BUILDER.comment("The minimum temperature required to become smouldering.").define("smouldering_temp", 200.0);
            PNE_TEMP_COST = COMMON_BUILDER.comment("The amount of temperature decreased per tick when using heat device directly.").define("temp_cost", 5.0);
            PNE_HEAT_CONVERTER_TEMP_COST = COMMON_BUILDER.comment("The amount of temperature decreased per tick.").define("heat_converter_temp_cost", 2.0);
            COMMON_BUILDER.pop();
        }

        if(MoreBurners.loadedBotania()) {
            COMMON_BUILDER.comment("Settings for exoflame compat parameters").push("exoflame_compat");
            EXOFLAME_BOOST_RATE = COMMON_BUILDER.comment("Maximum number of blaze burner exoflame can provide heat sustainably.").define("boost_rate", 9);
            EXOFLAME_SEETHING_BOOST_RATE = COMMON_BUILDER.comment("Maximum number of blaze burner exoflame can provide heat sustainably when superheated.").define("seething_boost_rate", 1);
            COMMON_BUILDER.pop();
        }

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        MoreBurners.loadConfig(COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MoreBurners.MODID + "-common.toml"));
    }

}
