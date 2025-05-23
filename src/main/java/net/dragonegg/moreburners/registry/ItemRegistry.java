package net.dragonegg.moreburners.registry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.content.item.HeatUpgradeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreBurners.MODID);

    public static final DeferredItem<Item> COPPER_COIL = ITEMS.registerSimpleItem("copper_coil");
    public static final DeferredItem<Item> NICKEL_COIL = ITEMS.registerSimpleItem("nickel_coil");

    public static final DeferredItem<SequencedAssemblyItem> INCOMPLETE_HEAT_UPGRADE =
            ITEMS.registerItem("incomplete_heat_upgrade", SequencedAssemblyItem::new);
    public static final DeferredItem<HeatUpgradeItem> HEAT_UPGRADE =
            ITEMS.registerItem("heat_upgrade", HeatUpgradeItem::new);

    public static final DeferredItem<BlockItem> ELECTRIC_BURNER_ITEM =
            ITEMS.registerSimpleBlockItem("electric_burner", BlockRegistry.ELECTRIC_BURNER);

}
