package me.imbanana.events;

import me.imbanana.BetterFishing;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModAutoFishingEvent implements ClientTickEvents.EndTick {
    private boolean recast = false;
    private int recastCooldown = 0;

    @Override
    public void onEndTick(Minecraft client) {
        if(!BetterFishing.getConfig().isAutoFishingEnabled()) {
            recast = false;
            return;
        }

        LocalPlayer player = client.player;
        if (player == null) {
            recast = false;
            return;
        }

        if(BetterFishing.getConfig().getBreakProtection() && !canUseActiveFishingRod(player)) {
            recast = false;
//            BetterFishing.getConfig().disableAutoFishing();
            client.player.sendOverlayMessage(
                    Component.translatable("text.betterfishing.auto_fishing.disabled")
                            .withStyle(ChatFormatting.DARK_RED)
                            .append(Component.literal(" "))
                            .append(Component.translatable("text.betterfishing.auto_fishing.disabled_fishing_rod").withStyle(ChatFormatting.WHITE))
            );
            return;
        }

        FishingHook fishingHook = client.player.fishing;

        if(fishingHook != null && fishingHook.biting && !recast) {
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
                    player.sendOverlayMessage(Component.translatable("event.betterfishing.auto_fishing.recast_time", String.format("%.2f", recastCooldown / 20f)));
                }
            }
        }
    }

    private boolean canUseActiveFishingRod(LocalPlayer player) {
        for(InteractionHand interactionHand : InteractionHand.values()) {
            ItemStack handItem = player.getItemInHand(interactionHand);
            if(handItem.getItem() != Items.FISHING_ROD) continue;

            return !handItem.isDamageableItem() || handItem.getMaxDamage() - handItem.getDamageValue() > 1;
        }

        return true;
    }

    private void useFishingRod(LocalPlayer player) {
        for(InteractionHand interactionHand : InteractionHand.values()) {
            ItemStack handItem = player.getItemInHand(interactionHand);
            if(handItem.getItem() != Items.FISHING_ROD) continue;

            Minecraft.getInstance().gameMode.useItem(player, interactionHand);
        }
    }
}
