package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.client.PingBlock.*;
import static java.lang.Math.*;

import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderGUI {
  public static void render() {
    Minecraft mc = Minecraft.getMinecraft();
    try {
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      double width = sr.getScaledWidth_double();
      double height = sr.getScaledHeight_double();
      double linestart = 10;
      double linewidth = linestart + Math.min(timer, 4)*4;
      Tessellator tes = Tessellator.getInstance();
      WorldRenderer wr = tes.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      wr.setTranslation(width/2, height/2, 0);
      wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
      GL11.glLineWidth(2);
      wr.pos(0, linestart, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom
      wr.pos(0, linewidth, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom
      wr.pos(linestart, 0, 0.0D).color(64, 64, 64, 127).endVertex();  //Right
      wr.pos(linewidth, 0, 0.0D).color(64, 64, 64, 127).endVertex();  //Right
      wr.pos(0, -linestart, 0.0D).color(64, 64, 64, 127).endVertex(); //Top
      wr.pos(0, -linewidth, 0.0D).color(64, 64, 64, 127).endVertex(); //Top
      wr.pos(-linestart, 0, 0.0D).color(64, 64, 64, 127).endVertex(); //Left
      wr.pos(-linewidth, 0, 0.0D).color(64, 64, 64, 127).endVertex(); //Left
      tes.draw();

      double startx = cos(PI/4) * linestart;
      double starty = sin(PI/4) * linestart;
      double endx = cos(PI/4) * linewidth-0.35;
      double endy = sin(PI/4) * linewidth-0.35;

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

      wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
      GL11.glLineWidth(1);
      wr.pos(endx, -endy, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(endx, -endy+0.75, 0.0D).color(64, 64, 64, 127).endVertex(); //Top-Right
      wr.pos(endx, endy, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(endx, endy-0.75, 0.0D).color(64, 64, 64, 127).endVertex();  //Bottom-Right
      wr.pos(-endx, endy, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-endx, endy-0.75, 0.0D).color(64, 64, 64, 127).endVertex(); //Bottom-Left
      wr.pos(-endx, -endy, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      wr.pos(-endx, -endy+0.75, 0.0D).color(64, 64, 64, 127).endVertex();//Top-Left
      tes.draw();

      wr.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
      GL11.glPointSize(1);
      wr.pos(startx-0.5, -starty, 0.0D).color(64, 64, 64, 127).endVertex();   //Top-Right
      wr.pos(startx-0.5, starty, 0.0D).color(64, 64, 64, 127).endVertex();    //Bottom-Right
      wr.pos(-startx+0.5, starty, 0.0D).color(64, 64, 64, 127).endVertex();   //Bottom-Left
      wr.pos(-startx+0.5, -starty, 0.0D).color(64, 64, 64, 127).endVertex();  //Top-Left
      tes.draw();

      int a1 = 0;
      int a2 = 0;
      int a3 = 0;
      int a4 = 0;
      int a5 = 0;
      int a6 = 0;
      int a7 = 0;
      int a8 = 0;
      double sensitivity = pow(mc.gameSettings.mouseSensitivity / 4 + 0.2, 3) * 8.0F;
      if(guimenu){
        cX = cX + mc.mouseHelper.deltaX * sensitivity;
        cY = cY - mc.mouseHelper.deltaY * sensitivity;
        cX = min(cX, 40);
        cX = max(cX, -40);
        cY = min(cY, 40);
        cY = max(cY, -40);
        double angle = toDegrees((atan2(-cX, cY) + PI));

        double dist = sqrt(pow(cX, 2) + pow(cY, 2));
        int darkangle = 128;
        if(dist > 15){
          if((angle <= 22.5) || (angle > 337.5)){
            a1 = darkangle;
            if(mc.gameSettings.keyBindAttack.isKeyDown()) pingBlock("here");
          } else if(angle <= 67.5){
            a2 = darkangle;
            if(mc.gameSettings.keyBindAttack.isKeyDown()) pingBlock("danger");
          } else if(angle <= 112.5){
            a3 = darkangle;
            if(mc.gameSettings.keyBindAttack.isKeyDown()) pingBlock("question");
          } else if(angle <= 157.5){
            a4 = darkangle;
            if(mc.gameSettings.keyBindAttack.isKeyDown()) pingBlock("no");
          } else if(angle <= 202.5){
            a5 = darkangle;
          } else if(angle <= 247.5){
            a6 = darkangle;
          } else if(angle <= 292.5){
            a7 = darkangle;
          } else if(angle <= 337.5){
            a8 = darkangle;
          }
        }
        wr.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
        GL11.glPointSize(4);
        wr.pos(cX, cY, 0).color(25, 25, 25, 33).endVertex();
        tes.draw();
      }

      double mos = (sqrt(pow(16, 2) + pow(16, 2)))/2;
      double midx = cos(PI/4) * (linewidth + 8);
      double midy = sin(PI/4) * (linewidth + 8);
      if(timer >= 5){
        int alpha = 6 * (timer-5);
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(linewidth, -8, 0.0D).color(32, 32, 32, alpha + a3).endVertex();         //Right 3
        wr.pos(linewidth, 8, 0.0D).color(32, 32, 32, alpha + a3).endVertex();          //Right 3
        wr.pos(linewidth+16, 8, 0.0D).color(32, 32, 32, alpha + a3).endVertex();    //Right 3
        wr.pos(linewidth+16, -8, 0.0D).color(32, 32, 32, alpha + a3).endVertex();   //Right 3
        wr.pos(-8, -linewidth-16, 0.0D).color(32, 32, 32, alpha + a1).endVertex();  //Top 1
        wr.pos(-8, -linewidth, 0.0D).color(32, 32, 32, alpha + a1).endVertex();        //Top 1
        wr.pos(8, -linewidth, 0.0D).color(32, 32, 32, alpha + a1).endVertex();         //Top 1
        wr.pos(8, -linewidth-16, 0.0D).color(32, 32, 32, alpha + a1).endVertex();   //Top 1
        wr.pos(-linewidth-16, -8, 0.0D).color(32, 32, 32, alpha + a7).endVertex();  //Left 7
        wr.pos(-linewidth-16, 8, 0.0D).color(32, 32, 32, alpha + a7).endVertex();   //Left 7
        wr.pos(-linewidth, 8, 0.0D).color(32, 32, 32, alpha + a7).endVertex();         //Left 7
        wr.pos(-linewidth, -8, 0.0D).color(32, 32, 32, alpha + a7).endVertex();        //Left 7
        wr.pos(-8, linewidth, 0.0D).color(32, 32, 32, alpha + a5).endVertex();         //Bottom 5
        wr.pos(-8, linewidth+16, 0.0D).color(32, 32, 32, alpha + a5).endVertex();   //Bottom 5
        wr.pos(8, linewidth+16, 0.0D).color(32, 32, 32, alpha + a5).endVertex();    //Bottom 5
        wr.pos(8, linewidth, 0.0D).color(32, 32, 32, alpha + a5).endVertex();          //Bottom 5
        midx = midx - 0.25;
        midy = midy - 0.25;
        wr.pos(midx - mos, midy, 0).color(32, 32, 32, alpha + a4).endVertex();         //Bottom-Right 4
        wr.pos(midx, midy + mos, 0).color(32, 32, 32, alpha + a4).endVertex();         //Bottom-Right 4
        wr.pos(midx + mos, midy, 0).color(32, 32, 32, alpha + a4).endVertex();         //Bottom-Right 4
        wr.pos(midx, midy - mos, 0).color(32, 32, 32, alpha + a4).endVertex();         //Bottom-Right 4
        wr.pos(-midx - mos, midy, 0).color(32, 32, 32, alpha + a6).endVertex();        //Bottom-Left 6
        wr.pos(-midx, midy + mos, 0).color(32, 32, 32, alpha + a6).endVertex();        //Bottom-Left 6
        wr.pos(-midx + mos, midy, 0).color(32, 32, 32, alpha + a6).endVertex();        //Bottom-Left 6
        wr.pos(-midx, midy - mos, 0).color(32, 32, 32, alpha + a6).endVertex();        //Bottom-Left 6
        wr.pos(-midx - mos, -midy, 0).color(32, 32, 32, alpha + a8).endVertex();       //Top-Left 8
        wr.pos(-midx, -midy + mos, 0).color(32, 32, 32, alpha + a8).endVertex();       //Top-Left 8
        wr.pos(-midx + mos, -midy, 0).color(32, 32, 32, alpha + a8).endVertex();       //Top-Left 8
        wr.pos(-midx, -midy - mos, 0).color(32, 32, 32, alpha + a8).endVertex();       //Top-Left 8
        wr.pos(midx - mos, -midy, 0).color(32, 32, 32, alpha + a2).endVertex();        //Top-Right 2
        wr.pos(midx, -midy + mos, 0).color(32, 32, 32, alpha + a2).endVertex();        //Top-Right 2
        wr.pos(midx + mos, -midy, 0).color(32, 32, 32, alpha + a2).endVertex();        //Top-Right 2
        wr.pos(midx, -midy - mos, 0).color(32, 32, 32, alpha + a2).endVertex();        //Top-Right 2
        tes.draw();

        GlStateManager.enableTexture2D();
        mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/worldpings.png"));

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        wr.pos(-8, -linewidth-16, 0.0D).tex(0, 0).color(255, 255, 255, alpha * 4).endVertex();    //Top 1
        wr.pos(-8, -linewidth, 0.0D).tex(0, 0.5).color(255, 255, 255, alpha * 4).endVertex();        //Top 1
        wr.pos(8, -linewidth, 0.0D).tex(0.5, 0.5).color(255, 255, 255, alpha * 4).endVertex();       //Top 1
        wr.pos(8, -linewidth-16, 0.0D).tex(0.5, 0).color(255, 255, 255, alpha * 4).endVertex();   //Top 1

        wr.pos(midx - 8, -midy - 8, 0).tex(0.5, 0).color(255, 255, 255, alpha * 4).endVertex();   //Top-Right 2
        wr.pos(midx - 8, -midy + 8, 0).tex(0.5, 0.5).color(255, 255, 255, alpha * 4).endVertex(); //Top-Right 2
        wr.pos(midx + 8, -midy + 8, 0).tex(1, 0.5).color(255, 255, 255, alpha * 4).endVertex();   //Top-Right 2
        wr.pos(midx + 8, -midy - 8, 0).tex(1, 0).color(255, 255, 255, alpha * 4).endVertex();     //Top-Right 2

        wr.pos(linewidth, -7, 0.0D).tex(0, 0.5).color(255, 255, 255, alpha * 4).endVertex();         //Right 3
        wr.pos(linewidth, 9, 0.0D).tex(0, 1).color(255, 255, 255, alpha * 4).endVertex();            //Right 3
        wr.pos(linewidth+16, 9, 0.0D).tex(0.5, 1).color(255, 255, 255, alpha * 4).endVertex();    //Right 3
        wr.pos(linewidth+16, -7, 0.0D).tex(0.5, 0.5).color(255, 255, 255, alpha * 4).endVertex(); //Right 3

        wr.pos(midx - 8, midy-2.5 - 8, 0).tex(0.5, 0.5).color(255, 255, 255, alpha * 4).endVertex(); //Bottom-Right 4
        wr.pos(midx - 8, midy-2.5 + 8, 0).tex(0.5, 1).color(255, 255, 255, alpha * 4).endVertex();   //Bottom-Right 4
        wr.pos(midx + 8, midy-2.5 + 8, 0).tex(1, 1).color(255, 255, 255, alpha * 4).endVertex();     //Bottom-Right 4
        wr.pos(midx + 8, midy-2.5 - 8, 0).tex(1, 0.5).color(255, 255, 255, alpha * 4).endVertex();   //Bottom-Right 4
        tes.draw();

        GlStateManager.disableTexture2D();
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