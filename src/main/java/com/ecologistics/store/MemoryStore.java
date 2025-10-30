package com.ecologistics.store;

import com.ecologistics.model.Envio;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStore {
  private static final Map<String, Envio> DB = new ConcurrentHashMap<>();
  public static List<Envio> all() { return new ArrayList<>(DB.values()); }
  public static Envio get(String id) { return DB.get(id); }
  public static void add(Envio e) { DB.put(e.getId(), e); }
}
