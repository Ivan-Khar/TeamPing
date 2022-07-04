package com.aqupd.teamping.util;

import static com.aqupd.teamping.listeners.EventListener.ticks;

import com.google.gson.JsonParser;
import java.awt.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

public class UtilMethods {
  public static double distanceTo3D(Entity e, BlockPos bp) {
    return Math.sqrt(Math.pow(e.getPositionEyes(ticks).xCoord - bp.getX(), 2) + Math.pow(e.getPositionEyes(ticks).yCoord - bp.getY(), 2) + Math.pow(e.getPositionEyes(ticks).zCoord - bp.getZ(), 2));
  }

  public static double distanceTo2D(Entity e, BlockPos bp) {
    return Math.sqrt(Math.pow(e.getPositionEyes(ticks).xCoord - bp.getX(), 2) + Math.pow(e.getPositionEyes(ticks).zCoord - bp.getZ(), 2));
  }

  public static boolean isValidJsonObject(String input) {
    try {
      new JsonParser().parse(input).getAsJsonObject();
      return true;
    } catch (IllegalStateException ex) {
      return false;
    }
  }

  public static void drawRect(int x, int y, int width, int height, Color color) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    GlStateManager.enableBlend();
    GlStateManager.disableTexture2D();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(x, y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    worldrenderer.pos(x, y + height, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    worldrenderer.pos(x + width, y + height, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    worldrenderer.pos(x + width, y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    tessellator.draw();
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
  }
}
