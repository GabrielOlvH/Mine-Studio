package me.steven.minestudio.mixin;

import me.steven.minestudio.MineStudio;
import me.steven.minestudio.audio.MSSoundInstance;
import me.steven.minestudio.items.MSDiscItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
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
        if (eventId != 1010) return;
        MineStudio.INSTANCE.getPLAYING_AUDIOS().remove(pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JukeboxBlockEntity && data == -1) {
            JukeboxBlockEntity jukebox = (JukeboxBlockEntity) blockEntity;
            ItemStack record = jukebox.getRecord();
            if (!MSDiscItem.Companion.isEmpty(record)) {
                CompoundTag tag = record.getTag();
                if (tag == null) return;
                MSSoundInstance instance = new MSSoundInstance();
                instance.fromTag(tag);
                MineStudio.INSTANCE.getPLAYING_AUDIOS().put(pos, instance);
                Text display;
                if (record.hasCustomName()) display = record.getName();
                else display = new LiteralText("MineStudio Disc");
                client.inGameHud.setRecordPlayingOverlay(display);
                ci.cancel();
            }
        }
    }
}
