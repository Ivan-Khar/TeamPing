package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static java.lang.Math.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderGUI {
  public static void render() {

    try {
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      double width = sr.getScaledWidth_double();
      double height = sr.getScaledHeight_double();
      double linestart = 10;
      double linewidth = linestart + Math.min(timer, 5)*3;
      Tessellator tes = Tessellator.getInstance();
      WorldRenderer wr = tes.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      wr.setTranslation(width/2, height/2, 0);
      wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
      GL11.glLineWidth(2);
      wr.pos(0, linestart, 0.0D).color(64, 64, 64, 127).endVertex();  //Top
      wr.pos(0, linewidth, 0.0D).color(64, 64, 64, 127).endVertex();  //Top
      wr.pos(linestart, 0, 0.0D).color(64, 64, 64, 127).endVertex();  //Right
      wr.pos(linewidth, 0, 0.0D).color(64, 64, 64, 127).endVertex();  //Right
      wr.pos(0, -linestart, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom
      wr.pos(0, -linewidth, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom
      wr.pos(-linestart, 0, 0.0D).color(64, 64, 64, 127).endVertex(); //Right
      wr.pos(-linewidth, 0, 0.0D).color(64, 64, 64, 127).endVertex(); //Right
      tes.draw();

      double startx = cos(PI/4) * linestart;
      double starty = sin(PI/4) * linestart;
      double endx = cos(PI/4) * linewidth;
      double endy = sin(PI/4) * linewidth;

      wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
      GL11.glLineWidth(3);
      wr.pos(startx, -starty, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(endx, -endy, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(startx, starty, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(endx, endy, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(-startx, starty, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-endx, endy, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-startx, -starty, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      wr.pos(-endx, -endy, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      tes.draw();

      GL11.glPointSize(2);
      wr.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
      linewidth = linewidth - 0.5;
      wr.pos(startx, -starty, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(endx, -endy, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(startx, starty, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(endx, endy, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(-startx, starty, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-endx, endy, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-startx, -starty, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      wr.pos(-endx, -endy, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      wr.pos(0, linestart, 0.0D).color(64, 64, 64, 127).endVertex();  //Top
      wr.pos(0, linewidth, 0.0D).color(64, 64, 64, 127).endVertex();  //Top
      wr.pos(linestart, 0, 0.0D).color(64, 64, 64, 127).endVertex();  //Right
      wr.pos(linewidth, 0, 0.0D).color(64, 64, 64, 127).endVertex();  //Right
      wr.pos(0, -linestart, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom
      wr.pos(0, -linewidth, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom
      wr.pos(-linestart, 0, 0.0D).color(64, 64, 64, 127).endVertex(); //Right
      wr.pos(-linewidth, 0, 0.0D).color(64, 64, 64, 127).endVertex(); //Right
      tes.draw();

      if(timer >= 5){

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
      GlStateManager.disableBlend();
    }
  }
}
