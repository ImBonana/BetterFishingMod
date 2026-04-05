package me.imbanana.events;

import com.mojang.blaze3d.platform.InputConstants;
import me.imbanana.BetterFishing;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ModKeyInputEvent implements ClientTickEvents.EndTick {
    private static final KeyMapping.Category KEY_CATEGORY = new KeyMapping.Category(BetterFishing.idOf("main"));

    public static KeyMapping toggleAutoFishingKey;

    public static void register() {
        toggleAutoFishingKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.betterfishing.toggle_auto_fishing",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KEY_CATEGORY
        ));
    }

    @Override
    public void onEndTick(Minecraft client) {
        if (toggleAutoFishingKey.consumeClick()) {
            BetterFishing.getConfig().toggleAutoFishing();

            if(client.player != null) {
                client.player.sendOverlayMessage(
                        Component.translatable("text.betterfishing.auto_fishing." + (BetterFishing.getConfig().isAutoFishingEnabled() ? "enabled" : "disabled"))
                                .withStyle(BetterFishing.getConfig().isAutoFishingEnabled() ? ChatFormatting.GREEN : ChatFormatting.DARK_RED)
                                .append(Component.literal(" "))
                                .append(Component.translatable("text.betterfishing.auto_fishing.auto_fishing").withStyle(ChatFormatting.WHITE))
                );
            }
        }
    }
}
