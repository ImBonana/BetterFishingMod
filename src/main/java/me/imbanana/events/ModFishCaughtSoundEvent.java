package me.imbanana.events;

import me.imbanana.BetterFishing;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class ModFishCaughtSoundEvent implements ClientTickEvents.EndTick {
    private int soundCooldown = 0;

    @Override
    public void onEndTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        FishingBobberEntity fishingBobber = client.player.fishHook;
        if (fishingBobber == null) return;

        if (fishingBobber.caughtFish && soundCooldown <= 0 && BetterFishing.getConfig().shouldMakeSoundWhenCaughtAFish()) {
            player.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 0.3f,0.8f);
        }

        if(soundCooldown <= 0) {
            soundCooldown = 5;
        } else {
            soundCooldown--;
        }
    }
}
