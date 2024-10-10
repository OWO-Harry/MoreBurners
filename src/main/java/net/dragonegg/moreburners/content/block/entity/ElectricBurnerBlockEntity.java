package net.dragonegg.moreburners.content.block.entity;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.config.CommonConfig;
import net.dragonegg.moreburners.content.block.ElectricBurnerBlock;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ElectricBurnerBlockEntity extends BaseBurnerBlockEntity {

    public static final int MAX_ENERGY_CAP = CommonConfig.ELECTRIC_BURNER_MAX_CAPACITY.get();
    public static final int ENERGY_COST = CommonConfig.ELECTRIC_BURNER_ENERGY_COST.get();
    public static final double ENERGY_MULTIPLIER_1 = CommonConfig.ELECTRIC_BURNER_ENERGY_MULTIPLIER_1.get();
    public static final double ENERGY_MULTIPLIER_2 = CommonConfig.ELECTRIC_BURNER_ENERGY_MULTIPLIER_2.get();
    public static final double MAX_HEAT = CommonConfig.ELECTRIC_BURNER_MAX_HEAT.get();
    public static final double UPGRADED_MAX_HEAT = CommonConfig.ELECTRIC_BURNER_UPGRADED_MAX_HEAT.get();
    public static final double HEATING_RATE = CommonConfig.ELECTRIC_BURNER_HEATING_RATE.get();
    public static final double COOLING_RATE = CommonConfig.ELECTRIC_BURNER_COOLING_RATE.get();
    public EnergyStorage energy = new EnergyStorage(MAX_ENERGY_CAP);
    public double energy_cost;
    public boolean upgraded;

    public ElectricBurnerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.ELECTRIC_BURNER_ENTITY.get(), pos, state);
        this.heat = 0.0;
        this.max_heat = MAX_HEAT;
        this.ticksExisted = 0;
        this.energy_cost = ENERGY_COST;
        this.upgraded = false;
    }

    private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> {
        return energy;
    });

    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.energy.deserializeNBT(IntTag.valueOf(nbt.getInt("energy")));
        this.upgraded = nbt.getBoolean("upgraded");
    }

    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("energy", this.energy.getEnergyStored());
        nbt.putBoolean("upgraded", this.upgraded);
    }

    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putInt("energy", this.energy.getEnergyStored());
        nbt.putBoolean("upgraded", this.upgraded);
        return nbt;
    }

    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return !this.remove && cap == ForgeCapabilities.ENERGY ? this.energyCap.cast() : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyCap.invalidate();
    }

    public void setUpgrade(boolean upgraded) {
        this.upgraded = upgraded;
        this.updateBlockState();
    }

    public void updateBlockState() {
        super.updateBlockState();
        this.setBlockUpgraded(this.upgraded);
    }

    public void setBlockUpgraded(boolean upgraded) {
        boolean state = this.getUpgradedFromBlock();
        if (state != upgraded) {
            assert this.level != null;
            this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(ElectricBurnerBlock.UPGRADED, upgraded));
            this.setChanged();
        }
    }

    public boolean getUpgradedFromBlock() {
        BlockState state = this.getBlockState();
        return state.hasProperty(ElectricBurnerBlock.UPGRADED) ? state.getValue(ElectricBurnerBlock.UPGRADED) : false;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        ++this.ticksExisted;

        this.max_heat = upgraded ? UPGRADED_MAX_HEAT : MAX_HEAT;
        super.tick(level,pos,state);

        this.energy_cost = ENERGY_COST * switch (getHeatLevelFromBlock()) {
            case NONE, SMOULDERING -> 1;
            case FADING, KINDLED -> ENERGY_MULTIPLIER_1;
            case SEETHING -> ENERGY_MULTIPLIER_2;
        };
        if (level.isClientSide) {

        }else {

            double prevHeat = this.heat;
            if (this.energy.getEnergyStored() > (int)this.energy_cost) {
                this.energy.extractEnergy((int)this.energy_cost, false);
                if (this.ticksExisted % 20 == 0) {
                    this.heat += HEATING_RATE;
                }
            }else if (this.ticksExisted % 20 == 0) {
                this.heat -= COOLING_RATE;
            }
            this.heat = Mth.clamp(this.heat, 0.0, max_heat);
            if (this.heat != prevHeat) {
                this.setChanged();
                this.updateBlockState();
            }

        }

    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip,isPlayerSneaking);
        if(getUpgradedFromBlock()) {
            forGoggles(tooltip, Lang.builder(MoreBurners.MODID).translate("burner.status.upgraded").style(ChatFormatting.BLUE), 1);
        }

        tooltip.add(Components.immutableEmpty());
        forGoggles(tooltip, Lang.builder(MoreBurners.MODID).translate("burner.energy.title").style(ChatFormatting.GRAY), 0);
        LangBuilder builder = Lang.builder(MoreBurners.MODID).text(LangNumberFormat.format((int)this.energy_cost))
                .translate("burner.energy.unit")
                .style(ChatFormatting.AQUA)
                .space()
                .add(Lang.builder(MoreBurners.MODID).translate("burner.per_tick").style(ChatFormatting.DARK_GRAY));
        forGoggles(tooltip, builder, 1);
        return added;
    }

}
