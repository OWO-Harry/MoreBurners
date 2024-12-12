package net.dragonegg.moreburners.mixin;

import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.gui.element.GuiGameElement.GuiRenderBuilder;
import net.dragonegg.moreburners.content.block.BaseBurnerBlock;
import net.dragonegg.moreburners.util.BurnerUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(AnimatedBlazeBurner.class)
public class AnimatedBlazeBurnerMixin {

    @Shadow(remap = false)
    private BlazeBurnerBlock.HeatLevel heatLevel;

    @Unique
    private int burnerIndex;

    @Redirect(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/gui/element/GuiGameElement$GuiRenderBuilder;atLocal(DDD)Lcom/simibubi/create/foundation/gui/element/GuiGameElement$GuiRenderBuilder;",
                    ordinal = 0
            ),
            remap = false
    )
    private GuiRenderBuilder onDraw$addBurnerVariety(GuiRenderBuilder instance, double x, double y, double z){

        long time = System.currentTimeMillis();

        List<Block> burners = BurnerUtil.getBurners();
        if (!Screen.hasShiftDown()) {
            burnerIndex = Math.toIntExact((time / 1000) % (burners.size()+1));
        }
        if(burnerIndex==burners.size()) {
            return instance.atLocal(0, 1.65, 0);
        }

        HeatLevel level = heatLevel == HeatLevel.SEETHING? HeatLevel.SEETHING : HeatLevel.KINDLED;
        BlockState state = BurnerUtil.getBurnerState(burners.get(burnerIndex), level);

        return AnimatedKinetics.defaultBlockElement(state).atLocal(0, 1.65, 0);

    }

    /*
    @Inject(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/compat/jei/category/animations/AnimatedBlazeBurner;blockElement(Lcom/jozufozu/flywheel/core/PartialModel;)Lcom/simibubi/create/foundation/gui/element/GuiGameElement$GuiRenderBuilder;",
                    ordinal = 0
            ),
            cancellable = true,
            remap = false
    )
    private void onDraw$cancelOtherModels(GuiGraphics graphics, int xOffset, int yOffset, CallbackInfo ci) {

        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {

            long dayTime = level.getDayTime();
            List<Block> burners = BurnerUtil.getBurners();
            int i = (int) ((dayTime / 25) % (burners.size()+1));
            if(i!=burners.size()) {
                ci.cancel();
            }

        }

    }
     */

}
