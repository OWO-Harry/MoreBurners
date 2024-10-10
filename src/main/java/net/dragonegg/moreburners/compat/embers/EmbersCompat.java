package net.dragonegg.moreburners.compat.embers;

import net.dragonegg.moreburners.content.block.EmberBurnerBlock;
import net.dragonegg.moreburners.content.block.entity.EmberBurnerBlockEntity;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.registry.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

public class EmbersCompat {

    public static final RegistryObject<Block> EMBER_BURNER;

    public static final RegistryObject<BlockEntityType<EmberBurnerBlockEntity>> EMBER_BURNER_ENTITY;

    public static final RegistryObject<Item> EMBER_BURNER_ITEM;

    public EmbersCompat() {
    }

    public static void init() {
    }

    static {

        EMBER_BURNER = BlockRegistry.BLOCKS.register("ember_burner",
                () -> new EmberBurnerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))
        );

        EMBER_BURNER_ENTITY = BlockRegistry.BLOCK_ENTITY_TYPES.register("ember_burner",
                () -> BlockEntityType.Builder.of(EmberBurnerBlockEntity::new, EMBER_BURNER.get()).build(null)
        );

        EMBER_BURNER_ITEM = ItemRegistry.ITEMS.register("ember_burner",
                () -> new BlockItem(EMBER_BURNER.get(), new Item.Properties())
        );

    }

}
