package com.dayloom.gateway.filter;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Protege rutas admin por dos vias (pasa con cualquiera):
 * 1. IP allowlist (env ADMIN_TAILNET_IPS): para despliegues donde la IP real
 *    del cliente llega intacta al container (bare metal, proxy que preserva IP).
 * 2. Identidad Tailnet (env ADMIN_TAILNET_LOGINS): cuando la peticion viene de
 *    loopback, se confia en el header Tailscale-User-Login que solo puede poner
 *    tailscaled (caso `tailscale serve`). Solo funciona si el gateway escucha
 *    unicamente en 127.0.0.1: cualquier otro origen loopback es la maquina local.
 * Corre antes que el filtro JWT.
 */
@Component
public class TailnetFilter implements GlobalFilter, Ordered {
  private final String allowlist;
  private final String logins;

  public TailnetFilter(@Value("${admin.tailnet-ips:100.64.0.0/10}") String allowlist,
      @Value("${admin.tailnet-logins:}") String logins) {
    this.allowlist = allowlist;
    this.logins = logins;
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 1;
  }

  static boolean isAdminPath(String path) {
    return path.startsWith("/api/admin/") || path.startsWith("/api/auth/admin/");
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getPath().value();
    if (!isAdminPath(path)) {
      return chain.filter(exchange);
    }
    String xff = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
    String remote = exchange.getRequest().getRemoteAddress() != null
        ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        : null;
    if (Tailnet.isAllowed(Tailnet.clientIp(xff, remote), allowlist)) {
      return chain.filter(exchange);
    }
    String tsLogin = exchange.getRequest().getHeaders().getFirst("Tailscale-User-Login");
    if (isLoopback(remote) && isAllowedLogin(tsLogin)) {
      return chain.filter(exchange);
    }
    return forbidden(exchange, "admin solo disponible en Tailnet");
  }

  static boolean isLoopback(String remote) {
    return "127.0.0.1".equals(remote) || "::1".equals(remote) || "::ffff:127.0.0.1".equals(remote);
  }

  boolean isAllowedLogin(String login) {
    if (login == null || login.isBlank() || logins == null || logins.isBlank()) return false;
    for (String allowed : logins.split(",")) {
      if (!allowed.isBlank() && allowed.trim().equalsIgnoreCase(login.trim())) return true;
    }
    return false;
  }

  private Mono<Void> forbidden(ServerWebExchange exchange, String msg) {
    var res = exchange.getResponse();
    res.setStatusCode(HttpStatus.FORBIDDEN);
    res.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    byte[] body = ("{\"error\":\"" + msg + "\"}").getBytes(StandardCharsets.UTF_8);
    DataBuffer buf = res.bufferFactory().wrap(body);
    return res.writeWith(Mono.just(buf));
  }
}
