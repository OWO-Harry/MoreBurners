package net.dragonegg.moreburners.util;

import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.dragonegg.moreburners.content.block.BaseBurnerBlock;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterRegistry {

    public static void registerBoilerHeaters() {
        for (Block block : BurnerUtil.getBurners()) {
            register(block);
        }
    }

    public static void register(Block block) {
        if (!(block instanceof BaseBurnerBlock)) {
            return;
        }
        BoilerHeater.REGISTRY.register(block, (level, pos, state) -> {
            BlazeBurnerBlock.HeatLevel value = state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
            if (value == BlazeBurnerBlock.HeatLevel.NONE) {
                return -1.0F;
            } else if (value == BlazeBurnerBlock.HeatLevel.SEETHING) {
                return 2.0F;
            } else {
                return value.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING) ? 1.0F : 0.0F;
            }
        });
    }

}
