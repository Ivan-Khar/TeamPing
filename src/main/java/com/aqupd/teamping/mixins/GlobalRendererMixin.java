package com.aqupd.teamping.mixins;

import com.aqupd.teamping.client.RenderPingInWorld;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class GlobalRendererMixin {
  @Inject(
    method = "renderWorldPass(IFJ)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;matrixMode(I)V", ordinal = 7)
  )
  private void mixin(CallbackInfo ci){
    RenderPingInWorld.renderBlock();
  }
}
