package me.steven.minestudio.mixin;

import me.steven.minestudio.MineStudioClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    
    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    private void minestudio_handleCustomDisc(PlayerEntity source, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId == 1010)
            MineStudioClient.INSTANCE.getPLAYING_AUDIOS().remove(pos);
    }
}
