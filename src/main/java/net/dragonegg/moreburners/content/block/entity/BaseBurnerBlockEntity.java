package net.dragonegg.moreburners.content.block.entity;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.foundation.utility.*;
import joptsimple.internal.Strings;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.config.ClientConfig;
import net.dragonegg.moreburners.config.CommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.simibubi.create.foundation.utility.LangBuilder.DEFAULT_SPACE_WIDTH;

public abstract class BaseBurnerBlockEntity extends BlockEntity implements IHaveGoggleInformation {

    public static final double SEETHING_HEAT = CommonConfig.SEETHING_HEAT.get();
    public static final double KINDLED_HEAT = CommonConfig.KINDLED_HEAT.get();
    public static final double FADING_HEAT = CommonConfig.FADING_HEAT.get();
    public static final double SMOULDERING_HEAT = CommonConfig.SMOULDERING_HEAT.get();
    public static final int BAR_LENGTH = ClientConfig.HEAT_BAR_LENGTH.get();

    public double heat;
    public double max_heat;
    protected int ticksExisted;
    protected int redstoneStrength;

    public BaseBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.redstoneStrength = 0;
    }

    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.heat = nbt.getDouble("heat");
        this.max_heat = nbt.getDouble("max_heat");
        this.redstoneStrength = nbt.getInt("redstone_strength");
    }

    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putDouble("heat", this.heat);
        nbt.putDouble("max_heat", this.max_heat);
        nbt.putInt("redstone_strength", this.redstoneStrength);
    }

    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putDouble("heat", this.heat);
        nbt.putDouble("max_heat", this.max_heat);
        nbt.putInt("redstone_strength", this.redstoneStrength);
        return nbt;
    }

    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setChanged() {
        super.setChanged();
        if (this.level instanceof ServerLevel) {
            ((ServerLevel)this.level).getChunkSource().blockChanged(this.worldPosition);
        }
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        this.updateRedstoneStrength(getPower(level, pos));
        if(this.redstoneStrength!=0) {
            this.max_heat *= (double) this.redstoneStrength /16;
        }
    }

    public BlazeBurnerBlock.HeatLevel getHeatLevelFromBlock() {
        return BlazeBurnerBlock.getHeatLevelOf(this.getBlockState());
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

    protected BlazeBurnerBlock.HeatLevel getHeatLevel() {

        BlazeBurnerBlock.HeatLevel level = BlazeBurnerBlock.HeatLevel.NONE;
        if(this.heat>=SEETHING_HEAT){
            level = BlazeBurnerBlock.HeatLevel.SEETHING;
        }else if(this.heat>=KINDLED_HEAT){
            level = BlazeBurnerBlock.HeatLevel.KINDLED;
        }else if(this.heat>=FADING_HEAT){
            level = BlazeBurnerBlock.HeatLevel.FADING;
        }else if(this.heat>=SMOULDERING_HEAT){
            level = BlazeBurnerBlock.HeatLevel.SMOULDERING;
        }

        return level;
    }

    public void updateRedstoneStrength(int signalStrength) {
        this.redstoneStrength = signalStrength;
        this.setChanged();
    }

    public int getPower(Level worldIn, BlockPos pos) {
        int power = 0;
        for (Direction direction : Iterate.directions)
            power = Math.max(worldIn.getSignal(pos.relative(direction), direction), power);
        for (Direction direction : Iterate.directions)
            power = Math.max(worldIn.getSignal(pos.relative(direction), Direction.UP), power);
        return power;
    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        ChatFormatting formatting = switch (getHeatLevel()) {
            case NONE,SMOULDERING -> ChatFormatting.WHITE;
            case FADING,KINDLED -> ChatFormatting.GOLD;
            case SEETHING -> ChatFormatting.BLUE;
        };
        forGoggles(tooltip, Lang.builder(MoreBurners.MODID).translate("burner.status.title",
                Components.translatable(MoreBurners.MODID + ".burner.status." + getHeatLevel().name().toLowerCase()).withStyle(formatting)), 0);
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
        int level = (int) (this.heat*BAR_LENGTH/this.max_heat);
        return componentHelper(level, forGoggles);
    }

    private MutableComponent componentHelper(int level, boolean forGoggles) {
        MutableComponent base =
                Components.empty()
                        .append(bars(level, ChatFormatting.DARK_GREEN))
                        .append(bars(BAR_LENGTH-level, ChatFormatting.DARK_RED));

        if (!forGoggles)
            return base;

        return Components.translatable(MoreBurners.MODID + ".burner.status.heat")
                .withStyle(ChatFormatting.GRAY)
                .append(Components.translatable(MoreBurners.MODID + ".burner.status.dots")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(base);
    }

    private MutableComponent bars(int level, ChatFormatting format) {
        return Components.literal(Strings.repeat('|', level)).withStyle(format);
    }
}
