package com.aqupd.teamping.mixins;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.listeners.EventListener.socket;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

  @Inject(
    method = "clickMouse()V",
    at = @At(value = "HEAD"),
    cancellable = true
  )
  private void mixin1(CallbackInfo ci){
    if(guimenu) ci.cancel();
  }

  @Inject(
    method = "sendClickBlockToController(Z)V",
    at = @At(value = "HEAD"),
    cancellable = true
  )
  private void mixin2(CallbackInfo ci){
    if(guimenu) ci.cancel();
  }

  @Inject(
    method = "shutdownMinecraftApplet()V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/stream/IStream;shutdownStream()V")
  )
  private void mixin3(CallbackInfo ci){
    stoppingmc = true;
    try {
      if(socket.isConnected()) socket.close();
    } catch (IOException e) {
      LOGGER.error("Server exception", e);
    }
  }
}
