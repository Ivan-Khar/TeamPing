package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static java.lang.Math.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class SendData {
  public static long lastpingtime = 0;

  public static void pingBlock(String type) {
    if ((System.currentTimeMillis() - lastpingtime) > 1000) {
      JsonObject data = new JsonObject();
      int distance = min(Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16, 128);
      Entity e = getMouseOverExtended(distance).entityHit;

      data.add("datatype", new JsonPrimitive("ping"));

      BlockPos bp;
      if (e != null) {
        bp = e.getPosition();
        data.add("isEntity", new JsonPrimitive(true));
      } else {
        bp = getMouseOverExtended(distance).getBlockPos();
        data.add("isEntity", new JsonPrimitive(false));
      }

      JsonArray blockpos = new JsonArray();
      blockpos.add(new JsonPrimitive(bp.getX()));
      blockpos.add(new JsonPrimitive(bp.getY()));
      blockpos.add(new JsonPrimitive(bp.getZ()));

      data.add("bp", blockpos);
      data.add("type", new JsonPrimitive(type));
      data.add("uuid", new JsonPrimitive(UUID.randomUUID().toString()));
      datatosend = data;
      lastpingtime = System.currentTimeMillis();
    }
  }

  public static void joinParty(String partyname) {
    JsonObject data = new JsonObject();
    data.add("datatype", new JsonPrimitive("party"));
    data.add("subtype", new JsonPrimitive("connect"));
    data.add("partyname", new JsonPrimitive(partyname));

    datatosend = data;
  }

  private static MovingObjectPosition getMouseOverExtended(float dist) {
    Minecraft mc = Minecraft.getMinecraft();
    Entity theRenderViewEntity = mc.getRenderViewEntity();
    AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(theRenderViewEntity.posX-0.5D, theRenderViewEntity.posY-0.0D, theRenderViewEntity.posZ-0.5D, theRenderViewEntity.posX+0.5D, theRenderViewEntity.posY+1.5D, theRenderViewEntity.posZ+0.5D);
    MovingObjectPosition returnMOP = null;
    if (mc.theWorld != null) {
      double var2 = dist;
      returnMOP = theRenderViewEntity.rayTrace(var2, 0);
      double calcdist = var2;
      Vec3 pos = theRenderViewEntity.getPositionEyes(0);
      var2 = calcdist;
      if (returnMOP != null)
      {
        calcdist = returnMOP.hitVec.distanceTo(pos);
      }

      Vec3 lookvec = theRenderViewEntity.getLook(0);
      Vec3 var8 = pos.addVector(lookvec.xCoord * var2, lookvec.yCoord * var2, lookvec.zCoord * var2);
      Entity pointedEntity = null;
      float var9 = 1.0F;
      List<Entity> list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(theRenderViewEntity, theViewBoundingBox.addCoord(lookvec.xCoord * var2, lookvec.yCoord * var2, lookvec.zCoord * var2).expand(var9, var9, var9));
      double d = calcdist;
      for (Entity entity : list) {
        if (entity.canBeCollidedWith()) {
          float bordersize = entity.getCollisionBorderSize();
          AxisAlignedBB aabb = new AxisAlignedBB(entity.posX-entity.width/2, entity.posY, entity.posZ-entity.width/2, entity.posX+entity.width/2, entity.posY+entity.height, entity.posZ+entity.width/2);
          aabb.expand(bordersize, bordersize, bordersize);
          MovingObjectPosition mop0 = aabb.calculateIntercept(pos, var8);
          if (aabb.isVecInside(pos)) {
            if (0.0D < d || d == 0.0D) {
              pointedEntity = entity;
              d = 0.0D;
            }
          } else if (mop0 != null) {
            double d1 = pos.distanceTo(mop0.hitVec);
            if (d1 < d || d == 0.0D) {
              pointedEntity = entity;
              d = d1;
            }
          }
        }
      }
      if (pointedEntity != null && (d < calcdist || returnMOP == null)) {
        returnMOP = new MovingObjectPosition(pointedEntity);
      }
    }
    return returnMOP;
  }
}
