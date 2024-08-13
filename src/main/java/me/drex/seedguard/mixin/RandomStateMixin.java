package me.drex.seedguard.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.seedguard.SeedManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RandomState.class)
public abstract class RandomStateMixin {

    @Shadow
    @Final
    PositionalRandomFactory random;

    @WrapOperation(
        method = "method_41561",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/PositionalRandomFactory;fromHashOf(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/util/RandomSource;"
        )
    )
    public RandomSource modifyResourceLocationDependentRandom(PositionalRandomFactory instance, ResourceLocation resourceLocation, Operation<RandomSource> original) {
        return SeedManager.getSurfaceRuleSeed(resourceLocation)
            .map(seed -> this.random.fromSeed(seed))
            .orElseGet(() -> original.call(instance, resourceLocation));
    }

}
