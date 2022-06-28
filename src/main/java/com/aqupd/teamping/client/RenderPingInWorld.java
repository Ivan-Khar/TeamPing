package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.util.UtilMethods.*;
import static java.lang.Math.*;
import static net.minecraft.client.particle.EntityFX.*;

import com.google.gson.JsonArray;
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
  private static final Minecraft mc = Minecraft.getMinecraft();
  private static final Tessellator tes = Tessellator.getInstance();
  private static final WorldRenderer wr = tes.getWorldRenderer();
  private static Entity e = mc.getRenderViewEntity();
  private static double oldy = 0;
  private static double newy = 0;

  public static void renderBlock(float pticks) {
    try {
      GlStateManager.pushMatrix();
      GlStateManager.pushAttrib();

      GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GlStateManager.enableBlend();
      GlStateManager.disableDepth();
      GlStateManager.disableTexture2D();
      GlStateManager.disableAlpha();

      oldy = e.prevPosY;
      newy = e.posY;
      e = mc.getRenderViewEntity();
      mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pings.png"));
      if (pings.size() != 0) {
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

            long pingtime = data.get("time").getAsLong();
            int lifetime = (int) (System.currentTimeMillis() - pingtime);

            int lifefade = lifetime/2;
            int lifefadeback = 15000/2 - lifefade;
            int trpy = (lifetime > 15000) ? 0 : min(255, (lifefade > 255) ? lifefadeback : lifefade);

            if (dist2d < 6) trpy = trpy/2;

            drawOutline(aabb, color.getRed(), color.getGreen(), color.getBlue(), (int) (trpy/1.5));
            drawBox(aabb, color.getRed(), color.getGreen(), color.getBlue(), trpy/6);

            float bx = jblock.get(0).getAsFloat() + 0.5F;
            float by = jblock.get(1).getAsFloat() + 0.5F;
            float bz = jblock.get(2).getAsFloat() + 0.5F;

            wr.setTranslation(iPX + bx, iPY, iPZ + bz);
            switch (type) {
              case "here":
                renderPing(trpy, 0, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp);
                break;
              case "notice":
                renderPing(trpy, 1, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp);
                break;
              case "question":
                renderPing(trpy, 2, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp);
                break;
              case "no":
                renderPing(trpy, 3, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp);
                break;
              case "yes":
                renderPing(trpy, 4, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp);
                break;
              case "defend":
                renderPing(trpy, 5, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp);
                break;
              case "attack":
                renderPing(trpy, 6, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp);
                break;
              case "mine":
                renderPing(trpy, 7, bx, by, bz, color.getRed(), color.getGreen(), color.getBlue(), pticks, bp);
                break;
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
      GL11.glLineWidth(1);
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

  public static void renderPing(int alpha, int texture, float bx, float by, float bz, int red, int green, int blue, float pticks, BlockPos bp) {
    Tessellator tes = Tessellator.getInstance();
    Vec3 player = new Vec3(e.posX - bx, e.posY - by + e.getEyeHeight(), e.posZ - bz);

    double ypos = 1 + oldy + (newy - oldy) * pticks;

    double yaw = -atan2(player.zCoord, player.xCoord) - PI/2;
    double sinyaw = sin(yaw);
    double cosyaw = cos(yaw);

    GL11.glLineWidth(4);
    wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(0, bp.getY() + 0.5, 0).color(red, green, blue, alpha/2).endVertex();
    wr.pos(0, ypos + 1.5, 0).color(red, green, blue, alpha/2).endVertex();
    tes.draw();
    double minU = 0.125 * texture;
    double maxU = minU + 0.125;
    GlStateManager.enableTexture2D();
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    if ((ypos - bp.getY()) >= -1) {
      wr.pos(cosyaw, ypos + 0.5, -sinyaw).tex(minU, 1).color(red, green, blue, alpha).endVertex(); //Bottom-left
      wr.pos(cosyaw, ypos + 2.5, -sinyaw).tex(minU, 0).color(red, green, blue, alpha).endVertex(); //Top-left
      wr.pos(-cosyaw, ypos + 2.5, sinyaw).tex(maxU, 0).color(red, green, blue, alpha).endVertex(); //Top-right
      wr.pos(-cosyaw, ypos + 0.5, sinyaw).tex(maxU, 1).color(red, green, blue, alpha).endVertex(); //Bottom-right
    } else {
      wr.pos(cosyaw, ypos + 0.5, -sinyaw).tex(minU, 0).color(red, green, blue, alpha).endVertex(); //Bottom-left
      wr.pos(cosyaw, ypos + 2.5, -sinyaw).tex(minU, 1).color(red, green, blue, alpha).endVertex(); //Top-left
      wr.pos(-cosyaw, ypos + 2.5, sinyaw).tex(maxU, 1).color(red, green, blue, alpha).endVertex(); //Top-right
      wr.pos(-cosyaw, ypos + 0.5, sinyaw).tex(maxU, 0).color(red, green, blue, alpha).endVertex(); //Bottom-right
    }
    tes.draw();
    GlStateManager.disableTexture2D();
  }
}