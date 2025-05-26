package net.dragonegg.moreburners.compat.pneumaticcraft;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import me.desht.pneumaticcraft.common.core.ModBlockEntities;
import me.desht.pneumaticcraft.common.heat.behaviour.HeatBehaviourManager;
import net.dragonegg.moreburners.content.block.HeatConverterBlock;
import net.dragonegg.moreburners.content.block.entity.HeatConverterBlockEntity;
import net.dragonegg.moreburners.mixin.accessor.BlockEntityTypeAccessor;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PneumaticCraftCompat {

    public static final RegistryObject<Block> HEAT_CONVERTER;

    public static final RegistryObject<BlockEntityType<HeatConverterBlockEntity>> HEAT_CONVERTER_ENTITY;

    public static final RegistryObject<Item> HEAT_CONVERTER_ITEM;
    public static final RegistryObject<Item> CONVERTER_COVER;

    public static boolean checkBasin(Block block, Direction direction, BlockEntity entity) {
        return block instanceof BasinBlock && direction == Direction.UP &&
                entity instanceof BasinBlockEntity basin && !basin.isRemoved();
    }

    public static boolean checkBoiler(Block block, Direction direction, BlockEntity entity) {
        return block instanceof FluidTankBlock && direction == Direction.UP &&
                entity instanceof FluidTankBlockEntity tank && !tank.isRemoved() &&
                tank.getControllerBE().boiler.isActive();
    }

    private static final List<BlockEntity> DUMMY_BE_LIST = new ArrayList<>();

    public static Stream<BlockEntity> streamBlockEntities() {
        if (DUMMY_BE_LIST.isEmpty()) {
            DUMMY_BE_LIST.addAll(ModBlockEntities.BLOCK_ENTITIES.getEntries().stream()
                    .flatMap(o -> ((BlockEntityTypeAccessor) o.get()).getValidBlocks().stream()
                            .findFirst()
                            .stream()
                            .map(b -> o.get().create(BlockPos.ZERO, b.defaultBlockState())))
                    .toList()
            );
        }
        return DUMMY_BE_LIST.stream();
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

        HEAT_CONVERTER_ITEM = ItemRegistry.ITEMS.register("heat_converter",
                () -> new BlockItem(HEAT_CONVERTER.get(), new Item.Properties())
        );

        CONVERTER_COVER = ItemRegistry.ITEMS.register("converter_cover",
                () -> new Item(new Item.Properties())
        );

    }
}
