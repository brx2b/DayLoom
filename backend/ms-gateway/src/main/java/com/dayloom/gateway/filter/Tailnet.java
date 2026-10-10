package com.dayloom.gateway.filter;

import java.net.InetAddress;

/**
 * Decide si una IP cliente pertenece a la Tailnet (CGNAT 100.64.0.0/10).
 *
 * NOTA Docker: dentro de un contenedor, la IP remota del socket es la del
 * bridge (172.x). La IP real del cliente solo llega via X-Forwarded-For
 * desde el proxy de borde. Confiar en XFF es spoofeable si el gateway es
 * publico y no hay proxy de confianza delante: para produccion real,
 * exponer este gateway con `tailscale serve` y leer la IP del socket.
 */
public final class Tailnet {
  private Tailnet() {}

  public static boolean isTailnetIp(String ip) {
    if (ip == null || ip.isBlank()) return false;
    try {
      byte[] b = InetAddress.getByName(ip.trim()).getAddress();
      if (b.length != 4) return false; // solo IPv4 100.64.0.0/10
      return (b[0] & 0xFF) == 100 && (b[1] & 0xC0) == 0x40;
    } catch (Exception e) {
      return false;
    }
  }

  public static String clientIp(String forwardedFor, String remoteAddress) {
    if (forwardedFor != null && !forwardedFor.isBlank()) {
      return forwardedFor.split(",")[0].trim();
    }
    return remoteAddress;
  }

  /**
   * Allowlist separada por comas: IPs sueltas ("100.119.13.70") o CIDR ("100.64.0.0/10").
   * Solo IPv4.
   */
  public static boolean isAllowed(String ip, String allowlist) {
    if (ip == null || ip.isBlank() || allowlist == null || allowlist.isBlank()) return false;
    byte[] addr;
    try {
      addr = InetAddress.getByName(ip.trim()).getAddress();
    } catch (Exception e) {
      return false;
    }
    if (addr.length != 4) return false;
    for (String entry : allowlist.split(",")) {
      entry = entry.trim();
      if (entry.isEmpty()) continue;
      try {
        if (entry.contains("/")) {
          String[] parts = entry.split("/", 2);
          byte[] net = InetAddress.getByName(parts[0].trim()).getAddress();
          int bits = Integer.parseInt(parts[1].trim());
          if (net.length == 4 && bits >= 0 && bits <= 32 && inSubnet(addr, net, bits)) return true;
        } else {
          byte[] single = InetAddress.getByName(entry).getAddress();
          if (single.length == 4 && java.util.Arrays.equals(addr, single)) return true;
        }
      } catch (Exception ignored) {
      }
    }
    return false;
  }

  static boolean inSubnet(byte[] addr, byte[] net, int bits) {
    int full = bits / 8;
    int rest = bits % 8;
    for (int i = 0; i < full; i++) {
      if (addr[i] != net[i]) return false;
    }
    if (rest > 0) {
      int mask = 0xFF << (8 - rest);
      if (((addr[full] & 0xFF) & mask) != ((net[full] & 0xFF) & mask)) return false;
    }
    return true;
  }
}
