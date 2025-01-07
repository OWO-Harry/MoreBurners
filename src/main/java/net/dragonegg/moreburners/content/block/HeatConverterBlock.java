package net.dragonegg.moreburners.content.block;

import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.block.IPneumaticWrenchable;
import me.desht.pneumaticcraft.common.advancements.AdvancementTriggers;
import me.desht.pneumaticcraft.common.block.IBlockComparatorSupport;
import me.desht.pneumaticcraft.common.block.entity.AbstractPneumaticCraftBlockEntity;
import me.desht.pneumaticcraft.common.core.ModBlocks;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.content.block.entity.HeatConverterBlockEntity;
import net.dragonegg.moreburners.util.BurnerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

public class HeatConverterBlock extends BaseBurnerBlock implements IBlockComparatorSupport, IPneumaticWrenchable {

    public HeatConverterBlock() {
        super(ModBlocks.defaultProps());
    }

    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new HeatConverterBlockEntity(pPos, pState);
    }

    public int getTintColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) {
        if (world != null && pos != null) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof HeatConverterBlockEntity heatConverter) {
                if(tintIndex==0) {
                    return heatConverter.getColorForTintIndex(tintIndex).getRGB();
                }else{
                    return BurnerUtil.getColor(state.getValue(HEAT_LEVEL));
                }
            }
            return 0xffffff;
        } else {
            return -1;
        }
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof HeatConverterBlockEntity he) {
            he.initHeatExchangersOnPlacement(world, pos);
        }
    }

    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!world.isClientSide()) {
            PneumaticCraftUtils.getTileEntityAt(world, pos, HeatConverterBlockEntity.class).ifPresent((pncBE) -> pncBE.onNeighborBlockUpdate(fromPos));
        }
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, PneumaticCraftCompat.HEAT_CONVERTER_ENTITY.get(),
                (level1, pos, state1, entity) -> entity.tick(level1, pos, state1)
        );
    }

    @Override
    public boolean onWrenched(Level level, Player player, BlockPos pos, Direction direction, InteractionHand interactionHand) {
        if (player != null && player.isShiftKeyDown()) {
            BlockEntity te = level.getBlockEntity(pos);
            if (!player.isCreative()) {
                Block.dropResources(level.getBlockState(pos), level, pos, te);
            }
            removeBlockSneakWrenched(level, pos);
            return true;
        }
        return false;
    }

    static void removeBlockSneakWrenched(Level world, BlockPos pos) {
        if (!world.isClientSide()) {
            world.removeBlock(pos, false);
            PneumaticRegistry.getInstance().getMiscHelpers().forceClientShapeRecalculation(world, pos);
        }

    }

    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @javax.annotation.Nullable BlockEntity te, ItemStack stack) {
        if (player instanceof ServerPlayer sp) {
            if (!(player instanceof FakePlayer) && te instanceof AbstractPneumaticCraftBlockEntity base) {
                if (!base.shouldPreserveStateOnBreak()) {
                    AdvancementTriggers.MACHINE_VANDAL.trigger(sp);
                }
            }
        }

        super.playerDestroy(worldIn, player, pos, state, te, stack);
    }

}
