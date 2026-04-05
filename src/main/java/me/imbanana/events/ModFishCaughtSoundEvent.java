package me.imbanana.events;

import me.imbanana.BetterFishing;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.FishingHook;

public class ModFishCaughtSoundEvent implements ClientTickEvents.EndTick {
    private int soundCooldown = 0;

    @Override
    public void onEndTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) return;

        FishingHook fishingHook = client.player.fishing;
        if (fishingHook == null) return;

        if (fishingHook.biting && soundCooldown <= 0 && BetterFishing.getConfig().shouldMakeSoundWhenCaughtAFish()) {
            player.level().playPlayerSound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.3f,0.8f);
        }

        if(soundCooldown <= 0) {
            soundCooldown = 5;
        } else {
            soundCooldown--;
        }
    }
}
