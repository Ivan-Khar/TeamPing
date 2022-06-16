package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.MOD_ID;
import static com.aqupd.teamping.TeamPing.pings;
import static com.aqupd.teamping.listeners.EventListener.ticks;
import static com.aqupd.teamping.setup.Registrations.keyBindings;
import static com.aqupd.teamping.util.UtilMethods.*;
import static java.lang.Math.*;
import static net.minecraft.client.particle.EntityFX.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPingInWorld {
  public static void renderBlock() {
    try {
      GlStateManager.pushMatrix();
      GlStateManager.pushAttrib();

      GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GlStateManager.enableBlend();
      GlStateManager.disableDepth();
      GlStateManager.disableTexture2D();
      GlStateManager.disableAlpha();

      Minecraft mc = Minecraft.getMinecraft();
      Tessellator tes = Tessellator.getInstance();
      WorldRenderer wr = tes.getWorldRenderer();
      Entity e = mc.getRenderViewEntity();

      if(pings.size() != 0) {
        for (JsonElement je: pings) {
          JsonObject data = je.getAsJsonObject();
          JsonArray jblock = data.get("bp").getAsJsonArray();
          JsonArray jcolor = data.get("color").getAsJsonArray();
          String type = data.get("type").getAsString();

          Color color = new Color(jcolor.get(0).getAsInt(), jcolor.get(1).getAsInt(), jcolor.get(2).getAsInt());
          BlockPos bp = new BlockPos(jblock.get(0).getAsInt(), jblock.get(1).getAsInt(), jblock.get(2).getAsInt());

          double dist = distanceTo3D(e, bp, ticks);
          double dist2d = distanceTo2D(e, bp, ticks);

          if (dist2d < Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16) {
            double iPX = -interpPosX;
            double iPY = -interpPosY;
            double iPZ = -interpPosZ;
            wr.setTranslation(iPX, iPY,iPZ);

            GL11.glLineWidth((float) (10 / dist));
            AxisAlignedBB aabb = new AxisAlignedBB(bp, bp.add(1, 1, 1));
            int lifetime = data.get("lifetime").getAsInt();

            int trpy;
            if (lifetime >= (500 + 63)){
              trpy = (500+63*2)-lifetime;
            } else {
              trpy = Math.min(lifetime, 63);
            }

            if(dist2d < 6) trpy = trpy/2;

            drawOutline(aabb, color.getRed(), color.getGreen(), color.getBlue(), trpy);
            drawBox(aabb, color.getRed(), color.getGreen(), color.getBlue(), trpy/3);

            float bx = jblock.get(0).getAsFloat() + 0.5F;
            float by = jblock.get(1).getAsFloat() + 0.5F;
            float bz = jblock.get(2).getAsFloat() + 0.5F;

            wr.setTranslation(iPX + bx, iPY + by, iPZ + bz);
            switch(type){
              case "here":
                renderPing(mc, wr, e, trpy, 64, 0, 32, 0, 32, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), dist2d);
                break;
              case "danger":
                renderPing(mc, wr, e, trpy, 64, 32, 64, 0, 32, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), dist2d);
                break;
              case "question":
                renderPing(mc, wr, e, trpy, 64, 0, 32, 32, 64, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), dist2d);
                break;
              case "no":
                renderPing(mc, wr, e, trpy, 64, 32, 64, 32, 64, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), dist2d);
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
      GlStateManager.enableAlpha();
      GlStateManager.popAttrib();
      GlStateManager.popMatrix();
      if (keyBindings[1].isPressed() && pings.size() != 0) {

      }
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

  public static void renderPing(Minecraft mc, WorldRenderer wr, Entity e, int transparency, int resolution, float minU, float maxU, float minV, float maxV, float bx, float by, float bz, int red, int green, int blue, double distance) {
    Tessellator tes = Tessellator.getInstance();
    Vec3 player = new Vec3(e.posX - bx, e.posY - by + e.getEyeHeight(), e.posZ - bz);

    float minU1 = minU / resolution;
    float maxU1 = maxU / resolution;
    float minV1 = minV / resolution;
    float maxV1 = maxV / resolution;

    double yaw = -atan2(player.zCoord, player.xCoord) - PI/2;
    double sinyaw = sin(yaw);
    double cosyaw = cos(yaw);

    GlStateManager.enableTexture2D();
    mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/worldpings.png"));
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

    if(player.yCoord >= -1) {
      wr.pos(cosyaw, player.yCoord + 0.5, -sinyaw).tex(minU1, maxV1).color(255, 255, 255, transparency * 4).endVertex(); //Bottom-left
      wr.pos(cosyaw, player.yCoord + 2.5, -sinyaw).tex(minU1, minV1).color(255, 255, 255, transparency * 4).endVertex(); //Top-left
      wr.pos(-cosyaw, player.yCoord + 2.5, sinyaw).tex(maxU1, minV1).color(255, 255, 255, transparency * 4).endVertex(); //Top-right
      wr.pos(-cosyaw, player.yCoord + 0.5, sinyaw).tex(maxU1, maxV1).color(255, 255, 255, transparency * 4).endVertex(); //Bottom-right
    } else {
      wr.pos(cosyaw, player.yCoord + 0.5, -sinyaw).tex(minU1, minV1).color(255, 255, 255, transparency * 4).endVertex(); //Bottom-left
      wr.pos(cosyaw, player.yCoord + 2.5, -sinyaw).tex(minU1, maxV1).color(255, 255, 255, transparency * 4).endVertex(); //Top-left
      wr.pos(-cosyaw, player.yCoord + 2.5, sinyaw).tex(maxU1, maxV1).color(255, 255, 255, transparency * 4).endVertex(); //Top-right
      wr.pos(-cosyaw, player.yCoord + 0.5, sinyaw).tex(maxU1, minV1).color(255, 255, 255, transparency * 4).endVertex(); //Bottom-right
    }

    tes.draw();
    GlStateManager.disableTexture2D();

    GL11.glLineWidth(2);
    wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    if(player.yCoord >= -1) {
      wr.pos(0, 0, 0).color(red, green, blue, transparency).endVertex();
      wr.pos(0, player.yCoord + 0.75, 0).color(red, green, blue, transparency).endVertex();
    } else {
      wr.pos(0, 0, 0).color(red, green, blue, transparency).endVertex();
      wr.pos(0, player.yCoord + 1.25, 0).color(red, green, blue, transparency).endVertex();
    }
    tes.draw();
  }
}