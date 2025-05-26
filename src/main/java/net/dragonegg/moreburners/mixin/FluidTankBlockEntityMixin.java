package net.dragonegg.moreburners.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import me.desht.pneumaticcraft.common.block.HeatPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidTankBlockEntity.class)
public class FluidTankBlockEntityMixin {

    @Inject(
            method = "updateBoilerState",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/tank/FluidTankBlockEntity;refreshCapability()V"),
            remap = false
    )
    private void updateBoilerState$updateHeatSource(CallbackInfo ci, @Local(ordinal = 0) int yOffset, @Local(ordinal = 1) FluidTankBlockEntity tank) {
        Level level = tank.getLevel();
        BlockPos pos = tank.getBlockPos();
        if (level != null && yOffset == 0) {
            level.updateNeighborsAt(pos, tank.getBlockState().getBlock());
            BlockState state = level.getBlockState(pos.below());
            BlockState newState = state.updateShape(Direction.UP, tank.getBlockState(), level, pos.below(), pos);
            level.setBlockAndUpdate(pos.below(), newState);
        }
    }

}
