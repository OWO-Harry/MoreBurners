package net.dragonegg.moreburners.registry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.content.item.HeatUpgradeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(MoreBurners.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockRegistry.ELECTRIC_BURNER.get().asItem());
        }
    };

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreBurners.MODID);

    public static RegistryObject<Item> register(String name, Item.Properties properties) {
        return ITEMS.register(name, () -> new Item(properties.tab(CREATIVE_TAB)));
    }

    public static final RegistryObject<Item> COPPER_COIL = register("copper_coil", new Item.Properties());
    public static final RegistryObject<Item> NICKEL_COIL = register("nickel_coil", new Item.Properties());

    public static final RegistryObject<SequencedAssemblyItem> INCOMPLETE_HEAT_UPGRADE =
            ITEMS.register("incomplete_heat_upgrade", () -> new SequencedAssemblyItem(new Item.Properties()));

    public static final RegistryObject<Item> HEAT_UPGRADE = ITEMS.register("heat_upgrade",
            () -> new HeatUpgradeItem(new Item.Properties())
    );

}
