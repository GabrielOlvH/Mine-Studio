package me.steven.minestudio.mixin;

import me.steven.minestudio.audio.MSSoundInstance;
import me.steven.minestudio.items.MSDiscItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow
    private ClientWorld world;

    @Shadow
    @Final
    private Map<BlockPos, SoundInstance> playingSongs;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void updateEntitiesForSong(World world, BlockPos pos, boolean playing);

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    private void minestudio_handleCustomDisc(PlayerEntity source, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId != 1010) return;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JukeboxBlockEntity) {
            JukeboxBlockEntity jukebox = (JukeboxBlockEntity) blockEntity;
            if (!MSDiscItem.Companion.isEmpty(jukebox.getRecord())) {
                MSSoundInstance instance = new MSSoundInstance();
                CompoundTag tag = jukebox.getRecord().getTag();
                if (tag == null) return;
                instance.fromTag(tag);
                playMSDisc(instance, pos);
                ci.cancel();
            }
        }
    }

    private void playMSDisc(MSSoundInstance soundInstance, BlockPos songPosition) {
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
    }
}
