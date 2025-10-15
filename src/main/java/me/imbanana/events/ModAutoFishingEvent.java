package me.imbanana.events;

import me.imbanana.BetterFishing;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

public class ModAutoFishingEvent implements ClientTickEvents.EndTick {
    private boolean recast = false;
    private int recastCooldown = 0;

    @Override
    public void onEndTick(MinecraftClient client) {
        if(!BetterFishing.getConfig().isAutoFishingEnabled()) {
            recast = false;
            return;
        }

        ClientPlayerEntity player = client.player;
        if (player == null) {
            recast = false;
            return;
        }

        if(BetterFishing.getConfig().getBreakProtection() && !canUseActiveFishingRod(player)) {
            recast = false;
//            BetterFishing.getConfig().disableAutoFishing();
            client.player.sendMessage(
                    Text.translatable("text.betterfishing.auto_fishing.disabled")
                            .formatted(Formatting.DARK_RED)
                            .append(Text.literal(" "))
                            .append(Text.translatable("text.betterfishing.auto_fishing.disabled_fishing_rod").formatted(Formatting.WHITE)),
                    true
            );
            return;
        }

        FishingBobberEntity fishingBobber = client.player.fishHook;

        if(fishingBobber != null && fishingBobber.caughtFish) {
            useFishingRod(player);
            recast = true;
            recastCooldown = BetterFishing.getConfig().getRecastDelay();
        } else if (recast) {
            if(recastCooldown <= 0) {
                useFishingRod(player);
                recast = false;
            } else {
                recastCooldown--;
                if(BetterFishing.getConfig().shouldShowRecastTime()) {
                    player.sendMessage(Text.translatable("event.betterfishing.auto_fishing.recast_time", String.format("%.2f", recastCooldown / 20f)), true);
                }
            }
        }
    }

    private boolean canUseActiveFishingRod(ClientPlayerEntity player) {
        for(Hand hand : Hand.values()) {
            ItemStack handItem = player.getStackInHand(hand);
            if(handItem.getItem() != Items.FISHING_ROD) continue;

            return !handItem.isDamageable() || handItem.getMaxDamage() - handItem.getDamage() > 1;
        }

        return true;
    }

    private void useFishingRod(ClientPlayerEntity player) {
        for(Hand hand : Hand.values()) {
            ItemStack handItem = player.getStackInHand(hand);
            if(handItem.getItem() != Items.FISHING_ROD) continue;

            MinecraftClient.getInstance().interactionManager.interactItem(player, hand);
        }
    }
}
