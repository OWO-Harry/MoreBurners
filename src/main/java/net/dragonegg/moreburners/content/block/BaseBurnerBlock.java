package net.dragonegg.moreburners.content.block;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.utility.Iterate;
import net.dragonegg.moreburners.content.block.entity.BaseBurnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public abstract class BaseBurnerBlock extends BaseEntityBlock implements IWrenchable {

    public static final EnumProperty<BlazeBurnerBlock.HeatLevel> HEAT_LEVEL = EnumProperty.create("blaze", BlazeBurnerBlock.HeatLevel.class);

    protected BaseBurnerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.NONE));
    }

    public RenderShape getRenderShape(BlockState state){
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HEAT_LEVEL);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isClientSide) {
            BlockEntity blockEntity = world.getBlockEntity(pos.above());
            if (blockEntity instanceof BasinBlockEntity) {
                BasinBlockEntity basin = (BasinBlockEntity)blockEntity;
                basin.notifyChangeOfContents();
            }
        }
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {

        float damage = 0;
        boolean giveFire = false;
        int time = 0;
        switch (state.getValue(HEAT_LEVEL)) {
            case SMOULDERING -> damage = 0.5f;
            case FADING -> {
                damage = 1.0f;
                giveFire = true;
                time = 6;
            }
            case KINDLED -> {
                damage = 2.0f;
                giveFire = true;
                time = 8;
            }
            case SEETHING -> {
                damage = 4.0f;
                giveFire = true;
                time = 300;
            }
        }

        if (!entity.isSteppingCarefully() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            entity.hurt(level.damageSources().hotFloor(), damage);
        }
        if (giveFire && !entity.fireImmune()) {
            entity.setSecondsOnFire(time);
        }

        super.stepOn(level, pos, state, entity);
    }

    public BlockState getState(BlazeBurnerBlock.HeatLevel level) {
        return this.defaultBlockState().setValue(HEAT_LEVEL, level);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

}
