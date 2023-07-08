package me.drex.seedguard.mixin;

import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructurePlacement.class)
public interface StructurePlacementAccessor {

    @Accessor("salt")
    @Mutable
    void setSalt(int salt);

}
