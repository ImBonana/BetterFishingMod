package me.imbanana.renderer;

import me.imbanana.BetterFishing;
import me.imbanana.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Colors;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;

public class FishingLineRenderer {
    public static int segmentIndex = 0;
    public static float animationProgress = 0;

    public static int renderFishingLineColor() {
        ModConfig config = BetterFishing.getConfig();

        float pos = (float) segmentIndex / getSegmentCount();
        float animationSpeed = config.shouldAnimateFishingLineColor() ? config.getFishingLineAnimationSpeed() : 0;
        float dir = config.shouldReverseAnimationDirection() ? -1f : 1f;
        animationProgress = wrapOnce(animationProgress + MinecraftClient.getInstance().getRenderTickCounter().getFixedDeltaTicks() * animationSpeed * 0.015f * dir, 1f) ;

        float delta = (pos + animationProgress) % 1.0f;
        if (delta < 0) delta += 1.0f;
        float smoothDelta = (0.5f - Math.abs(delta - 0.5f)) * 2.0f;
        smoothDelta = smoothDelta * smoothDelta * (3f - 2f * smoothDelta);

        float finalDelta = config.shouldAnimateFishingLineColor() ? smoothDelta : pos;

        int colorIdx = segmentPosToColorIdx(finalDelta);
        List<Color> colorList = config.getFishingLineColors().reversed();
        return lerpColor(finalDelta, colorList.get(colorIdx).getRGB(), colorList.get(colorIdx + 1 >= colorList.size() ? (config.shouldAnimateFishingLineColor() ? 0 : colorIdx) : colorIdx + 1).getRGB());
    }

    private static int lerpColor(float delta, int start, int end) {
        int startAlpha = (start >> 24) & 0xFF;
        int startR = (start >> 16) & 0xFF;
        int startG = (start >> 8) & 0xFF;
        int startB = start & 0xFF;

        int endAlpha = (end >> 24) & 0xFF;
        int endR = (end >> 16) & 0xFF;
        int endG = (end >> 8) & 0xFF;
        int endB = end & 0xFF;

        int finalAlpha = MathHelper.lerp(delta, startAlpha, endAlpha);
        int finalR = MathHelper.lerp(delta, startR, endR);
        int finalG = MathHelper.lerp(delta, startG, endG);
        int finalB = MathHelper.lerp(delta, startB, endB);

        return (finalAlpha << 24) | (finalR << 16) | (finalG << 8) | finalB;
    }

    private static float wrapOnce(float a, float b) {
        return a > b ? a - b : a;
    }

    private static int segmentPosToColorIdx(float pos) {
        return (int) (pos * (BetterFishing.getConfig().getFishingLineColors().size() - 1));
    }

    public static int getSegmentCount() {
        return 16;
    }

    public static void setSegmentIndex(float segmentStart) {
        segmentIndex = getSegmentIdx(segmentStart);
    }

    private static int getSegmentIdx(float x) {
        return Math.round(x * getSegmentCount());
    }
}
