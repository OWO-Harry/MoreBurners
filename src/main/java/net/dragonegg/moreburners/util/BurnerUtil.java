package net.dragonegg.moreburners.util;

import com.rekindled.embers.RegistryManager;
import net.dragonegg.moreburners.compat.embers.EmbersCompat;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.registry.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BurnerUtil {

    public static HashMap<Block, Item> CATALYST = new HashMap<>();

    public static void initCatalyst() {
        if(ModList.get().isLoaded("embers")) {
            CATALYST.put(EmbersCompat.EMBER_BURNER.get(), RegistryManager.ATMOSPHERIC_BELLOWS_ITEM.get());
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

}
