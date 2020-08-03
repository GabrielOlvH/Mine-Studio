package me.steven.minestudio.mixin;

import me.steven.minestudio.MineStudio;
import me.steven.minestudio.audio.MSSoundInstance;
import me.steven.minestudio.items.MSDiscItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow
    private ClientWorld world;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    private void minestudio_handleCustomDisc(PlayerEntity source, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId != 1010) return;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JukeboxBlockEntity) {
            MineStudio.INSTANCE.getPLAYING_AUDIOS().remove(pos);
            JukeboxBlockEntity jukebox = (JukeboxBlockEntity) blockEntity;
            ItemStack record = jukebox.getRecord();
            if (!MSDiscItem.Companion.isEmpty(record)) {
                CompoundTag tag = record.getTag();
                if (tag == null) return;
                MSSoundInstance instance = new MSSoundInstance();
                instance.fromTag(tag);
                MineStudio.INSTANCE.getPLAYING_AUDIOS().put(pos, instance);
                //playMSDisc(instance, pos);
                ci.cancel();
            }
        }
    }

    /*private void playMSDisc(MSSoundInstance soundInstance, BlockPos songPosition) {
        SoundInstance current = this.playingSongs.get(songPosition);
        if (current != null) {
            this.client.getSoundManager().stop(current);
            this.playingSongs.remove(songPosition);
        }
        //TODO add overlay for custom disc name
        //MusicDiscItem musicDiscItem = MusicDiscItem.bySound(song);
        //if (musicDiscItem != null) {
        //this.client.inGameHud.setRecordPlayingOverlay(musicDiscItem.getDescription());
        //}
        this.playingSongs.put(songPosition, soundInstance);
        this.client.getSoundManager().play(soundInstance);

        this.updateEntitiesForSong(this.world, songPosition, true);
    }*/
}
