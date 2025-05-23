/*
package net.dragonegg.moreburners.compat.botania;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity.FuelType;
import net.dragonegg.moreburners.config.CommonConfig;
import net.dragonegg.moreburners.mixin.accessor.BlazeBurnerBlockEntityAccessor;
import vazkii.botania.api.block.ExoflameHeatable;

import static com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity.MAX_HEAT_CAPACITY;

public class BlazeBurnerExoflameHeatable implements ExoflameHeatable {

    public static int BOOST_RATE = CommonConfig.EXOFLAME_BOOST_RATE.get();
    public static int SEETHING_BOOST_RATE = CommonConfig.EXOFLAME_SEETHING_BOOST_RATE.get();

    private final BlazeBurnerBlockEntity burner;

    public BlazeBurnerExoflameHeatable(BlazeBurnerBlockEntity burner) {
        this.burner = burner;
    }

    public boolean canSmelt() {
        if(this.burner.isCreative()) {
            return false;
        }
        FuelType fuel = this.burner.getActiveFuel();
        return switch (fuel){
            case NONE -> true;
            case NORMAL, SPECIAL -> this.getBurnTime() < MAX_HEAT_CAPACITY;
        };
    }

    public int getBurnTime() {
        return this.burner.getRemainingBurnTime();
    }

    public void boostBurnTime() {
        FuelType fuel = this.burner.getActiveFuel();
        if(fuel == FuelType.NONE) {
            ((BlazeBurnerBlockEntityAccessor) this.burner).setActiveFuel(FuelType.NORMAL);
            fuel = FuelType.NORMAL;
        }

        int rate = fuel == FuelType.NORMAL? BOOST_RATE : SEETHING_BOOST_RATE;
        ((BlazeBurnerBlockEntityAccessor) this.burner).setRemainingBurnTime(this.getBurnTime() + rate);
        this.burner.updateBlockState();
    }

    public void boostCookTime() {
    }

}
*/
