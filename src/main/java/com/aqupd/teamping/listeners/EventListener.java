package com.aqupd.teamping.listeners;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventListener {

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onClientTickEvent(TickEvent.ClientTickEvent event) {

  }
}
