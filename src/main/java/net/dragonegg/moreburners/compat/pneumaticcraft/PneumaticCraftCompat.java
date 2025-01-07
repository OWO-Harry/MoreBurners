package net.dragonegg.moreburners.compat.pneumaticcraft;

import net.dragonegg.moreburners.content.block.HeatConverterBlock;
import net.dragonegg.moreburners.content.block.entity.HeatConverterBlockEntity;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.registry.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class PneumaticCraftCompat {

    public static final RegistryObject<Block> HEAT_CONVERTER;

    public static final RegistryObject<BlockEntityType<HeatConverterBlockEntity>> HEAT_CONVERTER_ENTITY;

    public static final RegistryObject<Item> CONVERTER_COVER;

    public PneumaticCraftCompat() {
    }

    public static void init() {
    }

    static {

        HEAT_CONVERTER = BlockRegistry.registerBlock("heat_converter", HeatConverterBlock::new);

        HEAT_CONVERTER_ENTITY = BlockRegistry.BLOCK_ENTITY_TYPES.register("heat_converter",
                () -> BlockEntityType.Builder.of(HeatConverterBlockEntity::new, HEAT_CONVERTER.get()).build(null)
        );

        CONVERTER_COVER = ItemRegistry.register("converter_cover", new Item.Properties());

    }
}
