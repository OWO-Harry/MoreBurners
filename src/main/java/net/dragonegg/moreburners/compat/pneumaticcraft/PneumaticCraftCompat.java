package net.dragonegg.moreburners.compat.pneumaticcraft;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import me.desht.pneumaticcraft.common.heat.behaviour.HeatBehaviourManager;
import net.dragonegg.moreburners.content.block.HeatConverterBlock;
import net.dragonegg.moreburners.content.block.entity.HeatConverterBlockEntity;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.registry.ItemRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Supplier;

public class PneumaticCraftCompat {

    public static final DeferredBlock<HeatConverterBlock> HEAT_CONVERTER;

    public static final Supplier<BlockEntityType<HeatConverterBlockEntity>> HEAT_CONVERTER_ENTITY;

    public static final DeferredItem<BlockItem> HEAT_CONVERTER_ITEM;
    public static final DeferredItem<Item> CONVERTER_COVER;

    public static boolean checkBasin(Block block, Direction direction, BlockEntity entity) {
        return block instanceof BasinBlock && direction == Direction.UP &&
                entity instanceof BasinBlockEntity basin && !basin.isRemoved();
    }

    public static boolean checkBoiler(Block block, Direction direction, BlockEntity entity) {
        return block instanceof FluidTankBlock && direction == Direction.UP &&
                entity instanceof FluidTankBlockEntity tank && !tank.isRemoved() &&
                tank.getControllerBE().boiler.isActive();
    }

    public static void init() {
        HeatBehaviourManager.getInstance().registerBehaviour(HeatBehaviourBasin.ID, HeatBehaviourBasin::new);
        HeatBehaviourManager.getInstance().registerBehaviour(HeatBehaviourBoiler.ID, HeatBehaviourBoiler::new);
    }

    static {

        HEAT_CONVERTER = BlockRegistry.BLOCKS.register("heat_converter", HeatConverterBlock::new);

        HEAT_CONVERTER_ENTITY = BlockRegistry.BLOCK_ENTITY_TYPES.register("heat_converter",
                () -> BlockEntityType.Builder.of(HeatConverterBlockEntity::new, HEAT_CONVERTER.get()).build(null)
        );

        HEAT_CONVERTER_ITEM = ItemRegistry.ITEMS.registerSimpleBlockItem("heat_converter", HEAT_CONVERTER);

        CONVERTER_COVER = ItemRegistry.ITEMS.registerSimpleItem("converter_cover");

    }
}
