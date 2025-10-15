package me.imbanana.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.imbanana.BetterFishing;
import me.imbanana.renderer.FishingLineRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.render.entity.state.FishingBobberEntityState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntityRenderer.class)
public abstract class FishingBobberEntityRendererMixin extends EntityRenderer<FishingBobberEntity, FishingBobberEntityState> {
    protected FishingBobberEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @ModifyExpressionValue(
            method = "method_72983",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=16"
            )
    )
    private static int modifyRenderSegmentCountAnInt(int original) {
        return FishingLineRenderer.getSegmentCount();
    }

    @Inject(method = "renderFishingLine", at = @At(value = "HEAD"))
    private static void getVarsFromRenderFishingLine(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd, CallbackInfo ci) {
        FishingLineRenderer.setSegmentIndex(segmentStart);
    }

    @ModifyArg(method = "renderFishingLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;"))
    private static int renderFishingLineModifyColor(int original) {
        return BetterFishing.getConfig().isFishingLineColorEnabled() ? FishingLineRenderer.renderFishingLineColor() : original;
    }
}
