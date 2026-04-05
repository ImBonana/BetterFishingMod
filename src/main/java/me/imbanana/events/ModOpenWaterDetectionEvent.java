package me.imbanana.events;

import me.imbanana.BetterFishing;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.projectile.FishingHook;

public class ModOpenWaterDetectionEvent implements ClientTickEvents.EndTick {
    private boolean inOpenWater = true;
    private boolean alreadySendStatusMessage = false;
    private boolean lastOpenWaterState = false;

    @Override
    public void onEndTick(Minecraft client) {
        if (!BetterFishing.getConfig().isOpenWaterDetection()) return;

        LocalPlayer player = client.player;
        if (player == null) {
            this.inOpenWater = true;
            this.alreadySendStatusMessage = false;
            return;
        }

        FishingHook fishingHook = client.player.fishing;
        if (fishingHook == null) {
            this.inOpenWater = true;
            this.alreadySendStatusMessage = false;
            return;
        }

        if(fishingHook.currentState != FishingHook.FishHookState.BOBBING) return;


        this.inOpenWater = isInOpenWater(fishingHook);

        if (!alreadySendStatusMessage || lastOpenWaterState != this.inOpenWater) {
            if (this.inOpenWater) {
                player.sendOverlayMessage(
                        Component.translatable("event.betterfishing.open_water_detection.success")
                                .withStyle(ChatFormatting.AQUA)
                );
            } else {
                player.sendOverlayMessage(
                        Component.translatable("event.betterfishing.open_water_detection.fail")
                                .withStyle(ChatFormatting.RED)
                );
            }

            alreadySendStatusMessage = true;
            lastOpenWaterState = this.inOpenWater;
        }
    }

    private boolean isInOpenWater(FishingHook fishingHook) {
        return fishingHook.outOfWaterTime < 10 && fishingHook.calculateOpenWater(fishingHook.blockPosition());
    }
}
