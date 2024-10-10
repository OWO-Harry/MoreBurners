package net.dragonegg.moreburners.content.block;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.utility.Iterate;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.content.block.entity.BaseBurnerBlockEntity;
import net.dragonegg.moreburners.content.block.entity.ElectricBurnerBlockEntity;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ElectricBurnerBlock extends BaseBurnerBlock{

    public static final BooleanProperty UPGRADED = BooleanProperty.create("upgraded");


    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 0, 2,14, 3, 14),
            Block.box(1, 3, 1,15, 14, 15),
            Block.box(0, 6, 0,16, 12, 16),
            Block.box(2, 14, 2,14, 15, 14),
            Block.box(0, 12, 0,16, 16, 1),
            Block.box(0, 12, 1,1, 16, 15),
            Block.box(15, 12, 1,16, 16, 15),
            Block.box(0, 12, 15,16, 16, 16),
            Block.box(1, 14, 1,15, 16, 15)
    );
    public ElectricBurnerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(UPGRADED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UPGRADED);
    }

    public BlockState getState(BlazeBurnerBlock.HeatLevel level, Boolean upgraded) {
        return super.getState(level).setValue(UPGRADED, upgraded);
    }

    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if(!state.getValue(UPGRADED)) {
            return super.onSneakWrenched(state, context);
        }
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        level.levelEvent(2001, pos, Block.getId(state));
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof ElectricBurnerBlockEntity ebbe) {
            ebbe.setUpgrade(false);
            playRemoveSound(level,pos);
            if (player != null && !player.isCreative()) {
                ItemStack returned = ItemRegistry.HEAT_UPGRADE.get().getDefaultInstance();
                player.getInventory().placeItemBackInInventory(returned);
            }
        }
        return InteractionResult.SUCCESS;
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElectricBurnerBlockEntity(pos,state);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {

        return createTickerHelper(type, BlockRegistry.ELECTRIC_BURNER_ENTITY.get(),
                (level1, pos, state1, entity) -> entity.tick(level1, pos, state1)
        );
    }

}
