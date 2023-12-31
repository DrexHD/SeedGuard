package me.drex.seedguard.mixin;

import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(StructurePlacement.class)
public abstract class StructurePlacementMixin {

    @Redirect(
        method = "legacyProbabilityReducerWithDouble",
        at = @At(
            value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/WorldgenRandom;setLargeFeatureSeed(JII)V"
        )
    )
    private static void useFeatureSalt(WorldgenRandom random, long l, int i, int j, long levelSeed, int salt, int x, int y, float f) {
        random.setLargeFeatureWithSalt(levelSeed, x, y, salt);
    }

    @ModifyArgs(
        method = "legacyArbitrarySaltProbabilityReducer",
        at = @At(
            value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/WorldgenRandom;setLargeFeatureWithSalt(JIII)V"
        )
    )
    private static void useFeatureSalt(Args args, long levelSeed, int salt, int x, int y, float f) {
        args.set(3, salt);
    }

}
