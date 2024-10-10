package net.dragonegg.moreburners.content.block;

import net.dragonegg.moreburners.compat.embers.EmbersCompat;
import net.dragonegg.moreburners.content.block.entity.EmberBurnerBlockEntity;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class EmberBurnerBlock extends BaseBurnerBlock {

    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(1.0,7.0,1.0,15.0,10.0,15.0),
            Block.box(2.0,0.0,2.0,14.0,3.0,14.0),
            Block.box(0.0,3.0,0.0,16.0,7.0,16.0),
            Block.box(0.0,10.0,0.0,16.0,15.0,16.0),
            Block.box(1.0, 15.0, 1.0,15.0, 16.0, 15.0),
            Block.box(0.0, 6.0, 6.0,16.0, 10.0, 10.0),
            Block.box(6.0, 6.0, 0.0,10.0, 10.0, 16.0)
    );

    public EmberBurnerBlock(Properties properties) {
        super(properties);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EmberBurnerBlockEntity(pos,state);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {

        return createTickerHelper(type, EmbersCompat.EMBER_BURNER_ENTITY.get(),
                (level1, pos, state1, entity) -> entity.tick(level1, pos, state1)
        );
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }


}
