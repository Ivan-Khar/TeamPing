package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;
import static com.aqupd.teamping.listeners.EventListener.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ClientThreads {
  private final Socket socket;
  private final EntityPlayer player;
  private String reason;
  private int step = 0;
  private long lastinteraction = 0;
  private boolean init = true;
  private boolean closed = false;
  private String serverip;
  private boolean debug;

  private final OkHttpClient httpClient = new OkHttpClient();
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  public ClientThreads(Socket socket, EntityPlayer entity, String sip, Boolean debug) {
    this.socket = socket;
    this.player = entity;
    this.serverip = sip;
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
            JsonObject jo = new JsonParser().parse(text).getAsJsonObject();
            jo.add("lifetime", new JsonPrimitive(faketime));
            LOGGER.info("received ping " + jo);
            pings.add(jo);
            playsound=true;
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
              String token = Minecraft.getMinecraft().getSession().getToken();
              String serverid = (debug ? "localhost" : hash(serverip));

              JsonObject jsonObject = new JsonObject();
              jsonObject.add("accessToken", new JsonPrimitive(token));
              jsonObject.add("selectedProfile", new JsonPrimitive(player.getUniqueID().toString().replace("-", "")));
              jsonObject.add("serverId", new JsonPrimitive(serverid));
              if (!debug) {
                String query = jsonObject.toString();
                RequestBody body = RequestBody.create(query, JSON);
                Request request = new Request.Builder()
                  .url("https://sessionserver.mojang.com/session/minecraft/join")
                  .post(body)
                  .build();
                httpClient.newCall(request).execute();
              }
              data.add("serverid", new JsonPrimitive(serverid));
              LOGGER.info(step + " " + data);
              writer.println(data);
              step++;
            } else if (step == 6 && (System.currentTimeMillis() - lastinteraction) > 250) {
              LOGGER.info(step);
              writer.println("YES");
              init = false;
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
      } catch (IOException ex) {
        closed = true;
        connecting = false;
        LOGGER.error("Client writer exception", ex);
      }
    }
  }

  public static String hash(String str) {
    try {
      byte[] digest = digest(str);
      return new BigInteger(digest).toString(16);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private static byte[] digest(String str) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
    return md.digest(strBytes);
  }
}
