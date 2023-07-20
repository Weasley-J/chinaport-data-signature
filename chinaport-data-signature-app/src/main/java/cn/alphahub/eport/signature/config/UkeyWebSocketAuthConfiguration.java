package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.core.CertificateHandler;
import cn.alphahub.eport.signature.core.WebSocketClientHandler;
import cn.alphahub.eport.signature.entity.WebSocketWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.List;

/**
 * Ukey Web Socket Configurer
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class UkeyWebSocketAuthConfiguration implements WebSocketConfigurer {
    private final UkeyProperties ukeyProperties;
    private final WebSocketWrapper webSocketWrapper;
    private final CertificateHandler certificateHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        String[] paths = List.of(ukeyProperties.getWsUrl()).toArray(new String[0]);
        registry.addHandler(new WebSocketClientHandler(ukeyProperties, webSocketWrapper, certificateHandler), paths);
    }
}
