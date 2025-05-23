package net.dragonegg.moreburners.registry;

import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.content.block.ElectricBurnerBlock;
import net.dragonegg.moreburners.content.block.entity.ElectricBurnerBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MoreBurners.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MoreBurners.MODID);

    public static final DeferredBlock<ElectricBurnerBlock> ELECTRIC_BURNER =
            BLOCKS.registerBlock("electric_burner", ElectricBurnerBlock::new, Blocks.IRON_BLOCK.properties());

    public static final Supplier<BlockEntityType<ElectricBurnerBlockEntity>> ELECTRIC_BURNER_ENTITY =
            BLOCK_ENTITY_TYPES.register("electric_burner",
                    () -> Builder.of(ElectricBurnerBlockEntity::new, ELECTRIC_BURNER.get()).build(null)
            );


}
