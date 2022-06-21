package com.aqupd.teamping.client;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PingSound extends MovingSound {
  private final EntityPlayer player;

  public PingSound(EntityPlayer player)
  {
    super(new ResourceLocation("minecraft:fireworks.blast_far"));
    this.player = player;
    this.attenuationType = ISound.AttenuationType.NONE;
    this.repeat = false;
    this.repeatDelay = 0;
  }

  /**
   * Like the old updateEntity(), except more generic.
   */
  public void update()
  {
    this.xPosF = (float)this.player.posX;
    this.yPosF = (float)this.player.posY;
    this.zPosF = (float)this.player.posZ;
    this.volume = 0.5F;
    this.pitch = 1F;
  }
}
