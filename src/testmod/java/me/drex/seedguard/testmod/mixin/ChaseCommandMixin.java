package me.drex.seedguard.testmod.mixin;

import net.minecraft.server.commands.ChaseCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChaseCommand.class)
public abstract class ChaseCommandMixin {

    @ModifyArg(
        method = "lead",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/chase/ChaseServer;<init>(Ljava/lang/String;ILnet/minecraft/server/players/PlayerList;I)V"
        ),
        index = 3
    )
    private static int smoothChase(int i) {
        // Replace 10 updates per second with ~60
        return 1000 / 60;
    }

}
