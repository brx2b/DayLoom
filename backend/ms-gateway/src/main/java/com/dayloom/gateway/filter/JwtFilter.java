package com.dayloom.gateway.filter;

import com.dayloom.gateway.security.JwtService;
import java.nio.charset.StandardCharsets;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Exige JWT en todo salvo /api/auth/** y /actuator/**.
 * Propaga identidad al MS via headers X-User-*.
 */
@Component
public class JwtFilter implements GlobalFilter, Ordered {
  private final JwtService jwt;

  public JwtFilter(JwtService jwt) {
    this.jwt = jwt;
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 2;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getPath().value();
    if (path.startsWith("/api/auth/") || path.startsWith("/actuator/")) {
      return chain.filter(exchange);
    }
    String header = exchange.getRequest().getHeaders().getFirst("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      return unauthorized(exchange);
    }
    JwtService.Claims claims;
    try {
      claims = jwt.parse(header.substring(7));
    } catch (Exception e) {
      return unauthorized(exchange);
    }
    if (path.startsWith("/api/admin/") && !"ADMIN".equals(claims.role())) {
      return forbidden(exchange, "requiere rol ADMIN");
    }
    ServerHttpRequest mutated = exchange.getRequest().mutate()
        .header("X-User-Id", claims.userId())
        .header("X-User-Email", claims.email())
        .header("X-User-Role", claims.role())
        .build();
    return chain.filter(exchange.mutate().request(mutated).build());
  }

  private Mono<Void> unauthorized(ServerWebExchange exchange) {
    return error(exchange, HttpStatus.UNAUTHORIZED, "token requerido o invalido");
  }

  private Mono<Void> forbidden(ServerWebExchange exchange, String msg) {
    return error(exchange, HttpStatus.FORBIDDEN, msg);
  }

  private Mono<Void> error(ServerWebExchange exchange, HttpStatus status, String msg) {
    var res = exchange.getResponse();
    res.setStatusCode(status);
    res.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    byte[] body = ("{\"error\":\"" + msg + "\"}").getBytes(StandardCharsets.UTF_8);
    DataBuffer buf = res.bufferFactory().wrap(body);
    return res.writeWith(Mono.just(buf));
  }
}
