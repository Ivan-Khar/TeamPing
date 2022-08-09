package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.client.SendData.*;
import static com.aqupd.teamping.listeners.EventListener.*;
import static com.aqupd.teamping.setup.Registrations.keyBindings;
import static java.lang.Math.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("IntegerDivisionInFloatingPointContext")
public class PingSelector {
  private static boolean menu;
  public static double cX = 0;
  public static double cY = 0;
  private static int currentid = -1;
  public static void render() {
    Minecraft mc = Minecraft.getMinecraft();
    Tessellator tes = Tessellator.getInstance();
    WorldRenderer wr = tes.getWorldRenderer();
    FontRenderer fr = mc.fontRendererObj;
    try {
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      double width = sr.getScaledWidth_double();
      double height = sr.getScaledHeight_double();
      double linestart = 10;
      double linewidth = linestart + Math.min(timer, 4)*4;

      String s1 = "You need to join a party first!";
      String s2 = "Use \"" + Keyboard.getKeyName(keyBindings[2].getKeyCode()) + "\" in order to open party menu";

      if(partyName.equals("Your party id") || partyName.length() < 3) {
        fr.drawString(s1, (float) (width / 2) - (fr.getStringWidth(s1) / 2), (float) height / 2 - 64, 16711680, true);
        fr.drawString(s2, (float) (width / 2) - (fr.getStringWidth(s2) / 2), (float) height / 2 + 64, 16711680, true);
      }

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

      double sensitivity = pow(mc.gameSettings.mouseSensitivity / 4 + 0.2, 3) * 8.0F;
      int pingid = -1;

      if (guimenu || menu) {
        menu = true;
        cX = cX + mc.mouseHelper.deltaX * sensitivity;
        cY = cY - mc.mouseHelper.deltaY * sensitivity;
        cX = min(cX, 40);
        cX = max(cX, -40);
        cY = min(cY, 40);
        cY = max(cY, -40);
        double angle = toDegrees((atan2(-cX, cY) + PI));
        double dist = sqrt(pow(cX, 2) + pow(cY, 2));
        if (dist > 15) {
          pingid = (int) floor((angle-22.5)/45) + 1;
          if (pingid == 8) pingid = 0;
          if (mc.gameSettings.keyBindAttack.isKeyDown() || !guimenu) pingBlock(pingidnames[pingid]);
          if (!guimenu) menu=false;
        } else {
          if (mc.gameSettings.keyBindAttack.isKeyDown() || !guimenu) pingBlock("");
        }
        wr.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
        GL11.glPointSize(4);
        wr.pos(cX, cY, 0).color(25, 25, 25, 127).endVertex();
        tes.draw();
      }

      if (currentid != pingid) {
        currentid = pingid;
        Minecraft.getMinecraft().thePlayer.playSound("minecraft:random.wood_click", 0.1F, 2);
      }

      double mos = (sqrt(pow(16, 2) + pow(16, 2)))/2;
      double midx = cos(PI/4) * (linewidth + 8);
      double midy = sin(PI/4) * (linewidth + 8);
      if (timer >= 5) {
        int alpha = 6 * (timer-5);
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(linewidth, -8, 0.0D).color(32, 32, 32, (pingid == 2) ? alpha + 128 : alpha).endVertex();         //Right 2
        wr.pos(linewidth, 8, 0.0D).color(32, 32, 32, (pingid == 2) ? alpha + 128 : alpha).endVertex();          //Right 2
        wr.pos(linewidth+16, 8, 0.0D).color(32, 32, 32, (pingid == 2) ? alpha + 128 : alpha).endVertex();    //Right 2
        wr.pos(linewidth+16, -8, 0.0D).color(32, 32, 32, (pingid == 2) ? alpha + 128 : alpha).endVertex();   //Right 2
        wr.pos(-8, -linewidth-16, 0.0D).color(32, 32, 32, (pingid == 0) ? alpha + 128 : alpha).endVertex();  //Top 0
        wr.pos(-8, -linewidth, 0.0D).color(32, 32, 32, (pingid == 0) ? alpha + 128 : alpha).endVertex();        //Top 0
        wr.pos(8, -linewidth, 0.0D).color(32, 32, 32, (pingid == 0) ? alpha + 128 : alpha).endVertex();         //Top 0
        wr.pos(8, -linewidth-16, 0.0D).color(32, 32, 32, (pingid == 0) ? alpha + 128 : alpha).endVertex();   //Top 0
        wr.pos(-linewidth-16, -8, 0.0D).color(32, 32, 32, (pingid == 6) ? alpha + 128 : alpha).endVertex();  //Left 6
        wr.pos(-linewidth-16, 8, 0.0D).color(32, 32, 32, (pingid == 6) ? alpha + 128 : alpha).endVertex();   //Left 6
        wr.pos(-linewidth, 8, 0.0D).color(32, 32, 32, (pingid == 6) ? alpha + 128 : alpha).endVertex();         //Left 6
        wr.pos(-linewidth, -8, 0.0D).color(32, 32, 32, (pingid == 6) ? alpha + 128 : alpha).endVertex();        //Left 6
        wr.pos(-8, linewidth, 0.0D).color(32, 32, 32, (pingid == 4) ? alpha + 128 : alpha).endVertex();         //Bottom 4
        wr.pos(-8, linewidth+16, 0.0D).color(32, 32, 32, (pingid == 4) ? alpha + 128 : alpha).endVertex();   //Bottom 4
        wr.pos(8, linewidth+16, 0.0D).color(32, 32, 32, (pingid == 4) ? alpha + 128 : alpha).endVertex();    //Bottom 4
        wr.pos(8, linewidth, 0.0D).color(32, 32, 32, (pingid == 4) ? alpha + 128 : alpha).endVertex();          //Bottom 4
        midx = midx - 0.25;
        midy = midy - 0.25;
        wr.pos(midx - mos, midy, 0).color(32, 32, 32, (pingid == 3) ? alpha + 128 : alpha).endVertex();         //Bottom-Right 4
        wr.pos(midx, midy + mos, 0).color(32, 32, 32, (pingid == 3) ? alpha + 128 : alpha).endVertex();         //Bottom-Right 4
        wr.pos(midx + mos, midy, 0).color(32, 32, 32, (pingid == 3) ? alpha + 128 : alpha).endVertex();         //Bottom-Right 4
        wr.pos(midx, midy - mos, 0).color(32, 32, 32, (pingid == 3) ? alpha + 128 : alpha).endVertex();         //Bottom-Right 4
        wr.pos(-midx - mos, midy, 0).color(32, 32, 32, (pingid == 5) ? alpha + 128 : alpha).endVertex();        //Bottom-Left 6
        wr.pos(-midx, midy + mos, 0).color(32, 32, 32, (pingid == 5) ? alpha + 128 : alpha).endVertex();        //Bottom-Left 6
        wr.pos(-midx + mos, midy, 0).color(32, 32, 32, (pingid == 5) ? alpha + 128 : alpha).endVertex();        //Bottom-Left 6
        wr.pos(-midx, midy - mos, 0).color(32, 32, 32, (pingid == 5) ? alpha + 128 : alpha).endVertex();        //Bottom-Left 6
        wr.pos(-midx - mos, -midy, 0).color(32, 32, 32, (pingid == 7) ? alpha + 128 : alpha).endVertex();       //Top-Left 8
        wr.pos(-midx, -midy + mos, 0).color(32, 32, 32, (pingid == 7) ? alpha + 128 : alpha).endVertex();       //Top-Left 8
        wr.pos(-midx + mos, -midy, 0).color(32, 32, 32, (pingid == 7) ? alpha + 128 : alpha).endVertex();       //Top-Left 8
        wr.pos(-midx, -midy - mos, 0).color(32, 32, 32, (pingid == 7) ? alpha + 128 : alpha).endVertex();       //Top-Left 8
        wr.pos(midx - mos, -midy, 0).color(32, 32, 32, (pingid == 1) ? alpha + 128 : alpha).endVertex();        //Top-Right 2
        wr.pos(midx, -midy + mos, 0).color(32, 32, 32, (pingid == 1) ? alpha + 128 : alpha).endVertex();        //Top-Right 2
        wr.pos(midx + mos, -midy, 0).color(32, 32, 32, (pingid == 1) ? alpha + 128 : alpha).endVertex();        //Top-Right 2
        wr.pos(midx, -midy - mos, 0).color(32, 32, 32, (pingid == 1) ? alpha + 128 : alpha).endVertex();        //Top-Right 2
        tes.draw();

        double minU;
        double maxU;
        mc.renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/pings.png"));
        GlStateManager.enableTexture2D();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        minU = 0.125 * 0;
        maxU = minU + 0.125;
        wr.pos(-8, -linewidth-16, 0.0D).tex(minU, 0).color(8, 202, 209, min(255, alpha * 5)).endVertex();//Top 0
        wr.pos(-8, -linewidth, 0.0D).tex(minU, 1).color(8, 202, 209, min(255, alpha * 5)).endVertex();      //Top 0
        wr.pos(8, -linewidth, 0.0D).tex(maxU, 1).color(8, 202, 209, min(255, alpha * 5)).endVertex();       //Top 0
        wr.pos(8, -linewidth-16, 0.0D).tex(maxU, 0).color(8, 202, 209, min(255, alpha * 5)).endVertex(); //Top 0
        tes.draw();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        minU = 0.125 * 1;
        maxU = minU + 0.125;
        wr.pos(midx - 8, -midy - 8, 0).tex(minU, 0).color(199, 128, 232, min(255, alpha * 5)).endVertex(); //Top-Right 1
        wr.pos(midx - 8, -midy + 8, 0).tex(minU, 1).color(199, 128, 232, min(255, alpha * 5)).endVertex(); //Top-Right 1
        wr.pos(midx + 8, -midy + 8, 0).tex(maxU, 1).color(199, 128, 232, min(255, alpha * 5)).endVertex(); //Top-Right 1
        wr.pos(midx + 8, -midy - 8, 0).tex(maxU, 0).color(199, 128, 232, min(255, alpha * 5)).endVertex(); //Top-Right 1
        tes.draw();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        minU = 0.125 * 2;
        maxU = minU + 0.125;
        wr.pos(linewidth, -8, 0.0D).tex(minU, 0).color(255, 180, 128, min(255, alpha * 5)).endVertex();       //Right 2
        wr.pos(linewidth, 8, 0.0D).tex(minU, 1).color(255, 180, 128, min(255, alpha * 5)).endVertex();        //Right 2
        wr.pos(linewidth+16, 8, 0.0D).tex(maxU, 1).color(255, 180, 128, min(255, alpha * 5)).endVertex();  //Right 2
        wr.pos(linewidth+16, -8, 0.0D).tex(maxU, 0).color(255, 180, 128, min(255, alpha * 5)).endVertex(); //Right 2
        tes.draw();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        minU = 0.125 * 3;
        maxU = minU + 0.125;
        wr.pos(midx - 8, midy - 8, 0).tex(minU, 0).color(89, 173, 246, min(255, alpha * 5)).endVertex(); //Bottom-Right 3
        wr.pos(midx - 8, midy + 8, 0).tex(minU, 1).color(89, 173, 246, min(255, alpha * 5)).endVertex(); //Bottom-Right 3
        wr.pos(midx + 8, midy + 8, 0).tex(maxU, 1).color(89, 173, 246, min(255, alpha * 5)).endVertex(); //Bottom-Right 3
        wr.pos(midx + 8, midy - 8, 0).tex(maxU, 0).color(89, 173, 246, min(255, alpha * 5)).endVertex(); //Bottom-Right 3
        tes.draw();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        minU = 0.125 * 4;
        maxU = minU + 0.125;
        wr.pos(-8, linewidth, 0.0D).tex(minU, 0).color(255, 105, 97, min(255, alpha * 5)).endVertex();      //Bottom 4
        wr.pos(-8, linewidth+16, 0.0D).tex(minU, 1).color(255, 105, 97, min(255, alpha * 5)).endVertex();//Bottom 4
        wr.pos(8, linewidth+16, 0.0D).tex(maxU, 1).color(255, 105, 97, min(255, alpha * 5)).endVertex(); //Bottom 4
        wr.pos(8, linewidth, 0.0D).tex(maxU, 0).color(255, 105, 97, min(255, alpha * 5)).endVertex();       //Bottom 4
        tes.draw();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        minU = 0.125 * 5;
        maxU = minU + 0.125;
        wr.pos(-midx - 8, midy - 8, 0).tex(minU, 0).color(66, 214, 164, min(255, alpha * 5)).endVertex(); //Bottom-Left 5
        wr.pos(-midx - 8, midy + 8, 0).tex(minU, 1).color(66, 214, 164, min(255, alpha * 5)).endVertex(); //Bottom-Left 5
        wr.pos(-midx + 8, midy + 8, 0).tex(maxU, 1).color(66, 214, 164, min(255, alpha * 5)).endVertex(); //Bottom-Left 5
        wr.pos(-midx + 8, midy - 8, 0).tex(maxU, 0).color(66, 214, 164, min(255, alpha * 5)).endVertex(); //Bottom-Left 5
        tes.draw();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        minU = 0.125 * 6;
        maxU = minU + 0.125;
        wr.pos(-linewidth-16, -8, 0.0D).tex(minU, 0).color(157, 148, 255, min(255, alpha * 5)).endVertex();//Right 6
        wr.pos(-linewidth-16, 8, 0.0D).tex(minU, 1).color(157, 148, 255, min(255, alpha * 5)).endVertex(); //Right 6
        wr.pos(-linewidth, 8, 0.0D).tex(maxU, 1).color(157, 148, 255, min(255, alpha * 5)).endVertex();       //Right 6
        wr.pos(-linewidth, -8, 0.0D).tex(maxU, 0).color(157, 148, 255, min(255, alpha * 5)).endVertex();      //Right 6
        tes.draw();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        minU = 0.125 * 7;
        maxU = minU + 0.125;
        wr.pos(-midx - 8, -midy - 8, 0).tex(minU, 0).color(248, 243, 141, min(255, alpha * 5)).endVertex(); //Top-Left 7
        wr.pos(-midx - 8, -midy + 8, 0).tex(minU, 1).color(248, 243, 141, min(255, alpha * 5)).endVertex(); //Top-Left 7
        wr.pos(-midx + 8, -midy + 8, 0).tex(maxU, 1).color(248, 243, 141, min(255, alpha * 5)).endVertex(); //Top-Left 7
        wr.pos(-midx + 8, -midy - 8, 0).tex(maxU, 0).color(248, 243, 141, min(255, alpha * 5)).endVertex(); //Top-Left 7
        tes.draw();

        GlStateManager.disableTexture2D();
      }
    } catch(Exception e) {
      try {
        wr.finishDrawing();
      } catch (IllegalStateException ignored) {}
    } finally {
      wr.setTranslation(0, 0, 0);
      GL11.glLineWidth(2);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
    }
  }
}