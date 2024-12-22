package net.dragonegg.moreburners.event;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.dragonegg.moreburners.compat.botania.BlazeBurnerExoflameHeatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.forge.CapabilityUtil;


public class Events {

    public static void register(IEventBus bus) {
        bus.addGenericListener(BlockEntity.class, Events::attachBeCaps);
    }

    private static void attachBeCaps(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();

        if(ModList.get().isLoaded("botania")) {
            if(be instanceof BlazeBurnerBlockEntity burner) {
                e.addCapability(new ResourceLocation("botania", "exoflame_heatable"),
                        CapabilityUtil.makeProvider(BotaniaForgeCapabilities.EXOFLAME_HEATABLE,
                                new BlazeBurnerExoflameHeatable(burner)));
            }
        }
    }

}
