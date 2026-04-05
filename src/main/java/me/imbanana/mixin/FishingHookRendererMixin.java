package me.imbanana.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.imbanana.BetterFishing;
import me.imbanana.renderer.FishingLineRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHookRenderer.class)
public abstract class FishingHookRendererMixin extends EntityRenderer<FishingHook, FishingHookRenderState> {

    protected FishingHookRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @ModifyExpressionValue(
            method = "lambda$submit$1",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=16"
            )
    )
    private static int modifyRenderSegmentCountAnInt(int original) {
        return FishingLineRenderer.getSegmentCount();
    }

    @Inject(method = "stringVertex", at = @At(value = "HEAD"))
    private static void getVarsFromRenderFishingLine(float x, float y, float z, VertexConsumer buffer, PoseStack.Pose stringPose, float segmentStart, float segmentEnd, float getMinimumLineWidth, CallbackInfo ci) {
        FishingLineRenderer.setSegmentIndex(segmentStart);
    }

    @ModifyArg(method = "stringVertex", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private static int renderFishingLineModifyColor(int original) {
        return BetterFishing.getConfig().isFishingLineColorEnabled() ? FishingLineRenderer.renderFishingLineColor() : original;
    }
}
