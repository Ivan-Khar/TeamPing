package com.aqupd.teamping.mixins;

import static com.aqupd.teamping.TeamPing.*;

import com.aqupd.teamping.client.RenderGUI;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class GuiIngameMixin extends Gui {
  @Inject(
    method = "renderGameOverlay(F)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/GuiIngameForge;renderBossHealth()V", ordinal = 0, shift = At.Shift.AFTER)
  )
  private void test(float partialTicks, CallbackInfo ci){
    if(guimenu || timer > 0){
      RenderGUI.render();
    }
  }
}
