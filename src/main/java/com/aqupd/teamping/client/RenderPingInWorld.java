package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.MOD_ID;
import static com.aqupd.teamping.TeamPing.pings;
import static com.aqupd.teamping.listeners.EventListener.ticks;
import static com.aqupd.teamping.util.UtilMethods.distanceTo;
import static java.lang.Math.*;
import static net.minecraft.client.particle.EntityFX.*;
import static net.minecraft.util.MathHelper.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
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
          JsonArray jblock = data.get("bp").getAsJsonArray();
          JsonArray jcolor = data.get("color").getAsJsonArray();
          String type = data.get("type").getAsString();

          Color color = new Color(jcolor.get(0).getAsInt(), jcolor.get(1).getAsInt(), jcolor.get(2).getAsInt());
          BlockPos bp = new BlockPos(jblock.get(0).getAsInt(), jblock.get(1).getAsInt(), jblock.get(2).getAsInt());

          if (distanceTo(e, bp, ticks) < Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16) {
            double iPX = -interpPosX;
            double iPY = -interpPosY;
            double iPZ = -interpPosZ;
            wr.setTranslation(iPX, iPY,iPZ);

            GL11.glLineWidth(10 / distanceTo(e, bp, ticks));
            AxisAlignedBB aabb = new AxisAlignedBB(bp, bp.add(1, 1, 1));
            int lifetime = data.get("lifetime").getAsInt();
            int trpy;
            if (lifetime >= (500 + 63)){
              trpy = (500+63*2)-lifetime;
            } else {
              trpy = Math.min(lifetime, 63);
            }
            //drawOutline(aabb.expand(0.005, 0.005, 0.005), color.getRed(), color.getGreen(), color.getBlue(), trpy*4);
            //drawBox(aabb.expand(0.0025, 0.0025, 0.0025), color.getRed(), color.getGreen(), color.getBlue(), trpy/3);

            float bx = jblock.get(0).getAsFloat() + 0.5F;
            float by = jblock.get(1).getAsFloat() + 0.5F;
            float bz = jblock.get(2).getAsFloat() + 0.5F;

            wr.setTranslation(iPX + bx, iPY + by, iPZ + bz);
            trpy = Math.min(trpy*24, 191);
            switch(type){
              case "here":
                renderPing(mc, wr, e, trpy, 0, 32, 0, 32, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue());
                break;
              case "danger":
                renderPing(mc, wr, e, trpy, 32, 64, 0, 32, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue());
                break;
              case "question":
                renderPing(mc, wr, e, trpy, 0, 32, 32, 64, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue());
                break;
              case "no":
                renderPing(mc, wr, e, trpy, 32, 64, 32, 64, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue());
            }
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

  public static void renderPing(Minecraft mc, WorldRenderer wr, Entity e, int transparency, float minU, float maxU, float minV, float maxV, float bx, float by, float bz, int red, int green, int blue) {
    Tessellator tes = Tessellator.getInstance();

    float minU1 = minU / 64;
    float maxU1 = maxU / 64;
    float minV1 = minV / 64;
    float maxV1 = maxV / 64;

    double playerx = e.posX - bx;
    double playery = e.posY - by + e.getEyeHeight();
    double playerz = e.posZ - bz;

    double xzangle1 = Math.atan(playerx/playerz) + PI/2;
    double xzangle2 = Math.atan(playerx/playerz);
    double xyangle = Math.atan(playerx/playery) + PI/2;
    double zyangle = Math.atan(playerz/playery) + PI/2;
    double sinxz1 = sin(xzangle1) * 0.5;
    double cosxz1 = cos(xzangle1) * 0.5;
    double sinxz2 = sin(xzangle2) * 0.5;
    double cosxz2 = cos(xzangle2) * 0.5;
    double sinxy = sin(xyangle) * 0.5;
    double cosxy = cos(xyangle) * 0.5;
    double sinzy = sin(zyangle) * 0.5;
    double coszy = cos(zyangle) * 0.5;
    /*
    GL11.glLineWidth(4);
    wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(0, 0, 0).color(red, green, blue, transparency).endVertex();
    wr.pos(0, y-0.25, 0).color(red, green, blue, transparency).endVertex();
    tes.draw();
    */
    GlStateManager.enableTexture2D();
    mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/worldpings.png"));
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    //double an = wrapAngleTo180_double(Math.atan2(playerx, playerz) * 180/PI);
    //xy rotation + xz rotation
    wr.pos(-sinxz1 - sinxy, -cosxy, -cosxz1).tex(maxU1, minV1).color(255, 255, 255, transparency).endVertex(); //Bottom-left
    wr.pos(-sinxz1 + sinxy, cosxy, -cosxz1).tex(maxU1, maxV1).color(255, 255, 255, transparency).endVertex(); //Top-left
    wr.pos(sinxz1 + sinxy, cosxy, cosxz1).tex(minU1, maxV1).color(255, 255, 255, transparency).endVertex(); //Top-right
    wr.pos(sinxz1 - sinxy, -cosxy, cosxz1).tex(minU1, minV1).color(255, 255, 255, transparency).endVertex(); //Bottom-right

    //zy rotation + xz rotation
    wr.pos(cosxz2, -coszy, -sinxz2 - sinzy).tex(maxU1, minV1).color(255, 255, 255, transparency).endVertex(); //Bottom-left
    wr.pos(cosxz2, coszy, -sinxz2 + sinzy).tex(maxU1, maxV1).color(255, 255, 255, transparency).endVertex(); //Top-left
    wr.pos(-cosxz2, coszy, sinxz2 + sinzy).tex(minU1, maxV1).color(255, 255, 255, transparency).endVertex(); //Top-right
    wr.pos(-cosxz2, -coszy, sinxz2 - sinzy).tex(minU1, minV1).color(255, 255, 255, transparency).endVertex(); //Bottom-right

    tes.draw();
    GlStateManager.disableTexture2D();
  }
}