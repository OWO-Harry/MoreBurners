package net.dragonegg.moreburners.content.block.entity;

import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.lang.LangNumberFormat;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.config.CommonConfig;
import net.dragonegg.moreburners.content.block.ElectricBurnerBlock;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
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
    public EnergyStorage energy;
    public double energy_cost;
    public boolean upgraded;

    public ElectricBurnerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.ELECTRIC_BURNER_ENTITY.get(), pos, state);
        this.max_heat = MAX_HEAT;
        this.energy_cost = ENERGY_COST;
        this.upgraded = false;
        this.energy = new EnergyStorage(MAX_ENERGY_CAP);
    }

    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.energy.deserializeNBT(registries, IntTag.valueOf(nbt.getInt("energy")));
        this.upgraded = nbt.getBoolean("upgraded");
    }

    public void saveAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putInt("energy", this.energy.getEnergyStored());
        nbt.putBoolean("upgraded", this.upgraded);
    }

    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag nbt = super.getUpdateTag(registries);
        nbt.putInt("energy", this.energy.getEnergyStored());
        nbt.putBoolean("upgraded", this.upgraded);
        return nbt;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                BlockRegistry.ELECTRIC_BURNER_ENTITY.get(),
                (be, context) -> be.energy
        );
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
        if (!level.isClientSide()) {

            double prevHeat = this.heat;
            if (this.energy.getEnergyStored() > (int)this.energy_cost) {
                this.energy.extractEnergy((int)this.energy_cost, false);
            } else {
                this.canWork = false;
            }
            if (this.ticksExisted % 20 == 0) {
                if (this.canWork) {
                    this.heat += HEATING_RATE;
                } else {
                    this.heat -= COOLING_RATE;
                }
                this.canWork = true;
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

        tooltip.add(Component.empty());
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
