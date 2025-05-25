package net.dragonegg.moreburners.compat.pneumaticcraft;

import me.desht.pneumaticcraft.api.heat.HeatBehaviour;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.config.CommonConfig;
import net.minecraft.resources.ResourceLocation;

public class HeatBehaviourBasin extends HeatBehaviour {

    static final ResourceLocation ID = MoreBurners.RL("basin");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public boolean isApplicable() {
        return PneumaticCraftCompat.checkBasin(getBlockState().getBlock(), getDirection(), getCachedTileEntity());
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
