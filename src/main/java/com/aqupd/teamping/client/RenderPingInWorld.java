package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
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
  public static void renderBlock(float pticks) {
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

      double oldy = e.prevPosY;
      double newy = e.posY;

      if(pings.size() != 0) {
        for (JsonObject data: pings) {
          JsonArray jblock = data.get("bp").getAsJsonArray();
          JsonArray jcolor = data.get("color").getAsJsonArray();
          String type = data.get("type").getAsString();

          Color color = new Color(jcolor.get(0).getAsInt(), jcolor.get(1).getAsInt(), jcolor.get(2).getAsInt());
          BlockPos bp = new BlockPos(jblock.get(0).getAsInt(), jblock.get(1).getAsInt(), jblock.get(2).getAsInt());

          double dist = distanceTo3D(e, bp);
          double dist2d = distanceTo2D(e, bp);

          if (dist2d < Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16) {
            double iPX = -interpPosX;
            double iPY = -interpPosY;
            double iPZ = -interpPosZ;
            wr.setTranslation(iPX, iPY, iPZ);

            GL11.glLineWidth((float) (10 / dist));
            AxisAlignedBB aabb = new AxisAlignedBB(bp, bp.add(1, 1, 1));
            int lifetime = data.get("lifetime").getAsInt();

            int trpy;
            if (lifetime >= (500 + 31)){
              trpy = (500+31*2)-lifetime;
            } else {
              trpy = Math.min(lifetime, 31);
            }

            if(dist2d < 6) trpy = trpy/2;

            drawOutline(aabb, color.getRed(), color.getGreen(), color.getBlue(), trpy*4);
            drawBox(aabb, color.getRed(), color.getGreen(), color.getBlue(), trpy*2);

            float bx = jblock.get(0).getAsFloat() + 0.5F;
            float by = jblock.get(1).getAsFloat() + 0.5F;
            float bz = jblock.get(2).getAsFloat() + 0.5F;

            wr.setTranslation(iPX + bx, iPY, iPZ + bz);
            switch(type){
              case "here":
                renderPing(mc, wr, e, trpy, 64, 0, 32, 0, 32, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp, oldy, newy);
                break;
              case "danger":
                renderPing(mc, wr, e, trpy, 64, 32, 64, 0, 32, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp, oldy, newy);
                break;
              case "question":
                renderPing(mc, wr, e, trpy, 64, 0, 32, 32, 64, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp, oldy, newy);
                break;
              case "no":
                renderPing(mc, wr, e, trpy, 64, 32, 64, 32, 64, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp, oldy, newy);
            }
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

  public static void renderPing(Minecraft mc, WorldRenderer wr, Entity e, int transparency, int resolution, float minU, float maxU, float minV, float maxV, float bx, float by, float bz, int red, int green, int blue, float pticks, BlockPos bp, double oldy, double newy) {
    Tessellator tes = Tessellator.getInstance();
    Vec3 player = new Vec3(e.posX - bx, e.posY - by + e.getEyeHeight(), e.posZ - bz);

    double ypos = 1 + oldy + (newy - oldy) * pticks;

    float minU1 = minU / resolution;
    float maxU1 = maxU / resolution;
    float minV1 = minV / resolution;
    float maxV1 = maxV / resolution;

    double yaw = -atan2(player.zCoord, player.xCoord) - PI/2;
    double sinyaw = sin(yaw);
    double cosyaw = cos(yaw);

    GL11.glLineWidth(2);
    wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(0, bp.getY(), 0).color(red, green, blue, transparency*4).endVertex();
    wr.pos(0, ypos + 1.5, 0).color(red, green, blue, transparency*4).endVertex();
    tes.draw();

    GlStateManager.enableTexture2D();
    mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/worldpings.png"));
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

    if(ypos - bp.getY()>= -1) {
      wr.pos(cosyaw, ypos + 0.5, -sinyaw).tex(minU1, maxV1).color(255, 255, 255, transparency * 8).endVertex(); //Bottom-left
      wr.pos(cosyaw, ypos + 2.5, -sinyaw).tex(minU1, minV1).color(255, 255, 255, transparency * 8).endVertex(); //Top-left
      wr.pos(-cosyaw, ypos + 2.5, sinyaw).tex(maxU1, minV1).color(255, 255, 255, transparency * 8).endVertex(); //Top-right
      wr.pos(-cosyaw, ypos + 0.5, sinyaw).tex(maxU1, maxV1).color(255, 255, 255, transparency * 8).endVertex(); //Bottom-right
    } else {
      wr.pos(cosyaw, ypos + 0.5, -sinyaw).tex(minU1, minV1).color(255, 255, 255, transparency * 8).endVertex(); //Bottom-left
      wr.pos(cosyaw, ypos + 2.5, -sinyaw).tex(minU1, maxV1).color(255, 255, 255, transparency * 8).endVertex(); //Top-left
      wr.pos(-cosyaw, ypos + 2.5, sinyaw).tex(maxU1, maxV1).color(255, 255, 255, transparency * 8).endVertex(); //Top-right
      wr.pos(-cosyaw, ypos + 0.5, sinyaw).tex(maxU1, minV1).color(255, 255, 255, transparency * 8).endVertex(); //Bottom-right
    }
    tes.draw();
    GlStateManager.disableTexture2D();
  }
}