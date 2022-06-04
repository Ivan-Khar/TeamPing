package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.coords;
import static com.aqupd.teamping.listeners.EventListener.ticks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPing {

  public static void render(DrawBlockHighlightEvent event){
    try {
      GlStateManager.pushMatrix();
      GlStateManager.pushAttrib();
      GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();

      WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
      Entity entity = Minecraft.getMinecraft().getRenderViewEntity();

      for(BlockPos bp: coords){

        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)ticks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)ticks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)ticks;
        renderer.setTranslation(-d0, -d1, -d2);

        GL11.glLineWidth(10/distanceTo(entity, bp, ticks));
        AxisAlignedBB aabb = new AxisAlignedBB(bp, bp.add(1, 1, 1));

        drawOutline(aabb.expand(0.005,0.005,0.005), 0, 255, 255, 255);
        drawBox(aabb.expand(0.0025,0.0025,0.0025), 0, 255, 255, 63);
      }
    } catch (Exception e) {
      WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
      try {
        renderer.finishDrawing();
      } catch (IllegalStateException ignored) {}
    } finally {
      WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
      renderer.setTranslation(0, 0, 0);
      GlStateManager.enableTexture2D();
      GlStateManager.popAttrib();
      GlStateManager.popMatrix();
    }
  }
  public static void drawOutline(AxisAlignedBB boundingBox, int red, int green, int blue, int alpha) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    worldrenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    worldrenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
  }
  public static void drawBox(AxisAlignedBB boundingBox, int red, int green, int blue, int alpha) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    //down
    worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //north
    worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //west
    worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //east
    worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //south
    worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    //up
    worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
  }
  public static float distanceTo(Entity e, BlockPos bp, float ticks) {
    return (float) Math.sqrt(Math.pow(e.getPositionEyes(ticks).xCoord - bp.getX(), 2) + Math.pow(e.getPositionEyes(ticks).yCoord - bp.getY(), 2) + Math.pow(e.getPositionEyes(ticks).zCoord - bp.getZ(), 2));
  }
}
