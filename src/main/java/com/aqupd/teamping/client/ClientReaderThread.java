package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.*;

import java.io.*;
import java.net.Socket;

public class ClientReaderThread extends Thread{
  private final Socket socket;

  public ClientReaderThread(Socket socket) {
    this.socket = socket;
    connected = true;
  }

  public void run() {
    try {
      InputStream input = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));

      String text = "";

      do {
        if (step != 0) text = reader.readLine();
        if (init) {
          if (step == 1 && text.equals("YES")) {
            step++;
          } else if (step == 3 && text.equals("YES")) {
            step++;
          } else if (step == 5 && text.equals("SUCCESS")) {
            step++;
          }
        } else {
          int i;
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
