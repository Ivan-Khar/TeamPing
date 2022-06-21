package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.*;
import java.net.Socket;
import net.minecraft.entity.player.EntityPlayer;

public class ClientThreads {
  private final Socket socket;
  private final EntityPlayer player;
  private int step = 0;
  private boolean init = true;
  private boolean closed = false;

  public ClientThreads(Socket socket, EntityPlayer entity) {
    this.socket = socket;
    this.player = entity;
    new Reader().start();
    new Writer().start();
    connected = true;
    conattempts = 0;
  }

  private class Reader extends Thread {
    public void run() {
      try {
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String text = "";

        do {
          text = reader.readLine();
          if (text == null) break;

          if (init) {
            if (step == 1 && text.equals("YES")) {
              step++;
            } else if (step == 3 && text.equals("YES")) {
              step++;
            } else if (step == 5 && text.equals("SUCCESS")) {
              step++;
            }
          } else {
            int faketime = 31 * 2 + 500;
            JsonObject jo = JsonParser.parseString(text).getAsJsonObject();
            jo.add("lifetime", new JsonPrimitive(faketime));
            pings.add(jo);
          }
        } while (!text.equals("DISCONNECT"));
        closed = true;
        connected = false;

        socket.close();
      } catch (IOException ex) {
        LOGGER.error("Client reader exception", ex);
        connected = false;
      }
    }
  }

  private class Writer extends Thread {
    public void run() {
      try {
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        JsonObject data = new JsonObject();
        do {
          JsonObject ping1 = new JsonObject();
          if (ping != null) ping1 = ping;
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
            writer.println(ping1);
            ping = new JsonObject();
          }
        } while (!closed);
      } catch (IOException ex) {
        LOGGER.error("Client writer exception", ex);
      }
    }
  }
}
