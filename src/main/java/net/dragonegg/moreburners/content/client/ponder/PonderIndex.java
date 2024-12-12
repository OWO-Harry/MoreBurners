package net.dragonegg.moreburners.content.client.ponder;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import net.dragonegg.moreburners.MoreBurners;

public class PonderIndex {

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(MoreBurners.MODID);

    public static void register() {

        /*
        HELPER.forComponents(ItemProviderUtil.createBlockEntry(BlockRegistry.EMBER_BURNER))
                .addStoryBoard(new ResourceLocation(MoreBurners.MODID,"ember_burner/basic"), EmberBurnerScenes::emberBurnerBasin);

         */
    }

}
