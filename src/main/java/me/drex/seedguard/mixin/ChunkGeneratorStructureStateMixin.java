package me.drex.seedguard.mixin;

import me.drex.seedguard.SeedManager;
import net.minecraft.core.Holder;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(ChunkGeneratorStructureState.class)
public class ChunkGeneratorStructureStateMixin {

    @ModifyArg(
        method = "createForNormal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/chunk/ChunkGeneratorStructureState;<init>(Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/biome/BiomeSource;JJLjava/util/List;)V"
        ), index = 4
    )
    private static List<Holder<StructureSet>> modifyFeatureSalts(List<Holder<StructureSet>> original) {
        return original.stream().map(ChunkGeneratorStructureStateMixin::modifyFeatureSalt).toList();
    }

    private static Holder<StructureSet> modifyFeatureSalt(Holder<StructureSet> holder) {
        StructureSet structureSet = holder.value();
        if (structureSet.placement() instanceof RandomSpreadStructurePlacement) {
            // ConcentricRingsStructurePlacement doesn't use the salt
            int salt = SeedManager.getStructureSeed(holder);
            ((StructurePlacementAccessor)structureSet.placement()).setSalt(salt);
        }
        return holder;
    }

}
