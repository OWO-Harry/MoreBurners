package net.dragonegg.moreburners.content.block.entity;

import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.block.AtmosphericBellowsBlock;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.util.DecimalFormats;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.compat.embers.EmbersCompat;
import net.dragonegg.moreburners.config.ClientConfig;
import net.dragonegg.moreburners.config.CommonConfig;
import net.dragonegg.moreburners.content.block.EmberBurnerBlock;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EmberBurnerBlockEntity extends BaseBurnerBlockEntity implements IExtraDialInformation {

    public static final double MAX_CAPACITY = CommonConfig.EMBER_BURNER_MAX_CAPACITY.get();
    public static final double EMBER_COST = CommonConfig.EMBER_BURNER_EMBER_COST.get();
    public static final double MAX_HEAT = CommonConfig.EMBER_BURNER_MAX_HEAT.get();
    public static final double MAX_HEAT_1 = CommonConfig.EMBER_BURNER_MAX_HEAT_BELLOWS_1.get();
    public static final double MAX_HEAT_2 = CommonConfig.EMBER_BURNER_MAX_HEAT_BELLOWS_2.get();
    public static final double HEATING_RATE = CommonConfig.EMBER_BURNER_HEATING_RATE.get();
    public static final double COOLING_RATE = CommonConfig.EMBER_BURNER_COOLING_RATE.get();
    public static final double RATE_MULTIPLIER = CommonConfig.EMBER_BURNER_RATE_BELLOWS_MULTIPLIER.get();

    public static final List<? extends String> BLOCK_COVERED = ClientConfig.EMBER_BURNER_BLOCK_COVERED.get();

    protected static Random random = new Random();

    public IEmberCapability capability = new DefaultEmberCapability() {
        public void onContentsChanged() {
            super.onContentsChanged();
            EmberBurnerBlockEntity.this.setChanged();
        }

    };

    public EmberBurnerBlockEntity(BlockPos pos, BlockState state) {
        super(EmbersCompat.EMBER_BURNER_ENTITY.get(), pos, state);
        this.capability.setEmberCapacity(MAX_CAPACITY);
        this.heat = 0.0;
        this.max_heat = MAX_HEAT;
        this.ticksExisted = 0;
    }

    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.capability.deserializeNBT(nbt);
    }

    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.capability.writeToNBT(nbt);
    }

    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.capability.writeToNBT(nbt);
        return nbt;
    }

    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return !this.remove && cap == EmbersCapabilities.EMBER_CAPABILITY ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        ++this.ticksExisted;
        if (level.isClientSide) {
            spawnParticles(this.getHeatLevelFromBlock());
        }else {

            double heatingRate = HEATING_RATE;
            double coolingRate = COOLING_RATE;
            if(isBellowsAdjacent()) {
                this.max_heat = MAX_HEAT_1;
                heatingRate *= RATE_MULTIPLIER;
                coolingRate *= RATE_MULTIPLIER;
            }else if(isBellowsOneBlockAway()) {
                this.max_heat = MAX_HEAT_2;
            }else{
                this.max_heat = MAX_HEAT;
            }

            super.tick(level,pos,state);

            double prevHeat = this.heat;
            if (this.ticksExisted % 20 == 0) {
                for(BlockPos burnerPos : getAdjacentEmberBurners()) {
                    EmberBurnerBlockEntity adjBurner = ((EmberBurnerBlockEntity) this.level.getBlockEntity(burnerPos));
                    double cap1 = this.capability.getEmber();
                    double cap2 = adjBurner.capability.getEmber();
                    double transmit = Math.abs(cap1-cap2)/2.0;
                    if(transmit<0.5){
                        continue;
                    }
                    if(cap1>cap2) {
                        this.capability.removeAmount(transmit,true);
                        adjBurner.capability.addAmount(transmit, true);
                    }else{
                        adjBurner.capability.removeAmount(transmit, true);
                        this.capability.addAmount(transmit, true);
                    }
                }
            }
            if (this.capability.getEmber() >= EMBER_COST) {
                this.capability.removeAmount(EMBER_COST, true);
                if (this.ticksExisted % 20 == 0) {
                    this.heat += heatingRate;
                }
            }else if (this.ticksExisted % 20 == 0) {
                this.heat -= coolingRate;
            }
            this.heat = Mth.clamp(this.heat, 0.0, max_heat);
            if (this.heat != prevHeat) {
                this.setChanged();
                this.updateBlockState();
            }

        }

    }

    public List<BlockPos> getAdjacentEmberBurners() {
        if(this.level==null){
            return null;
        }
        List<BlockPos> poss = new ArrayList<>();
        for(Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos pos = this.worldPosition.relative(dir);
            if (this.level.getBlockState(pos).getBlock() instanceof EmberBurnerBlock) {
                poss.add(pos);
            }
        }
        return poss;
    }

    public boolean isBellowsOneBlockAway() {
        if(this.level==null){
            return false;
        }
        for(int x=-2;x<=2;x++){
            for(int z=-2;z<=2;z++){
                if(Math.abs(x)+Math.abs(z)==2){
                    BlockPos pos = this.worldPosition.offset(x,0,z);
                    BlockState state = this.level.getBlockState(pos);
                    if (state.getBlock() instanceof AtmosphericBellowsBlock) {
                        BlockPos facedPos = pos.relative(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                        if(getAdjacentEmberBurners().contains(facedPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isBellowsAdjacent() {
        if(this.level==null){
            return false;
        }
        for( Direction direction : Direction.Plane.HORIZONTAL ) {
            BlockPos pos = this.worldPosition.relative(direction);
            BlockState state = this.level.getBlockState(pos);
            if (state.getBlock() instanceof AtmosphericBellowsBlock) {
                if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) == direction.getOpposite()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void spawnParticles(BlazeBurnerBlock.HeatLevel heatLevel) {
        if (this.level != null) {
            Block above = this.level.getBlockState(this.worldPosition.above()).getBlock();
            if(!isCovered(above)) {
                if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {

                    Color color = new Color(255, 64, 16);
                    int particleCount = (int)(1.0F + (float)Math.sqrt(this.heat));
                    if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.SEETHING)) {
                        color = new Color(color.getBlue(), color.getGreen(), color.getRed());
                    }

                    GlowParticleOptions options = new GlowParticleOptions(new Vector3f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F),2.0f);
                    BlockPos pos = this.worldPosition;
                    for(int i = 0; i < particleCount; ++i) {
                        level.addParticle(options, pos.getX() + 0.2 + random.nextFloat()*0.6, pos.getY() + 1, pos.getZ() + 0.2 + random.nextFloat() * 0.6, (Math.random() * 2.0 - 1.0) * 0.2, 0.0, (Math.random() * 2.0 - 1.0) * 0.2);
                    }
                }
            }
        }
    }

    public static boolean isCovered(Block block) {
        Iterator var1 = ((List)BLOCK_COVERED).iterator();
        String pass;
        String[] ids = block.getDescriptionId().split("\\.");
        String id = ids[1] + ":" + ids[2];
        do {
            if (!var1.hasNext()) {
                return false;
            }
            pass = (String)var1.next();
        } while(!pass.equals(id));

        return true;
    }

    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        if(dialType == "ember") {
            DecimalFormat heatFormat = DecimalFormats.getDecimalFormat("embers.decimal_format.heat");
            information.add(Component.translatable("embers.tooltip.dial.heat", heatFormat.format(this.heat), heatFormat.format(this.max_heat)));
        }

    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip,isPlayerSneaking);

        tooltip.add(Components.immutableEmpty());
        forGoggles(tooltip, Lang.builder(MoreBurners.MODID).translate("burner.ember.title").style(ChatFormatting.GRAY), 0);
        LangBuilder builder = Lang.builder(MoreBurners.MODID).text(LangNumberFormat.format(EMBER_COST))
                .style(ChatFormatting.AQUA)
                .space()
                .add(Lang.builder(MoreBurners.MODID).translate("burner.per_tick").style(ChatFormatting.DARK_GRAY));
        forGoggles(tooltip, builder, 1);
        return added;
    }

}
