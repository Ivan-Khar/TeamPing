package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.listeners.EventListener.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.awt.*;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class PingBlock {
  public static void pingBlock(String type, Color color){
    if((System.currentTimeMillis() - lastpingtime) > 1000) {
      Minecraft mc = Minecraft.getMinecraft();
      JsonObject data = new JsonObject();
      int distance = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16;
      if((Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16 > 128)) distance = 128;
      BlockPos bp = mc.thePlayer.rayTrace(distance, ticks).getBlockPos();

      JsonArray blockpos = new JsonArray();
      blockpos.add(new JsonPrimitive(bp.getX()));
      blockpos.add(new JsonPrimitive(bp.getY()));
      blockpos.add(new JsonPrimitive(bp.getZ()));

      JsonArray clr = new JsonArray();
      clr.add(color.getRed());
      clr.add(color.getGreen());
      clr.add(color.getBlue());

      int faketime = 63 * 2 + 500;

      data.add("bp", blockpos);
      data.add("lifetime", new JsonPrimitive(faketime));
      data.add("type", new JsonPrimitive(type));
      data.add("color", clr);
      data.add("uuid", new JsonPrimitive(UUID.randomUUID().toString()));
      ping = data;
      pings.add(data);
      lastpingtime = System.currentTimeMillis();
    }
  }
}
