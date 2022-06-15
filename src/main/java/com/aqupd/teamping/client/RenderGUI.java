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

      double linestart = 10;
      double linewidth = linestart + Math.min(timer, 10)*1.5;
      Tessellator tes = Tessellator.getInstance();
      WorldRenderer wr = tes.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      wr.setTranslation((float) sr.getScaledWidth()/2, (float) sr.getScaledHeight()/2+0.5, 0);
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

      wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
      GL11.glLineWidth(3);
      wr.pos(sin(45) * linestart, -sin(45) * linestart, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(sin(45) * linewidth, -sin(45) * linewidth, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(sin(45) * linestart, sin(45) * linestart, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(sin(45) * linewidth, sin(45) * linewidth, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(-sin(45) * linestart, sin(45) * linestart, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-sin(45) * linewidth, sin(45) * linewidth, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-sin(45) * linestart, -sin(45) * linestart, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      wr.pos(-sin(45) * linewidth, -sin(45) * linewidth, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      tes.draw();

      GL11.glPointSize(2);
      wr.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
      linewidth = linewidth - 0.5;
      wr.pos(sin(45) * linestart, -sin(45) * linestart, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(sin(45) * linewidth, -sin(45) * linewidth, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(sin(45) * linestart, sin(45) * linestart, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(sin(45) * linewidth, sin(45) * linewidth, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(-sin(45) * linestart, sin(45) * linestart, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-sin(45) * linewidth, sin(45) * linewidth, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-sin(45) * linestart, -sin(45) * linestart, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      wr.pos(-sin(45) * linewidth, -sin(45) * linewidth, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      wr.pos(0, linestart, 0.0D).color(64, 64, 64, 127).endVertex();  //Top
      wr.pos(0, linewidth, 0.0D).color(64, 64, 64, 127).endVertex();  //Top
      wr.pos(linestart, 0, 0.0D).color(64, 64, 64, 127).endVertex();  //Right
      wr.pos(linewidth, 0, 0.0D).color(64, 64, 64, 127).endVertex();  //Right
      wr.pos(0, -linestart, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom
      wr.pos(0, -linewidth, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom
      wr.pos(-linestart, 0, 0.0D).color(64, 64, 64, 127).endVertex(); //Right
      wr.pos(-linewidth, 0, 0.0D).color(64, 64, 64, 127).endVertex(); //Right
      tes.draw();

      if(timer >= 10){
        GL11.glPointSize(1);
        wr.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
        for(int k=0;k<=360;k+=1){
          double x = linewidth + 12;
          double y = 0;
          wr.pos(x + (10*Math.cos(Math.toRadians(k))),y + (10*Math.sin(Math.toRadians(k))), 0).color(64, 64, 64, 127).endVertex();  //right
        }
        for(int k=0;k<=360;k+=1){
          double x = 0;
          double y = -linewidth - 12;
          wr.pos(x + (10*Math.cos(Math.toRadians(k))), y - (10*Math.sin(Math.toRadians(k))), 0).color(64, 64, 64, 127).endVertex(); //down
        }
        for(int k=0;k<=360;k+=1){
          double x = -linewidth - 12;
          double y = 0;
          wr.pos(x - (10*Math.cos(Math.toRadians(k))), y + (10*Math.sin(Math.toRadians(k))), 0).color(64, 64, 64, 127).endVertex(); //left
        }
        for(int k=0;k<=360;k+=1){
          double x = 0;
          double y = linewidth + 12;
          wr.pos(x + (10*Math.cos(Math.toRadians(k))),y + (10*Math.sin(Math.toRadians(k))), 0).color(64, 64, 64, 127).endVertex();   //top
        }

        linewidth = linewidth + 12;
        for(int k=0;k<=360;k+=1){
          double x = sin(45) * linewidth;
          double y = -sin(45) * linewidth;
          wr.pos(x + (10*Math.cos(Math.toRadians(k))),y - (10*Math.sin(Math.toRadians(k))), 0).color(64, 64, 64, 127).endVertex();  //top-right
        }
        for(int k=0;k<=360;k+=1){
          double x = sin(45) * linewidth;
          double y = sin(45) * linewidth;
          wr.pos(x + (10*Math.cos(Math.toRadians(k))), y + (10*Math.sin(Math.toRadians(k))), 0).color(64, 64, 64, 127).endVertex(); //bottom-right
        }
        for(int k=0;k<=360;k+=1){
          double x = -sin(45) * linewidth;
          double y = sin(45) * linewidth;
          wr.pos(x - (10*Math.cos(Math.toRadians(k))), y + (10*Math.sin(Math.toRadians(k))), 0).color(64, 64, 64, 127).endVertex(); //bottom-left
        }
        for(int k=0;k<=360;k+=1){
          double x = -sin(45) * linewidth;
          double y = -sin(45) * linewidth;
          wr.pos(x - (10*Math.cos(Math.toRadians(k))),y - (10*Math.sin(Math.toRadians(k))), 0).color(64, 64, 64, 127).endVertex();   //top
        }
        tes.draw();
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
