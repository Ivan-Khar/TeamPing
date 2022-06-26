package com.aqupd.teamping.client;

import static com.aqupd.teamping.TeamPing.pings;

import com.google.gson.JsonObject;
import java.util.Iterator;

public class PingManager {
  public static void clear() {
    Iterator<JsonObject> pingsIter = pings.iterator();
    while (pingsIter.hasNext()) {
      pingsIter.next();
      pingsIter.remove();
    }
  }
}
