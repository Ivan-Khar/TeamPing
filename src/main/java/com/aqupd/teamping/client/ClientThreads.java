package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.listeners.EventListener.connecting;
import static com.aqupd.teamping.listeners.EventListener.playsound;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.*;
import java.net.Socket;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ClientThreads {
  private final Socket socket;
  private final EntityPlayer player;
  private final boolean debug;
  private String reason;
  private int step = 0;
  private boolean init = true;
  private boolean closed = false;

  public ClientThreads(Socket socket, EntityPlayer entity, Boolean debug) {
    this.socket = socket;
    this.player = entity;
    this.debug = debug;
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

        do {
          text = reader.readLine();
          if (text == null) break;
          if (init) {
            if (step == 1 && text.length() != 0) {
              init = false;
              if (text.equals("SUCCESS")) {
                LOGGER.info(step);
                step++;
              } else if (text.equals("NOTSUCCESS")) {
                LOGGER.info(step);
                conattempts = 3;
                reason = "Server couldn't verify you. Restart your game in order to connect";
                break;
              }
            }
          } else {
            JsonObject jo = new JsonParser().parse(text).getAsJsonObject();
            jo.add("time", new JsonPrimitive(System.currentTimeMillis()));
            LOGGER.info("received ping " + jo);
            pings.add(jo);

            Integer[] playerpos = new Integer[3];
            playerpos[0] = Minecraft.getMinecraft().thePlayer.getPosition().getX();
            playerpos[1] = Minecraft.getMinecraft().thePlayer.getPosition().getY();
            playerpos[2] = Minecraft.getMinecraft().thePlayer.getPosition().getZ();

            Integer[] blockps = new Integer[3];
            blockps[0] = Math.min(2, Math.max(-2, playerpos[0] - jo.get("bp").getAsJsonArray().get(0).getAsInt()));
            blockps[1] = Math.min(2, Math.max(-2, playerpos[1] - jo.get("bp").getAsJsonArray().get(1).getAsInt()));
            blockps[2] = Math.min(2, Math.max(-2, playerpos[2] - jo.get("bp").getAsJsonArray().get(2).getAsInt()));

            playsound[0] = playerpos[0] - blockps[0];
            playsound[1] = playerpos[1] - blockps[1];
            playsound[2] = playerpos[2] - blockps[2];
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
        long pingtime = System.currentTimeMillis();

        do {
          JsonObject ping1 = new JsonObject();
          if (socket.isClosed()) break;
          if (ping != null) ping1 = ping;
          if (init) {
            if (step == 0) {
              data.add("name", new JsonPrimitive(player.getName()));
              String serverid = (UUID.randomUUID().toString());

              JsonObject jsonObject = new JsonObject();
              jsonObject.add("accessToken", new JsonPrimitive(Minecraft.getMinecraft().getSession().getToken()));
              jsonObject.add("selectedProfile", new JsonPrimitive(player.getUniqueID().toString().replace("-", "")));
              jsonObject.add("serverId", new JsonPrimitive(serverid));
              if (!debug) {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                String query = jsonObject.toString();
                StringEntity requestEntity = new StringEntity(query, ContentType.APPLICATION_JSON);
                HttpPost postMethod = new HttpPost("https://sessionserver.mojang.com/session/minecraft/join");
                postMethod.setEntity(requestEntity);
                httpclient.execute(postMethod);
              }
              data.add("serverid", new JsonPrimitive(serverid));
              LOGGER.info(step + " " + data);
              writer.println(data);
              step++;
            }
          } else if ((System.currentTimeMillis() - pingtime) > 1000) {
            writer.println("PING");
            pingtime = System.currentTimeMillis();
          } else if (!ping1.equals(new JsonObject())) {
            LOGGER.info("send ping " + ping1);
            writer.println(ping1);
            ping = new JsonObject();
          }
        } while (!closed);
        closed = true;
        connecting = false;
        LOGGER.info("Client writer stopped");
      } catch (IOException ex) {
        closed = true;
        connecting = false;
        LOGGER.error("Client writer exception", ex);
      }
    }
  }
}
