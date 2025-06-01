package net.dragonegg.moreburners.mixin.compat;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.common.block.entity.IHeatExchangingTE;
import net.dragonegg.moreburners.content.block.entity.HeatConverterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;

@Mixin(BasinRecipe.class)
public class BasinRecipeMixin {

    @ModifyVariable(
            method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z",
            at = @At(value = "STORE"),
            remap = false
    )
    private static HeatLevel apply$getHeatLevel(HeatLevel heat, @Local(argsOnly = true) BasinBlockEntity basin) {
        Level level = Objects.requireNonNull(basin.getLevel());
        BlockPos pos = basin.getBlockPos().below(1);
        BlockEntity entity = level.getBlockEntity(pos);

        HeatLevel newHeat = HeatLevel.NONE;
        if (entity instanceof IHeatExchangingTE heatTE) {
            IHeatExchangerLogic logic = heatTE.getHeatExchanger(Direction.UP);
            if (logic != null)
                newHeat = HeatConverterBlockEntity.getHeatLevel(logic.getTemperature());
        }

        if (newHeat.isAtLeast(heat))
            heat = newHeat;
        return heat;
    }

}
