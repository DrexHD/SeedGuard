package me.drex.seedguard.mixin;

import me.drex.seedguard.SeedManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "createLevels", at = @At("HEAD"))
    private void loadFeatureSeeds(ChunkProgressListener chunkProgressListener, CallbackInfo ci) {
        SeedManager.load((MinecraftServer) (Object) this);
    }

}
