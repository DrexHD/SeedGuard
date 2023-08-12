package me.drex.seedguard.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.drex.seedguard.SeedManager;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(ChunkGeneratorStructureState.class)
public class ChunkGeneratorStructureStateMixin {

    @Inject(
        method = "createForNormal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/chunk/ChunkGeneratorStructureState;<init>(Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/biome/BiomeSource;JJLjava/util/List;)V"
        )
    )
    private static void modifyFeatureSalts(RandomState randomState, long l, BiomeSource biomeSource, HolderLookup<StructureSet> holderLookup, CallbackInfoReturnable<ChunkGeneratorStructureState> cir, @Local List<Holder<StructureSet>> list) {
        list.forEach(holder -> {
            StructureSet structureSet = holder.value();
            int salt = SeedManager.getStructureSeed(holder);
            ((StructurePlacementAccessor) structureSet.placement()).setSalt(salt);
        });
    }

    // Using @ModifyArgs to capture target method arguments
    @ModifyArgs(
        method = "generateRingPositions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/RandomSource;setSeed(J)V"
        )
    )
    private void modifyRingPlacementFeatureSalt(Args args, Holder<StructureSet> holder, ConcentricRingsStructurePlacement structurePlacement) {
        args.set(0, (long) SeedManager.getStructureSeed(holder));
    }

}
