package me.steven.minestudio.mixin;

import me.steven.minestudio.MineStudioClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow
    private ClientWorld world;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    private void minestudio_handleCustomDisc(PlayerEntity source, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId == 1010)
            MineStudioClient.INSTANCE.getPLAYING_AUDIOS().remove(pos);
    }
}
