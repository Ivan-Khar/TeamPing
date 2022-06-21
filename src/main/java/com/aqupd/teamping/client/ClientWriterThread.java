package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.*;
import java.net.Socket;
import net.minecraft.entity.player.EntityPlayer;

@SuppressWarnings({"InfiniteLoopStatement", "FieldCanBeLocal"})
public class ClientWriterThread extends Thread{
  private final Socket socket;
  private final EntityPlayer player;

  public ClientWriterThread(Socket socket, EntityPlayer entity) {
    this.socket = socket;
    this.player = entity;
    connected = true;
  }

  public void run() {
    try {
      OutputStream output = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(output, true);
      JsonObject data = new JsonObject();
      do {
        JsonObject ping1 = new JsonObject();
        if(ping != null) {ping1 = ping;}
        if (init) {
          if (step == 0) {
            writer.println("CONNECT");
            step++;
          } else if (step == 2) {
            writer.println("DATA");
            step++;
          } else if (step == 4) {
            data.add("name", new JsonPrimitive(player.getName()));
            data.add("uuid", new JsonPrimitive(player.getUniqueID().toString()));
            writer.println(data);
            step++;
          } else if (step == 6) {
            writer.println("YES");
            init = false;
          }
        } else if(ping1.size() != 0){
          LOGGER.info(ping1);
          writer.println(ping1);
          ping = new JsonObject();
        }
      } while (true);
    } catch (IOException ex) {
      LOGGER.error("Server exception", ex);
    }
  }
}
