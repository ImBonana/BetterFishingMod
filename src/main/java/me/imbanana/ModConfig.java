package me.imbanana;

import com.google.gson.GsonBuilder;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModConfig implements ModMenuApi {
    public static ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(BetterFishing.idOf("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(BetterFishing.MOD_ID + ".json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build()
            ).build();

    @SerialEntry
    private boolean makeSoundWhenCaughtAFish = true;

    @SerialEntry
    private boolean openWaterDetection = false;

    @SerialEntry
    private boolean autoFishingEnabled = false;

    @SerialEntry
    private int recastDelay = 20;

    @SerialEntry
    private int reelDelay = 10;

    @SerialEntry
    private boolean showRecastTime = true;

    @SerialEntry
    private boolean breakProtection = true;

    @SerialEntry
    private boolean fishingLineColorEnabled = false;

    @SerialEntry
    private boolean animateFishingLineColor = false;

    @SerialEntry
    private float fishingLineAnimationSpeed = 1;

    @SerialEntry
    private boolean reverseAnimationDirection = false;

    @SerialEntry
    private List<Color> fishingLineColors = new ArrayList<>(){{
        add(Color.BLACK);
    }};

    public boolean shouldMakeSoundWhenCaughtAFish() {
        return this.makeSoundWhenCaughtAFish;
    }

    public boolean isOpenWaterDetection() {
        return openWaterDetection;
    }

    public boolean isAutoFishingEnabled() {
        return this.autoFishingEnabled;
    }

    public int getRecastDelay() {
        return this.recastDelay;
    }

    public int getReelDelay() {
        return this.reelDelay;
    }

    public boolean shouldShowRecastTime() {
        return this.showRecastTime;
    }

    public boolean getBreakProtection() {
        return this.breakProtection;
    }

    public boolean isFishingLineColorEnabled() {
        return this.fishingLineColorEnabled;
    }

    public boolean shouldAnimateFishingLineColor() {
        return this.animateFishingLineColor;
    }

    public float getFishingLineAnimationSpeed() {
        return this.fishingLineAnimationSpeed;
    }

    public boolean shouldReverseAnimationDirection() {
        return this.reverseAnimationDirection;
    }

    public List<Color> getFishingLineColors() {
        return this.fishingLineColors;
    }

    public void toggleAutoFishing() {
        this.autoFishingEnabled = !this.autoFishingEnabled;
        ModConfig.HANDLER.save();
    }

    public void disableAutoFishing() {
        this.autoFishingEnabled = false;
        ModConfig.HANDLER.save();
    }

    public YetAnotherConfigLib.Builder createConfigScreen(ModConfig defaults, ModConfig config, YetAnotherConfigLib.Builder builder) {
        Option<Boolean> makeSoundWhenCaughtAFishOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.betterfishing.option.make_sound_when_caught_a_fish"))
                .binding(
                        defaults.shouldMakeSoundWhenCaughtAFish(),
                        config::shouldMakeSoundWhenCaughtAFish,
                        value -> config.makeSoundWhenCaughtAFish = value
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<Boolean> openWaterDetectionOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.betterfishing.option.open_water_detection"))
                .description(OptionDescription.of(Text.translatable("config.betterfishing.option.open_water_detection.desc")))
                .binding(
                        defaults.isOpenWaterDetection(),
                        config::isOpenWaterDetection,
                        value -> config.openWaterDetection = value
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<Integer> recastDelayOption = Option.<Integer>createBuilder()
                .name(Text.translatable("config.betterfishing.option.auto_fishing.recast_delay"))
                .description(OptionDescription.of(Text.translatable("config.betterfishing.option.auto_fishing.recast_delay.desc")))
                .binding(
                        defaults.getRecastDelay(),
                        config::getRecastDelay,
                        value -> config.recastDelay = value
                )
                .controller(option -> IntegerFieldControllerBuilder.create(option)
                        .min(0)
                        .formatValue(v -> Text.literal(String.format("%s ticks", v)))
                )
                .available(config.isAutoFishingEnabled())
                .build();

        Option<Integer> reelDelayOption = Option.<Integer>createBuilder()
                .name(Text.translatable("config.betterfishing.option.auto_fishing.reel_delay"))
                .description(OptionDescription.of(Text.translatable("config.betterfishing.option.auto_fishing.reel_delay.desc")))
                .binding(
                        defaults.getReelDelay(),
                        config::getReelDelay,
                        value -> config.reelDelay = value
                )
                .controller(option -> IntegerFieldControllerBuilder.create(option)
                        .min(0)
                        .formatValue(v -> Text.literal(String.format("%s ticks", v)))
                )
                .available(config.isAutoFishingEnabled())
                .build();

        Option<Boolean> showRecastTimeOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.betterfishing.option.auto_fishing.show_recast_time"))
                .description(OptionDescription.of(Text.translatable("config.betterfishing.option.auto_fishing.show_recast_time.desc")))
                .binding(
                        defaults.shouldShowRecastTime(),
                        config::shouldShowRecastTime,
                        value -> config.showRecastTime = value
                )
                .controller(TickBoxControllerBuilder::create)
                .available(config.isAutoFishingEnabled())
                .build();

        Option<Boolean> breakProtectionOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.betterfishing.option.auto_fishing.break_protection"))
                .description(OptionDescription.of(Text.translatable("config.betterfishing.option.auto_fishing.break_protection.desc")))
                .binding(
                        defaults.getBreakProtection(),
                        config::getBreakProtection,
                        value -> config.breakProtection = value
                )
                .controller(TickBoxControllerBuilder::create)
                .available(config.isAutoFishingEnabled())
                .build();

        Option<Boolean> autoFishingEnabledOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.betterfishing.option.auto_fishing.enable"))
                .binding(
                        defaults.isAutoFishingEnabled(),
                        config::isAutoFishingEnabled,
                        value -> config.autoFishingEnabled = value
                )
                .controller(TickBoxControllerBuilder::create)
                .addListener((option, event) -> {
                    boolean value = option.pendingValue();

                    recastDelayOption.setAvailable(value);
                    reelDelayOption.setAvailable(value);
                    showRecastTimeOption.setAvailable(value);
                    breakProtectionOption.setAvailable(value);
                })
                .build();

        Option<Float> fishingLineAnimationSpeedOption = Option.<Float>createBuilder()
                .name(Text.translatable("config.betterfishing.option.fishing_line.animation_speed"))
                .binding(
                        defaults.getFishingLineAnimationSpeed(),
                        config::getFishingLineAnimationSpeed,
                        value -> config.fishingLineAnimationSpeed = value
                )
                .controller(option -> FloatSliderControllerBuilder.create(option)
                        .range(0f, 1f)
                        .formatValue(value -> Text.literal(String.format("%.1f", value * 100) + "%"))
                        .step(0.001f)
                )
                .available(config.isFishingLineColorEnabled() && config.shouldAnimateFishingLineColor())
                .build();

        Option<Boolean> reverseAnimationDirectionOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.betterfishing.option.fishing_line.reverse"))
                .binding(
                        defaults.shouldReverseAnimationDirection(),
                        config::shouldReverseAnimationDirection,
                        value -> config.reverseAnimationDirection = value
                )
                .controller(TickBoxControllerBuilder::create)
                .available(config.isFishingLineColorEnabled() && config.shouldAnimateFishingLineColor())
                .build();

        Option<Boolean> animateFishingLineColorOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.betterfishing.option.fishing_line.animate"))
                .binding(
                        defaults.shouldAnimateFishingLineColor(),
                        config::shouldAnimateFishingLineColor,
                        value -> config.animateFishingLineColor = value
                )
                .controller(TickBoxControllerBuilder::create)
                .addListener((option, event) -> {
                    fishingLineAnimationSpeedOption.setAvailable(option.pendingValue());
                    reverseAnimationDirectionOption.setAvailable(option.pendingValue());
                })
                .available(config.isFishingLineColorEnabled())
                .build();

        ListOption<Color> fishingLineColorsOption = ListOption.<Color>createBuilder()
                .name(Text.translatable("config.betterfishing.option.fishing_line.colors"))
                .binding(
                        defaults.getFishingLineColors(),
                        config::getFishingLineColors,
                        value -> config.fishingLineColors = value
                )
                .minimumNumberOfEntries(1)
                .maximumNumberOfEntries(16)
                .initial(Color.BLACK)
                .controller(ColorControllerBuilder::create)
                .available(config.isFishingLineColorEnabled())
                .build();

        Option<Boolean> fishingLineColorEnabledOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("config.betterfishing.option.fishing_line.enable"))
                .binding(
                        defaults.isFishingLineColorEnabled(),
                        config::isFishingLineColorEnabled,
                        value -> config.fishingLineColorEnabled = value
                )
                .controller(TickBoxControllerBuilder::create)
                .addListener((option, event) -> {
                    animateFishingLineColorOption.setAvailable(option.pendingValue());
                    fishingLineAnimationSpeedOption.setAvailable(option.pendingValue() && animateFishingLineColorOption.pendingValue());
                    reverseAnimationDirectionOption.setAvailable(option.pendingValue() && animateFishingLineColorOption.pendingValue());
                    fishingLineColorsOption.setAvailable(option.pendingValue());
                })
                .build();

        return builder.title(Text.translatable("config.betterfishing.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.betterfishing.category.betterfishing"))
                        .option(makeSoundWhenCaughtAFishOption)
                        .option(openWaterDetectionOption)
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("config.betterfishing.group.auto_fishing"))
                                .option(autoFishingEnabledOption)
                                .option(recastDelayOption)
                                .option(reelDelayOption)
                                .option(showRecastTimeOption)
                                .option(breakProtectionOption)
                                .build()
                        )
                        .build()
                )
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.betterfishing.group.fishing_line"))
                        .option(fishingLineColorEnabledOption)
                        .option(animateFishingLineColorOption)
                        .option(fishingLineAnimationSpeedOption)
                        .option(reverseAnimationDirectionOption)
                        .group(fishingLineColorsOption)
                        .build()
                );
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> YetAnotherConfigLib.create(HANDLER, this::createConfigScreen).generateScreen(screen);
    }
}
