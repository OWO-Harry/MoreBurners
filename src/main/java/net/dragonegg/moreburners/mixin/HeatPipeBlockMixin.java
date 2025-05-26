package net.dragonegg.moreburners.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.desht.pneumaticcraft.common.block.HeatPipeBlock;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HeatPipeBlock.class)
public class HeatPipeBlockMixin {

    @ModifyVariable(
            method = "recalculateState",
            at = @At(value = "STORE"),
            remap = false
    )
    private static boolean recalculateState$addConnections(
            boolean connected, @Local Level level, @Local(argsOnly = true) BlockPos currentPos, @Local Direction dir) {
        BlockPos pos = currentPos.relative(dir);
        Block block = level.getBlockState(pos).getBlock();
        BlockEntity entity = level.getBlockEntity(pos);
        return connected
                || PneumaticCraftCompat.checkBasin(block, dir, entity)
                || PneumaticCraftCompat.checkBoiler(block, dir, entity);
    }

}
