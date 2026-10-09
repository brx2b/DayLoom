package com.dayloom.gateway.filter;

import java.nio.charset.StandardCharsets;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/** Bloquea /api/admin/** fuera de la Tailnet (100.64.0.0/10). Corre antes que el filtro JWT. */
@Component
public class TailnetFilter implements GlobalFilter, Ordered {
  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 1;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getPath().value();
    if (!path.startsWith("/api/admin/")) {
      return chain.filter(exchange);
    }
    String xff = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
    String remote = exchange.getRequest().getRemoteAddress() != null
        ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        : null;
    if (!Tailnet.isTailnetIp(Tailnet.clientIp(xff, remote))) {
      return forbidden(exchange, "admin solo disponible en Tailnet");
    }
    return chain.filter(exchange);
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
