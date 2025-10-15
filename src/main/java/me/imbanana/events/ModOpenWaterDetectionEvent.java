package me.imbanana.events;

import me.imbanana.BetterFishing;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ModOpenWaterDetectionEvent implements ClientTickEvents.EndTick {
    private boolean inOpenWater = true;
    private boolean alreadySendStatusMessage = false;

    @Override
    public void onEndTick(MinecraftClient client) {
        if (!BetterFishing.getConfig().isOpenWaterDetection()) return;

        ClientPlayerEntity player = client.player;
        if (player == null) {
            this.inOpenWater = true;
            this.alreadySendStatusMessage = false;
            return;
        }

        FishingBobberEntity fishingBobber = client.player.fishHook;
        if (fishingBobber == null) {
            this.inOpenWater = true;
            this.alreadySendStatusMessage = false;
            return;
        }

        if(fishingBobber.state != FishingBobberEntity.State.BOBBING) return;

        if (isInOpenWater(fishingBobber)) {
            if(!alreadySendStatusMessage) {
                player.sendMessage(
                        Text.translatable("event.betterfishing.open_water_detection.success")
                                .formatted(Formatting.AQUA),
                        true
                );
                alreadySendStatusMessage = true;
            }
        } else {
            if(!alreadySendStatusMessage) {
                player.sendMessage(
                        Text.translatable("event.betterfishing.open_water_detection.fail")
                                .formatted(Formatting.RED),
                        true
                );
                alreadySendStatusMessage = true;
            }
        }
    }

    private boolean isInOpenWater(FishingBobberEntity fishingBobber) {
        return this.inOpenWater = this.inOpenWater && fishingBobber.outOfOpenWaterTicks < 10 && fishingBobber.isOpenOrWaterAround(fishingBobber.getBlockPos());
    }
}
