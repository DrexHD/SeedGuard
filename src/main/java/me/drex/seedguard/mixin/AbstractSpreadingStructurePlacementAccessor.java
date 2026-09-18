package me.drex.seedguard.mixin;

import net.minecraft.world.level.levelgen.structure.placement.AbstractSpreadingStructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSpreadingStructurePlacement.class)
public interface AbstractSpreadingStructurePlacementAccessor {

    @Accessor("salt")
    @Mutable
    void setSalt(int salt);

}
