package com.aqupd.teamping.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

public class UtilMethods {
  public static float distanceTo(Entity e, BlockPos bp, float ticks) {
    return (float) Math.sqrt(Math.pow(e.getPositionEyes(ticks).xCoord - bp.getX(), 2) + Math.pow(e.getPositionEyes(ticks).yCoord - bp.getY(), 2) + Math.pow(e.getPositionEyes(ticks).zCoord - bp.getZ(), 2));
  }
}
