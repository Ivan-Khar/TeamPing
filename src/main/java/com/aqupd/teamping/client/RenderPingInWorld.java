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
          JsonArray block = data.get("bp").getAsJsonArray();
          String type = data.get("type").getAsString();
          BlockPos bp = new BlockPos(block.get(0).getAsInt(), block.get(1).getAsInt(), block.get(2).getAsInt());

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
            //drawOutline(aabb.expand(0.005, 0.005, 0.005), 0, 255, 255, trpy*4);
            //drawBox(aabb.expand(0.0025, 0.0025, 0.0025), 0, 255, 255, trpy/3);

            float bx = block.get(0).getAsFloat() + 0.5F;
            float by = block.get(1).getAsFloat() + 0.5F;
            float bz = block.get(2).getAsFloat() + 0.5F;

            wr.setTranslation(iPX + bx, iPY + by, iPZ + bz);
            renderPing(mc, wr, e, Math.min(trpy*24, 255), 0, 0.5, 0, 0.5, bx, by, bz);

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

  public static void renderPing(Minecraft mc, WorldRenderer wr, Entity e, int transparency, double minU, double maxU, double minV, double maxV, float bx, float by, float bz) {
    Tessellator tes = Tessellator.getInstance();

    double playerx = e.posX - bx;
    double playerz = e.posZ - bz;
    double angle = Math.atan(playerx/playerz) + PI/2;
    double x1 = sin(angle) * 0.5;
    double z1 = cos(angle) * 0.5;
    Vec3 vec = new Vec3(e.posX-bz, e.posY-by, e.posZ-bz);
    vec.distanceTo(new Vec3(0, 0, 0));

    GlStateManager.enableTexture2D();
    mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/worldpings.png"));
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    wr.pos(-x1, -0.5, -z1).tex(maxU, maxV).color(255, 255, 255, transparency).endVertex(); //Bottom-left
    wr.pos(-x1, 0.5 , -z1).tex(maxU, minV).color(255, 255, 255, transparency).endVertex(); //Top-left
    wr.pos(x1, 0.5, z1).tex(minU, minV).color(255, 255, 255, transparency).endVertex(); //Top-right
    wr.pos(x1, -0.5, z1).tex(minU, maxV).color(255, 255, 255, transparency).endVertex(); //Bottom-right
    tes.draw();
    GlStateManager.disableTexture2D();

    /*
    System.out.println(""
      + "Pitch: " + pitch + ", "
      + "Yaw: " + yaw + ", "
      + "rX: " + rX + ", "
      + "rZ: " + rZ + ", "
      + "rYZ: " + rYZ + ", "
      + "rXY: " + rXY + ", "
      + "rXZ: " + rXZ);

    wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(-rX - rXY, -rZ, -rYZ - rXZ).color(255, 0, 0, 255).endVertex();
    wr.pos(-rX + rXY, rZ, -rYZ + rXZ).color(255, 0, 0, 255).endVertex();
    wr.pos(-rX + rXY, rZ, -rYZ + rXZ).color(0, 255, 0, 255).endVertex();
    wr.pos(rX + rXY, rZ, rYZ + rXZ).color(0, 255, 0, 255).endVertex();
    wr.pos(rX + rXY, rZ, rYZ + rXZ).color(0, 0, 255, 255).endVertex();
    wr.pos(rX - rXY, -rZ, rYZ - rXZ).color(0, 0, 255, 255).endVertex();
    wr.pos(rX - rXY, -rZ, rYZ - rXZ).color(0, 0, 255, 255).endVertex();
    wr.pos(-rX - rXY, -rZ, -rYZ - rXZ).color(255, 255, 0, 255).endVertex();
    tes.draw();
    */
  }
}