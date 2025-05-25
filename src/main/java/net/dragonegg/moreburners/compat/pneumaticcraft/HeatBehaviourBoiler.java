package net.dragonegg.moreburners.compat.pneumaticcraft;

import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import me.desht.pneumaticcraft.api.heat.HeatBehaviour;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class HeatBehaviourBoiler extends HeatBehaviour {

    static final ResourceLocation ID = MoreBurners.RL("boiler");

    public HeatBehaviour initialize(IHeatExchangerLogic logic, Level level, BlockPos pos, Direction dir) {
        super.initialize(logic, level, pos, dir);
        if (isApplicable()) {
            logic.addTemperatureListener((prev, curr) -> {
                BoilerData boiler = ((FluidTankBlockEntity) getCachedTileEntity()).getControllerBE().boiler;
                boiler.needsHeatLevelUpdate = true;
            });
        }
        return this;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public boolean isApplicable() {
        return PneumaticCraftCompat.checkBoiler(getBlockState().getBlock(), getDirection(), getCachedTileEntity());
    }

    @Override
    public void tick() {
        double cost = CommonConfig.PNE_TEMP_COST.get();
        IHeatExchangerLogic logic = getHeatExchanger();
        if (logic.getTemperature() >= logic.getAmbientTemperature() + cost) {
            logic.addHeat(-cost);
        }
    }

}
