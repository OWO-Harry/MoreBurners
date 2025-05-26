package net.dragonegg.moreburners.util;


import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.common.block.entity.IHeatExchangingTE;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.content.block.BaseBurnerBlock;
import net.dragonegg.moreburners.content.block.entity.HeatConverterBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterRegistry {

    public static void registerBoilerHeaters() {
        for (Block block : BurnerUtil.getBurners()) {
            register(block);
        }

        if (MoreBurners.loadedPNE()) {
            registerPNEBlocks();
        }
    }

    public static float fromHeatLevel(BlazeBurnerBlock.HeatLevel value) {
        return switch (value) {
            case NONE -> BoilerHeater.NO_HEAT;
            case SMOULDERING -> BoilerHeater.PASSIVE_HEAT;
            case FADING, KINDLED -> 1;
            case SEETHING -> 2;
        };
    }

    public static void register(Block block) {
        if (!(block instanceof BaseBurnerBlock)) {
            return;
        }
        BoilerHeater.REGISTRY.register(block, (level, pos, state) ->
                fromHeatLevel(state.getValue(BlazeBurnerBlock.HEAT_LEVEL)));
    }

    public static void registerPNEBlocks() {
        PneumaticCraftCompat.streamBlockEntities().forEach(be -> {
            if (be instanceof IHeatExchangingTE) {
                BoilerHeater.REGISTRY.register(be.getBlockState().getBlock(), ((level, pos, state) -> {
                    if (level.getBlockEntity(pos) instanceof IHeatExchangingTE heatTE) {
                        IHeatExchangerLogic logic = heatTE.getHeatExchanger(Direction.UP);
                        if (logic != null) {
                            return fromHeatLevel(HeatConverterBlockEntity.getHeatLevel(logic.getTemperature()));
                        }
                    }
                    return BoilerHeater.NO_HEAT;
                }));
            }
        });
    }

}
