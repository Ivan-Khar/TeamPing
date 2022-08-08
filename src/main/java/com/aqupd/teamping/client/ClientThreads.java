package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.client.SendData.leaveParty;
import static com.aqupd.teamping.listeners.EventListener.*;
import static com.aqupd.teamping.util.UtilMethods.isValidJsonObject;

import com.google.gson.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
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

  public ClientThreads(Socket socket, EntityPlayer entity, Boolean debug) {
    this.socket = socket;
    this.player = entity;
    this.debug = debug;
    new Reader().start();
    conattempts = 0;
  }

  private class Reader extends Thread {
    public void run() {
      JsonObject data = new JsonObject();
      String text;
      boolean init = true;
      boolean writedata = true;
      int step = 0;
      String reason = "";

      try {
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        OutputStream output = socket.getOutputStream();
        outputStream = output;
        PrintWriter writer = new PrintWriter(output, true);

        do {
          if (!writedata) text = reader.readLine(); else text = "";
          if (text == null) break;
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
              writer.println(data);
              step++;
              writedata = false;
            } else if (step == 1 && text.length() != 0) {
              init = false;
              if (text.equals("SUCCESS")) {
                new KeepAlive().start();
              } else if (text.equals("NOTSUCCESS")) {
                conattempts = 3;
                reason = "Server couldn't verify you. Restart your game in order to connect";
                break;
              }
            }
          } else if (isValidJsonObject(text)) {
            JsonObject jo = new JsonParser().parse(text).getAsJsonObject();
            switch (jo.get("datatype").getAsString()) {
              case "ping":
                jo.add("time", new JsonPrimitive(System.currentTimeMillis()));
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
                break;
              case "party":
                switch (jo.get("subtype").getAsString()) {
                  case "list":
                    isInParty = true;
                    ArrayList<String> newPartyPlayers = new ArrayList<>();
                    JsonArray ja = jo.get("players").getAsJsonArray();
                    for(JsonElement je: ja) newPartyPlayers.add(je.getAsString());
                    partyPlayers = newPartyPlayers;
                    break;
                  case "kickmessage":
                    isInParty = false;
                    partyPlayers.clear();
                    leaveParty();
                    switch (jo.get("message").getAsString()) {
                      case "kicked":
                        LOGGER.info("you got kicked from the party");
                        break;
                      case "banned":
                        LOGGER.info("you got banned from the party");
                        break;
                      case "playerlimit":
                        LOGGER.info("Party is full");
                    }
                }
                break;
              case "list":
                playerCount = jo.get("connected").getAsInt();
                GitVersion = jo.get("version").getAsString();
            }
          }
        } while (true);
        socket.close();
        LOGGER.info("DISCONNECTED" + (reason.length() == 0 ? "" : " with reason: " + reason));
      } catch (IOException ex) {
        LOGGER.error("Client reader exception", ex);
      } finally {
        connecting = false;
        isInParty = false;
        outputStream = null;
      }
    }
  }

  private static class KeepAlive extends Thread {
    public void run() {
      long lastping = System.currentTimeMillis();
      do {
        if (System.currentTimeMillis() - lastping > 1000) {
          PrintWriter printWriter = new PrintWriter(outputStream, true);
          printWriter.println("PING");
          lastping = System.currentTimeMillis();
        }
      } while (connecting);
    }
  }
}
