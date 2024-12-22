package net.dragonegg.moreburners.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;


public class CommonConfig {

    public CommonConfig() {
    }

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
    public static ConfigValue<Double> HEAT_CONVERTER_SEETHING_TEMP;
    public static ConfigValue<Double> HEAT_CONVERTER_KINDLED_TEMP;
    public static ConfigValue<Double> HEAT_CONVERTER_FADING_TEMP;
    public static ConfigValue<Double> HEAT_CONVERTER_SMOULDERING_TEMP;
    public static ConfigValue<Double> HEAT_CONVERTER_TEMP_COST;
    public static ConfigValue<Integer> EXOFLAME_BOOST_RATE;
    public static ConfigValue<Integer> EXOFLAME_SEETHING_BOOST_RATE;

    public static ConfigValue<Double> SEETHING_HEAT;
    public static ConfigValue<Double> KINDLED_HEAT;
    public static ConfigValue<Double> FADING_HEAT;
    public static ConfigValue<Double> SMOULDERING_HEAT;

    public static void registerCommonConfig(){

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Settings for all burners' parameters").push("parameters");
        SEETHING_HEAT = builder.comment("The minimum heat value required to become seething. (super heating)").define("seething_heat", 340.0);
        KINDLED_HEAT = builder.comment("The minimum heat value required to become kindled. (heating)").define("kindled_heat", 200.0);
        FADING_HEAT = builder.comment("The minimum heat value required to become fading. (heating)").define("fading_heat", 160.0);
        SMOULDERING_HEAT = builder.comment("The minimum heat value required to become smouldering.").define("smouldering_heat", 80.0);

        if (ModList.get().isLoaded("embers")) {
            builder.comment("Settings for ember burner's parameters").push("ember_burner");
            EMBER_BURNER_MAX_CAPACITY = builder.comment("The max amount of ember capacity.").define("max_capacity", 6000.0);
            EMBER_BURNER_EMBER_COST = builder.comment("The amount of ember consumed per tick.").define("ember_cost", 1.0);
            EMBER_BURNER_HEATING_RATE = builder.comment("The amount of heat gained per tick when consuming ember.").define("heating_rate", 1.0);
            EMBER_BURNER_COOLING_RATE = builder.comment("The amount of heat lost per tick when not consuming ember.").define("cooling_rate", 1.0);
            EMBER_BURNER_RATE_BELLOWS_MULTIPLIER = builder.comment("The value to multiply on the heating rate and the cooling rate when ember burner is adjacent to atmospheric bellows.").define("rate_multiplier", 2.0);
            EMBER_BURNER_MAX_HEAT = builder.comment("The maximum heat value the ember burner can reach without atmospheric bellows.").define("max_heat", 300.0);
            EMBER_BURNER_MAX_HEAT_BELLOWS_1 = builder.comment("The maximum heat value the ember burner adjacent to atmospheric bellows can reach.").define("max_heat_1", 400.0);
            EMBER_BURNER_MAX_HEAT_BELLOWS_2 = builder.comment("The maximum heat value the ember burner one blocks away from atmospheric bellows can reach.").define("max_heat_2", 340.0);
            builder.pop();
        }

        if(ModList.get().isLoaded("pneumaticcraft")) {
            builder.comment("Settings for heat converter's parameters").push("heat_converter");
            HEAT_CONVERTER_SEETHING_TEMP = builder.comment("The minimum temperature required to become seething. (super heating)").define("seething_temp", 1200.0);
            HEAT_CONVERTER_KINDLED_TEMP = builder.comment("The minimum temperature required to become kindled. (heating)").define("kindled_temp", 450.0);
            HEAT_CONVERTER_FADING_TEMP = builder.comment("The minimum temperature required to become fading. (heating)").define("fading_temp", 400.0);
            HEAT_CONVERTER_SMOULDERING_TEMP = builder.comment("The minimum temperature required to become smouldering.").define("smouldering_temp", 200.0);
            HEAT_CONVERTER_TEMP_COST = builder.comment("The amount of temperature decreased per tick.").define("temp_cost", 2.0);
            builder.pop();
        }

        if(ModList.get().isLoaded("botania")) {
            builder.comment("Settings for exoflame compat parameters").push("exoflame_compat");
            EXOFLAME_BOOST_RATE = builder.comment("Maximum number of blaze burner exoflame can provide heat sustainably.").define("boost_rate", 9);
            EXOFLAME_SEETHING_BOOST_RATE = builder.comment("Maximum number of blaze burner exoflame can provide heat sustainably when superheated.").define("seething_boost_rate", 1);
            builder.pop();
        }

        builder.comment("Settings for electric burner's parameters").push("electric_burner");
        ELECTRIC_BURNER_MAX_CAPACITY = builder.comment("The max amount of energy capacity.").define("max_capacity", 50000);
        ELECTRIC_BURNER_ENERGY_COST = builder.comment("The amount of energy consumed per tick.").define("energy_cost", 100);
        ELECTRIC_BURNER_ENERGY_MULTIPLIER_1 = builder.comment("The value to multiply on the energy consumed per tick when heating.").define("energy_multiplier_1", 1.5);
        ELECTRIC_BURNER_ENERGY_MULTIPLIER_2 = builder.comment("The value to multiply on the energy consumed per tick when super heating.").define("energy_multiplier_2", 2.0);
        ELECTRIC_BURNER_HEATING_RATE = builder.comment("The amount of heat gained per tick when consuming energy.").define("heating_rate", 1.0);
        ELECTRIC_BURNER_COOLING_RATE = builder.comment("The amount of heat lost per tick when not consuming energy.").define("cooling_rate", 1.0);
        ELECTRIC_BURNER_MAX_HEAT = builder.comment("The maximum heat value the electric burner can reach before upgraded.").define("max_heat", 300.0);
        ELECTRIC_BURNER_UPGRADED_MAX_HEAT = builder.comment("The maximum heat value the electric burner can reach after upgraded.").define("upgraded_max_heat", 400.0);
        builder.pop();

        builder.pop();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }

}
