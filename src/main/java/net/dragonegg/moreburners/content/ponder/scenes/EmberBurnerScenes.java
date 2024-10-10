package net.dragonegg.moreburners.content.ponder.scenes;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.entity.EmberPacketEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import net.dragonegg.moreburners.content.block.entity.EmberBurnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class EmberBurnerScenes {

    public static void emberBurnerBasin(SceneBuilder scene, SceneBuildingUtil util) {

        scene.title("ember_burner", "Using Blaze Burners");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(10);

        BlockPos burner = util.grid.at(2, 1, 2);
        scene.world.showSection(util.select.position(burner), Direction.DOWN);
        scene.idle(10);
        scene.world.showSection(util.select.position(burner.above()), Direction.DOWN);
        scene.idle(10);

        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Ember Burners can provide Heat to Any Recipes required Blaze Burner")
                .pointAt(util.vector.blockSurface(burner, Direction.WEST))
                .placeNearTarget();
        scene.idle(80);

        scene.world.hideSection(util.select.position(burner.above()), Direction.UP);
        scene.idle(20);
        scene.world.setBlock(burner.above(), Blocks.AIR.defaultBlockState(), false);
        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("It consumes Ember Energy to increase Temperature")
                .pointAt(util.vector.blockSurface(burner, Direction.WEST))
                .placeNearTarget();
        scene.idle(80);

        scene.world.showSection(util.select.position(burner.north()), Direction.SOUTH);
        scene.idle(10);
        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Ember Energy can be Input from All Sides using Ember Receptor")
                .pointAt(util.vector.blockSurface(burner.north(), Direction.SOUTH))
                .placeNearTarget();
        scene.idle(80);

        for (int x = 0; x < 3; x++) {
            scene.world.createEntity(w -> {
                EmberPacketEntity emberEntity = RegistryManager.EMBER_PACKET.get().create(w);
                emberEntity.setPosRaw(2,2,5);
                emberEntity.addTag("{lifetime:60,Motion:[0.0d,0.5d,0.0d],destX:2,destY:1,destZ:3}");
                return emberEntity;
            });
            if (x == 1) {
                scene.world.modifyBlock(burner, s -> s.setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.KINDLED), false);
            }
            scene.idle(20);
        }

        scene.world.showSection(util.select.position(burner.west()), Direction.EAST);
        scene.idle(10);
        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("The value of Ember Energy and Heat can be read from Dials")
                .pointAt(util.vector.blockSurface(burner.west(), Direction.EAST))
                .placeNearTarget();
        scene.idle(80);

        scene.world.showSection(util.select.position(burner.east()), Direction.DOWN);
        scene.idle(10);
        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Atmospheric Bellows can increase its Maximum Heat and it can now reach super heating")
                .pointAt(util.vector.blockSurface(burner.east(), Direction.UP))
                .placeNearTarget();
        scene.idle(80);

        for (int x = 0; x < 6; x++) {
            scene.world.createEntity(w -> {
                EmberPacketEntity emberEntity = RegistryManager.EMBER_PACKET.get().create(w);
                emberEntity.setPosRaw(2,2,5);
                emberEntity.addTag("{lifetime:60,Motion:[0.0d,0.5d,0.0d],destX:2,destY:1,destZ:3}");
                return emberEntity;
            });
            if (x == 4) {
                scene.world.modifyBlock(burner, s -> s.setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SEETHING), false);
            }
            scene.idle(20);
        }

        scene.world.showSection(util.select.position(burner.south()), Direction.DOWN);
        scene.idle(10);
        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Other Ember Burners' Maximum Heat next to it will also Improved")
                .pointAt(util.vector.blockSurface(burner.south(), Direction.WEST))
                .placeNearTarget();
        scene.idle(80);

        for (int x = 0; x < 3; x++) {
            scene.world.createEntity(w -> {
                EmberPacketEntity emberEntity = RegistryManager.EMBER_PACKET.get().create(w);
                emberEntity.setPosRaw(2,2,5);
                emberEntity.addTag("{lifetime:60,Motion:[0.0d,0.5d,0.0d],destX:2,destY:1,destZ:3}");
                return emberEntity;
            });
            if (x == 2) {
                scene.world.modifyBlock(burner.south(), s -> s.setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SEETHING), false);
            }
            scene.idle(20);
        }

    }

}
