package net.dragonegg.moreburners.registry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.content.item.HeatUpgradeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreBurners.MODID);

    /*
    public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot",
            () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> NICKEL_NUGGET = ITEMS.register("nickel_nugget",
            () -> new Item(new Item.Properties())
    );
     */
    public static final RegistryObject<Item> COPPER_COIL = ITEMS.register("copper_coil",
            () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> NICKEL_COIL = ITEMS.register("nickel_coil",
            () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<SequencedAssemblyItem> INCOMPLETE_HEAT_UPGRADE =
            ITEMS.register("incomplete_heat_upgrade", () -> new SequencedAssemblyItem(new Item.Properties()));
    public static final RegistryObject<Item> HEAT_UPGRADE = ITEMS.register("heat_upgrade",
            () -> new HeatUpgradeItem(new Item.Properties())
    );

    public static final RegistryObject<Item> ELECTRIC_BURNER_ITEM = ITEMS.register("electric_burner",
            () -> new BlockItem(BlockRegistry.ELECTRIC_BURNER.get(), new Item.Properties())
    );

}
