package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.MOD_ID;
import static com.aqupd.teamping.TeamPing.pings;
import static com.aqupd.teamping.listeners.EventListener.ticks;
import static com.aqupd.teamping.util.UtilMethods.distanceTo;
import static net.minecraft.client.particle.EntityFX.*;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPingInWorld {

  public static void renderBlock(DrawBlockHighlightEvent event) {
    try {
      GlStateManager.pushMatrix();
      GlStateManager.pushAttrib();
      GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GlStateManager.enableBlend();
      GlStateManager.disableDepth();
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      Minecraft mc = Minecraft.getMinecraft();
      Tessellator tes = Tessellator.getInstance();
      WorldRenderer wr = tes.getWorldRenderer();
      Entity e = mc.getRenderViewEntity();
      if(pings.size() != 0) {
        for (JsonElement je : pings) {
          JsonObject data = je.getAsJsonObject();
          JsonArray block = data.get("bp").getAsJsonArray();
          String type = data.get("type").getAsString();
          BlockPos bp = new BlockPos(block.get(0).getAsInt(), block.get(1).getAsInt(), block.get(2).getAsInt());

          if (distanceTo(e, bp, ticks) < Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16) {
            double d0 = -(e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) ticks);
            double d1 = -(e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) ticks);
            double d2 = -(e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) ticks);
            wr.setTranslation(d0, d1, d2);

            GL11.glLineWidth(10 / distanceTo(e, bp, ticks));
            AxisAlignedBB aabb = new AxisAlignedBB(bp, bp.add(1, 1, 1));
            int lifetime = data.get("lifetime").getAsInt();
            int trpy;
            if (lifetime >= (500 + 255)){
              trpy = (500+255+255)-lifetime;
            } else {
              trpy = Math.min(lifetime, 255);
            }

            drawOutline(aabb.expand(0.005, 0.005, 0.005), 0, 255, 255, trpy);
            drawBox(aabb.expand(0.0025, 0.0025, 0.0025), 0, 255, 255, trpy/12);

            float bx = block.get(0).getAsFloat() + 0.5F;
            float by = block.get(1).getAsFloat() + 0.5F;
            float bz = block.get(2).getAsFloat() + 0.5F;

            wr.setTranslation(d0 + bx, d1 + by, d2 + bz);
            renderPing(mc, wr, e, 0, 0.5);

          }
          int lifetime = data.get("lifetime").getAsInt();
          if (lifetime <= 0) {
            pings.remove(je);
          }
        }
      }
    } catch(Exception e) {
      WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
      try {
        wr.finishDrawing();
      } catch (IllegalStateException ignored) {}
    } finally {
      WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
      wr.setTranslation(0, 0, 0);
      GlStateManager.enableTexture2D();
      GlStateManager.enableDepth();
      GlStateManager.enableLighting();
      GlStateManager.popAttrib();
      GlStateManager.popMatrix();
    }
  }

  public static void drawOutline(AxisAlignedBB boundingBox,int red, int green, int blue, int alpha) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer wr = tessellator.getWorldRenderer();
    wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
  }

  public static void drawBox(AxisAlignedBB boundingBox,int red, int green, int blue, int alpha) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer wr = tessellator.getWorldRenderer();
    //down
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //north
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //west
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //east
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //south
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //up
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    wr.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
  }

  public static void renderPing(Minecraft mc, WorldRenderer wr, Entity e, double u, double v)
  {
    Tessellator tes = Tessellator.getInstance();

    double iPX = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)ticks;
    double iPY = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)ticks;
    double iPZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)ticks;
    float f5 = (float)(e.prevPosX + (e.posX - e.prevPosX) * (double)ticks - iPX);
    float f6 = (float)(e.prevPosY + (e.posY - e.prevPosY) * (double)ticks - iPY);
    float f7 = (float)(e.prevPosZ + (e.posZ - e.prevPosZ) * (double)ticks - iPZ);

    float rZ = ActiveRenderInfo.getRotationX();
    float rX = ActiveRenderInfo.getRotationZ();
    float rXY = ActiveRenderInfo.getRotationXY();
    float rXZ = ActiveRenderInfo.getRotationXZ();
    float rYZ = ActiveRenderInfo.getRotationYZ();
    GlStateManager.enableTexture2D();
    mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/worldpings.png"));

    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    wr.pos(f5 - rX * 0.5F - rXY * 0.5F, f6 - rZ * 0.5F, f7 - rYZ * 0.5F - rXZ * 0.5F).tex(u, v).color(255, 255, 255, 255).endVertex();
    wr.pos(f5 - rX * 0.5F + rXY * 0.5F, f6 + rZ * 0.5F, f7 - rYZ * 0.5F + rXZ * 0.5F).tex(u, u).color(255, 255, 255, 255).endVertex();
    wr.pos(f5 + rX * 0.5F + rXY * 0.5F, f6 + rZ * 0.5F, f7 + rYZ * 0.5F + rXZ * 0.5F).tex(v, u).color(255, 255, 255, 255).endVertex();
    wr.pos(f5 + rX * 0.5F - rXY * 0.5F, f6 - rZ * 0.5F, f7 + rYZ * 0.5F - rXZ * 0.5F).tex(v, v).color(255, 255, 255, 255).endVertex();
    tes.draw();
    GlStateManager.disableTexture2D();
  }
}