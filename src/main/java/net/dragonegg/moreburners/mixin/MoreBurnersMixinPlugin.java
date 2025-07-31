package net.dragonegg.moreburners.mixin;

import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MoreBurnersMixinPlugin implements IMixinConfigPlugin {

    public MoreBurnersMixinPlugin() {
    }

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return !(targetClassName.equals("com.simibubi.create.content.processing.basin.BasinRecipe") ||
                targetClassName.equals("com.simibubi.create.content.fluids.tank.FluidTankBlockEntity") ||
                targetClassName.equals("me.desht.pneumaticcraft.common.block.HeatPipeBlock")) ||
                LoadingModList.get().getModFileById("pneumaticcraft") != null;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

}
