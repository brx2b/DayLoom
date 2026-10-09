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
}
