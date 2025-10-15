package me.imbanana.events;

import me.imbanana.BetterFishing;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ModKeyInputEvent implements ClientTickEvents.EndTick {
    private static final KeyBinding.Category KEY_CATEGORY = new KeyBinding.Category(BetterFishing.idOf("main"));

    public static KeyBinding toggleAutoFishingKey;

    public static void register() {
        toggleAutoFishingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterfishing.toggle_auto_fishing",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KEY_CATEGORY
        ));
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        if (toggleAutoFishingKey.wasPressed()) {
            BetterFishing.getConfig().toggleAutoFishing();

            if(client.player != null) {
                client.player.sendMessage(
                        Text.translatable("text.betterfishing.auto_fishing." + (BetterFishing.getConfig().isAutoFishingEnabled() ? "enabled" : "disabled"))
                                .formatted(BetterFishing.getConfig().isAutoFishingEnabled() ? Formatting.GREEN : Formatting.DARK_RED)
                                .append(Text.literal(" "))
                                .append(Text.translatable("text.betterfishing.auto_fishing.auto_fishing").formatted(Formatting.WHITE)),
                        true
                );
            }
        }
    }
}
