package net.dragonegg.moreburners.util;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import net.dragonegg.moreburners.compat.pneumaticcraft.PneumaticCraftCompat;
import net.dragonegg.moreburners.content.block.BaseBurnerBlock;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.registry.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BurnerUtil {

    public static HashMap<Block, Item> CATALYST = new HashMap<>();

    public static void initCatalyst() {
        if(ModList.get().isLoaded("pneumaticcraft")) {
            CATALYST.put(PneumaticCraftCompat.HEAT_CONVERTER.get(), Items.AIR);
        }
        CATALYST.put(BlockRegistry.ELECTRIC_BURNER.get(), ItemRegistry.HEAT_UPGRADE.get());
    }

    public static List<Block> getBurners() {
        List<Block> burners = new ArrayList<>();
        for (RegistryObject<Block> blockRegistryObject : BlockRegistry.BLOCKS.getEntries()) {
            burners.add(blockRegistryObject.get());
        }
        return burners;
    }

    public static List<ItemStack> getBurnerStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Block burner : getBurners()) {
            stacks.add(burner.asItem().getDefaultInstance());
        }
        return stacks;
    }

    public static BlockState getBurnerState(Block block, HeatLevel level) {
        if(block instanceof BaseBurnerBlock burner) {
            return burner.getState(level);
        }
        return null;
    }

    public static int getColor(HeatLevel level) {
        return switch (level) {
            case NONE, SMOULDERING -> 0xffffff;
            case FADING, KINDLED -> 0xcb3d07;
            case SEETHING -> 0x3a9af7;
        };
    }

}
