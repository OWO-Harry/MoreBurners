package net.dragonegg.moreburners.registry;

import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.content.block.ElectricBurnerBlock;
import net.dragonegg.moreburners.content.block.entity.ElectricBurnerBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ItemRegistry.CREATIVE_TAB)));
    }

    public static final DeferredRegister<Block> BLOCKS;
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;

    public static final RegistryObject<Block> ELECTRIC_BURNER;
    public static final RegistryObject<BlockEntityType<ElectricBurnerBlockEntity>> ELECTRIC_BURNER_ENTITY;

    public BlockRegistry() {}

    static {

        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreBurners.MODID);
        BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MoreBurners.MODID);

        ELECTRIC_BURNER = registerBlock("electric_burner", () -> new ElectricBurnerBlock(Properties.copy(Blocks.STONE)));
        ELECTRIC_BURNER_ENTITY = BLOCK_ENTITY_TYPES.register("electric_burner",
                () -> Builder.of(ElectricBurnerBlockEntity::new, ELECTRIC_BURNER.get()).build(null)
        );

    }


}
