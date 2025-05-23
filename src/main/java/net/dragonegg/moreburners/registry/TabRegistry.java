package net.dragonegg.moreburners.registry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.dragonegg.moreburners.MoreBurners;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class TabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreBurners.MODID);

    public static final Supplier<CreativeModeTab> MOREBURNERS_TAB = CREATIVE_TABS.register("moreburners_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.moreburners"))
                    .icon(() -> new ItemStack(ItemRegistry.HEAT_UPGRADE.get()))
                    .displayItems((params, output) ->
                            ItemRegistry.ITEMS.getEntries().stream()
                                .map(DeferredHolder::get)
                                .filter(item -> !(item instanceof SequencedAssemblyItem))
                                .forEach(output::accept))
                    .build());

}
