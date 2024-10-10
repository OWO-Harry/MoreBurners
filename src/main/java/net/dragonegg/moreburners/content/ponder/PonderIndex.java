package net.dragonegg.moreburners.content.ponder;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import net.dragonegg.moreburners.MoreBurners;
import net.dragonegg.moreburners.content.ponder.scenes.EmberBurnerScenes;
import net.dragonegg.moreburners.registry.BlockRegistry;
import net.dragonegg.moreburners.util.ItemProviderUtil;
import net.minecraft.resources.ResourceLocation;

public class PonderIndex {

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(MoreBurners.MODID);

    public static void register() {

        /*
        HELPER.forComponents(ItemProviderUtil.createBlockEntry(BlockRegistry.EMBER_BURNER))
                .addStoryBoard(new ResourceLocation(MoreBurners.MODID,"ember_burner/basic"), EmberBurnerScenes::emberBurnerBasin);

         */
    }

}
