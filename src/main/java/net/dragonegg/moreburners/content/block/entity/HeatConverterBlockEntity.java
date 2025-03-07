package net.dragonegg.moreburners.content.block.entity;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import joptsimple.internal.Strings;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.client.util.TintColor;
import me.desht.pneumaticcraft.common.block.entity.*;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
import me.desht.pneumaticcraft.common.heat.SyncedTemperature;
import me.desht.pneumaticcraft.common.network.*;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.lang.LangBuilder;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.config.ClientConfig;
import net.dragonegg.moreburners.config.CommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.createmod.catnip.lang.LangBuilder.DEFAULT_SPACE_WIDTH;


public class HeatConverterBlockEntity extends AbstractTickingBlockEntity implements IComparatorSupport, IHeatTinted, IHeatExchangingTE, IHaveGoggleInformation {

    public static final double SEETHING_TEMP = CommonConfig.HEAT_CONVERTER_SEETHING_TEMP.get();
    public static final double KINDLED_TEMP = CommonConfig.HEAT_CONVERTER_KINDLED_TEMP.get();
    public static final double FADING_TEMP = CommonConfig.HEAT_CONVERTER_FADING_TEMP.get();
    public static final double SMOULDERING_TEMP = CommonConfig.HEAT_CONVERTER_SMOULDERING_TEMP.get();
    public static final double TEMP_COST = CommonConfig.HEAT_CONVERTER_TEMP_COST.get();
    public static final int BAR_LENGTH = ClientConfig.HEAT_BAR_LENGTH.get();

    public static final double MAX_TEMP = 2273.0;

    protected final IHeatExchangerLogic heatExchanger;
    private final LazyOptional<IHeatExchangerLogic> heatCap;
    private int comparatorOutput;
    @DescSynced
    protected final SyncedTemperature syncedTemperature;

    public HeatConverterBlockEntity(BlockPos pos, BlockState state) {
        this(PneumaticCraftCompat.HEAT_CONVERTER_ENTITY.get(), pos, state);
    }

    HeatConverterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.heatExchanger = PneumaticRegistry.getInstance().getHeatRegistry().makeHeatExchangerLogic();
        this.heatCap = LazyOptional.of(() -> this.heatExchanger);
        this.comparatorOutput = 0;
        this.syncedTemperature = new SyncedTemperature(this.heatExchanger);
        this.heatExchanger.setThermalCapacity(10.0F);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(level.isClientSide()) {
            this.tickClient();
        }else{
            this.tickServer();
        }
    }

    public void tickServer() {
        super.tickServer();
        this.syncedTemperature.tick();
        int newComparatorOutput = HeatUtil.getComparatorOutput((int)this.heatExchanger.getTemperature());
        if (this.comparatorOutput != newComparatorOutput) {
            this.comparatorOutput = newComparatorOutput;
            this.nonNullLevel().updateNeighbourForOutputSignal(this.getBlockPos(), this.getBlockState().getBlock());
        }
        if(this.heatExchanger.getTemperature() >= this.heatExchanger.getAmbientTemperature() + TEMP_COST) {
            this.heatExchanger.addHeat(-TEMP_COST);
        }
        this.updateBlockState();
    }

    public void updateBlockState() {
        this.setBlockHeat(this.getHeatLevel());
    }

    protected void setBlockHeat(BlazeBurnerBlock.HeatLevel heat) {
        BlazeBurnerBlock.HeatLevel inBlockState = this.getHeatLevelFromBlock();
        if (inBlockState != heat) {
            assert this.level != null;
            this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(BlazeBurnerBlock.HEAT_LEVEL, heat));
            this.setChanged();
        }
    }

    public BlazeBurnerBlock.HeatLevel getHeatLevelFromBlock() {
        return BlazeBurnerBlock.getHeatLevelOf(this.getBlockState());
    }

    protected BlazeBurnerBlock.HeatLevel getHeatLevel() {

        double temp = this.heatExchanger.getTemperature() - 273.0F;

        BlazeBurnerBlock.HeatLevel level = BlazeBurnerBlock.HeatLevel.NONE;
        if(temp>=SEETHING_TEMP){
            level = BlazeBurnerBlock.HeatLevel.SEETHING;
        }else if(temp>=KINDLED_TEMP){
            level = BlazeBurnerBlock.HeatLevel.KINDLED;
        }else if(temp>=FADING_TEMP){
            level = BlazeBurnerBlock.HeatLevel.FADING;
        }else if(temp>=SMOULDERING_TEMP){
            level = BlazeBurnerBlock.HeatLevel.SMOULDERING;
        }

        return level;
    }

    public int getComparatorValue() {
        return this.comparatorOutput;
    }

    public IHeatExchangerLogic getHeatExchanger(Direction dir) {
        return dir != Direction.UP? this.heatExchanger : null;
    }

    public @NotNull LazyOptional<IHeatExchangerLogic> getHeatCap(Direction dir) {
        return dir != Direction.UP? this.heatCap : LazyOptional.empty();
    }

    public IItemHandler getPrimaryInventory() {
        return null;
    }

    protected boolean shouldRerenderChunkOnDescUpdate() {
        return true;
    }

    @Override
    public TintColor getColorForTintIndex(int i) {
        return HeatUtil.getColourForTemperature(this.syncedTemperature.getSyncedTemp());
    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        BlazeBurnerBlock.HeatLevel level = this.getHeatLevelFromBlock();
        ChatFormatting formatting = switch (level) {
            case NONE,SMOULDERING -> ChatFormatting.WHITE;
            case FADING,KINDLED -> ChatFormatting.GOLD;
            case SEETHING -> ChatFormatting.BLUE;
        };
        forGoggles(tooltip, Lang.builder(MoreBurners.MODID).translate("burner.status.title",
                Component.translatable(MoreBurners.MODID + ".burner.status." + level.name().toLowerCase()).withStyle(formatting)), 0);
        forGoggles(tooltip, Lang.builder(MoreBurners.MODID).add(getHeatComponent(true)), 1);
        return true;

    }

    public void forGoggles(List<? super MutableComponent> tooltip, LangBuilder builder, int indents) {
        tooltip.add(Lang.builder(MoreBurners.MODID)
                .text(Strings.repeat(' ', getIndents(Minecraft.getInstance().font, 4 + indents)))
                .add(builder)
                .component());
    }

    static int getIndents(Font font, int defaultIndents) {
        int spaceWidth = font.width(" ");
        if (DEFAULT_SPACE_WIDTH == spaceWidth) {
            return defaultIndents;
        }

        return Mth.ceil(DEFAULT_SPACE_WIDTH * defaultIndents / spaceWidth);
    }

    public MutableComponent getHeatComponent(boolean forGoggles) {
        int level = (int) (this.syncedTemperature.getSyncedTemp() * BAR_LENGTH / MAX_TEMP);
        return componentHelper(level, forGoggles);
    }

    private MutableComponent componentHelper(int level, boolean forGoggles) {
        MutableComponent base =
                Component.empty()
                        .append(bars(level, ChatFormatting.DARK_GREEN))
                        .append(bars(BAR_LENGTH-level, ChatFormatting.DARK_RED));

        if (!forGoggles)
            return base;

        return Component.translatable(MoreBurners.MODID + ".burner.status.heat")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.translatable(MoreBurners.MODID + ".burner.status.dots")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(base);
    }

    private MutableComponent bars(int level, ChatFormatting format) {
        return Component.literal(Strings.repeat('|', level)).withStyle(format);
    }

}
