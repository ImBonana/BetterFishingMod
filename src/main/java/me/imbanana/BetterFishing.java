package me.imbanana;

import me.imbanana.events.ModAutoFishingEvent;
import me.imbanana.events.ModFishCaughtSoundEvent;
import me.imbanana.events.ModKeyInputEvent;
import me.imbanana.events.ModOpenWaterDetectionEvent;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterFishing implements ClientModInitializer {
	public static final String MOD_ID = "betterfishing";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
        ModConfig.HANDLER.load();
        ModKeyInputEvent.register();

        ClientTickEvents.END_CLIENT_TICK.register(new ModAutoFishingEvent());
        ClientTickEvents.END_CLIENT_TICK.register(new ModFishCaughtSoundEvent());
        ClientTickEvents.END_CLIENT_TICK.register(new ModOpenWaterDetectionEvent());
        ClientTickEvents.END_CLIENT_TICK.register(new ModKeyInputEvent());
	}

    public static ModConfig getConfig() {
        return ModConfig.HANDLER.instance();
    }

    public static Identifier idOf(String path) {
        return Identifier.of(MOD_ID, path);
    }
}