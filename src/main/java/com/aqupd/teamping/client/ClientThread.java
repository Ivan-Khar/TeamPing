package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.*;
import java.net.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientThread extends Thread{
  private final Socket socket;
  private final EntityPlayer player;
  public ClientThread(Socket socket, EntityPlayer entity) {this.socket = socket; this.player=entity;}

  public void run() {
    try {
      InputStream input = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));
      OutputStream output = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(output, true);

      String text = "";
      int step = 0;
      connected = true;
      JsonObject data = new JsonObject();
      do {
        if (shutclientthread) { writer.println("DISCONNECT"); break; }
        if (step != 0) text = reader.readLine();
        if (step == 0) {
          writer.println("CONNECT");
          step++;
        } else if (step == 1 && text.equals("YES")) {
          writer.println("DATA");
          step++;
        } else if (step == 2 && text.equals("YES")) {
          data.add("name", new JsonPrimitive(player.getName()));
          data.add("uuid", new JsonPrimitive(player.getUniqueID().toString()));
          writer.println(data);
          step++;
        } else if (step == 3 && text.equals("SUCCESS")) {
          writer.println("YES");
          step++;
        }
      } while (!text.equals("DISCONNECT"));
      socket.close();
      connected = false;
    } catch (IOException ex) {
      LOGGER.error("Server exception", ex);
      connected = false;
    }
  }
}
