package net.dragonegg.moreburners.registry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.dragonegg.moreburners.MoreBurners;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Iterator;


public class TabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreBurners.MODID);

    public static final RegistryObject<CreativeModeTab> MOREBURNERS_TAB = CREATIVE_TABS.register("moreburners_tab",
            () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.moreburners")).icon(
                () -> new ItemStack(ItemRegistry.HEAT_UPGRADE.get())
            ).displayItems((params, output) -> {
                Iterator<RegistryObject<Item>> var2 = ItemRegistry.ITEMS.getEntries().iterator();
                while(var2.hasNext()) {
                    RegistryObject<Item> item = var2.next();
                    if(!(item.get() instanceof SequencedAssemblyItem)) {
                        output.accept(item.get());
                    }
                }
            }).build());

}
