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
import net.minecraft.util.MathHelper;
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
            renderPing(mc, wr, e, Math.min(trpy*24, 255), 0, 0.5, 0, 0.5);

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

  public static void renderPing(Minecraft mc, WorldRenderer wr, Entity e, int transparency, double minU, double maxU, double minV, double maxV)
  {
    Tessellator tes = Tessellator.getInstance();
    float blockX = (float)(e.prevPosX + (e.posX - e.prevPosX) * (double)ticks - interpPosX);
    float blockY = (float)(e.prevPosY + (e.posY - e.prevPosY) * (double)ticks - interpPosY);
    float blockZ = (float)(e.prevPosZ + (e.posZ - e.prevPosZ) * (double)ticks - interpPosZ);

    float rX = ActiveRenderInfo.getRotationX() * 0.5F;
    float rZ = ActiveRenderInfo.getRotationZ() * 0.5F;
    float rYZ = ActiveRenderInfo.getRotationYZ() * 0.5F;
    float rXY = ActiveRenderInfo.getRotationXY() * 0.5F;
    float rXZ = ActiveRenderInfo.getRotationXZ() * 0.5F;

    /*
    GlStateManager.enableTexture2D();
    mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/worldpings.png"));
    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    wr.pos(blockX - rX - rXY, blockY - rZ, blockZ - rYZ - rXZ).tex(maxU, maxV).color(255, 255, 255, transparency).endVertex();
    wr.pos(blockX - rX + rXY, blockY + rZ, blockZ - rYZ + rXZ).tex(maxU, minV).color(255, 255, 255, transparency).endVertex();
    wr.pos(blockX + rX + rXY, blockY + rZ, blockZ + rYZ + rXZ).tex(minU, minV).color(255, 255, 255, transparency).endVertex();
    wr.pos(blockX + rX - rXY, blockY - rZ, blockZ + rYZ - rXZ).tex(minU, maxV).color(255, 255, 255, transparency).endVertex();
    tes.draw();
    GlStateManager.disableTexture2D();

    */
    wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    wr.pos(blockX - rX - rXY, blockY - rZ, blockZ - rYZ - rXZ).color(255, 0, 0, 255).endVertex();
    wr.pos(blockX - rX + rXY, blockY + rZ, blockZ - rYZ + rXZ).color(255, 0, 0, 255).endVertex();
    wr.pos(blockX - rX + rXY, blockY + rZ, blockZ - rYZ + rXZ).color(0, 255, 0, 255).endVertex();
    wr.pos(blockX + rX + rXY, blockY + rZ, blockZ + rYZ + rXZ).color(0, 255, 0, 255).endVertex();
    wr.pos(blockX + rX + rXY, blockY + rZ, blockZ + rYZ + rXZ).color(0, 0, 255, 255).endVertex();
    wr.pos(blockX + rX - rXY, blockY - rZ, blockZ + rYZ - rXZ).color(0, 0, 255, 255).endVertex();
    wr.pos(blockX + rX - rXY, blockY - rZ, blockZ + rYZ - rXZ).color(0, 0, 255, 255).endVertex();
    wr.pos(blockX - rX - rXY, blockY - rZ, blockZ - rYZ - rXZ).color(255, 255, 0, 255).endVertex();
    tes.draw();
  }
}