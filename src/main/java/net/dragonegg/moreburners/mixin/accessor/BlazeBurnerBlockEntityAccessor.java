package net.dragonegg.moreburners.mixin.accessor;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlazeBurnerBlockEntity.class)
public interface BlazeBurnerBlockEntityAccessor {

    @Accessor("activeFuel")
    void setActiveFuel(BlazeBurnerBlockEntity.FuelType fuel);

    @Accessor("remainingBurnTime")
    void setRemainingBurnTime(int time);

}
