package pe.edu.cibertec.hospital.citas.config;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.sql.SQLException;

@Configuration
public class H2ConsoleConfig {

    private static final Logger log = LoggerFactory.getLogger(H2ConsoleConfig.class);

    private Server webServer;
    private Server tcpServer;

    @EventListener(ContextRefreshedEvent.class)
    public void startH2Servers() {
        try {
            this.webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8085").start();
            this.tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
            log.info("[H2 CONSOLE] Servidor Web H2 activo en: http://localhost:8085 (JDBC URL: jdbc:h2:mem:citasdb)");
            log.info("[H2 CONSOLE] Servidor TCP H2 activo en: localhost:9092 para DBeaver / IntelliJ");
        } catch (SQLException e) {
            log.warn("[H2 CONSOLE] No se pudo inicializar servidor H2: {}", e.getMessage());
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void stopH2Servers() {
        if (this.webServer != null) {
            this.webServer.stop();
        }
        if (this.tcpServer != null) {
            this.tcpServer.stop();
        }
    }

}
