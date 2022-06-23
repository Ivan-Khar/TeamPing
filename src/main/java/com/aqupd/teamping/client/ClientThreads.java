package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.*;
import java.net.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientThreads {
  private final Socket socket;
  private final EntityPlayer player;
  private String reason;
  private int step = 0;
  private long lastinteraction = 0;
  private boolean init = true;
  private boolean closed = false;

  public ClientThreads(Socket socket, EntityPlayer entity) {
    this.socket = socket;
    this.player = entity;
    new Reader().start();
    new Writer().start();
    conattempts = 0;
  }

  private class Reader extends Thread {
    public void run() {
      try {
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String text;
        lastinteraction = System.currentTimeMillis();

        do {
          text = reader.readLine();
          if (text == null) break;

          if (init) {
            if (step == 1 && text.equals("YES")) {
              LOGGER.info(step);
              step++;
              lastinteraction = System.currentTimeMillis();
            } else if (step == 3 && text.equals("YES")) {
              LOGGER.info(step);
              step++;
              lastinteraction = System.currentTimeMillis();
            } else if (step == 5 && text.length() != 0) {
              if(text.equals("SUCCESS")) {
                LOGGER.info(step);
                step++;
              } else if (text.equals("NOTSUCCESS")) {
                LOGGER.info(step);
                conattempts = 3;
                reason = "Server couldn't verify you";
                break;
              }
              lastinteraction = System.currentTimeMillis();
            }
          } else {
            int faketime = 31 * 2 + 500;
            JsonObject jo = JsonParser.parseString(text).getAsJsonObject();
            jo.add("lifetime", new JsonPrimitive(faketime));
            LOGGER.info("received ping " + jo);
            pings.add(jo);
            Minecraft.getMinecraft().getSoundHandler().playSound(new PingSound(player));
          }
        } while (!closed);
        closed = true;
        connecting = false;
        time = System.currentTimeMillis();
        socket.close();
        LOGGER.info("DISCONNECTED" + (reason.length() == 0 ? "" : " with reason: " + reason));
      } catch (IOException ex) {
        LOGGER.error("Client reader exception", ex);
        closed = true;
        connecting = false;
        time = System.currentTimeMillis();
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
          if (socket.isClosed()) break;
          if (ping != null) ping1 = ping;
          if (init) {
            if (step == 0 && (System.currentTimeMillis() - lastinteraction) > 250) {
              LOGGER.info(step);
              writer.println("CONNECT");
              step++;
            } else if (step == 2 && (System.currentTimeMillis() - lastinteraction) > 250) {
              LOGGER.info(step);
              writer.println("DATA");
              step++;
            } else if (step == 4 && (System.currentTimeMillis() - lastinteraction) > 250) {
              data.add("name", new JsonPrimitive(player.getName()));
              data.add("uuid", new JsonPrimitive(player.getUniqueID().toString()));
              LOGGER.info(step + " " + data);
              writer.println(data);
              step++;
            } else if (step == 6 && (System.currentTimeMillis() - lastinteraction) > 250) {
              LOGGER.info(step);
              writer.println("YES");
              init = false;
            }
          } else if(ping1.size() != 0){
            LOGGER.info("send ping " + ping1);
            writer.println(ping1);
            ping = new JsonObject();
          }
        } while (!closed);
        closed = true;
        connecting = false;
      } catch (IOException ex) {
        LOGGER.error("Client writer exception", ex);
      }
    }
  }
}
