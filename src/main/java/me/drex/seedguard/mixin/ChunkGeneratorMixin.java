package me.drex.seedguard.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.drex.seedguard.SeedManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {

    @ModifyArg(
        method = "applyBiomeDecoration",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/WorldgenRandom;setFeatureSeed(JII)V",
            ordinal = 1
        ), index = 0
    )
    private long modifyFeatureSeed(long featurePopulationSeed, @Local BlockPos pos, @Local WorldgenRandom worldgenRandom, @Local PlacedFeature placedFeature) {
        return worldgenRandom.setDecorationSeed(SeedManager.getFeatureSeed(placedFeature.feature()), pos.getX(), pos.getZ());
    }


}
