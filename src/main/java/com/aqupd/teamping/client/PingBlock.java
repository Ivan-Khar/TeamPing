package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.listeners.EventListener.*;
import static java.lang.Math.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class PingBlock {
  public static void pingBlock(String type) {
    if ((System.currentTimeMillis() - lastpingtime) > 1000) {
      Minecraft mc = Minecraft.getMinecraft();
      JsonObject data = new JsonObject();
      int distance = min(Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16, 128);
      BlockPos bp = mc.thePlayer.rayTrace(distance, ticks).getBlockPos();

      JsonArray blockpos = new JsonArray();
      blockpos.add(new JsonPrimitive(bp.getX()));
      blockpos.add(new JsonPrimitive(bp.getY()));
      blockpos.add(new JsonPrimitive(bp.getZ()));

      data.add("bp", blockpos);
      data.add("type", new JsonPrimitive(type));
      data.add("uuid", new JsonPrimitive(UUID.randomUUID().toString()));
      ping = data;
      lastpingtime = System.currentTimeMillis();
    }
  }
}
