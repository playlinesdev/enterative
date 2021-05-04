package br.com.chart.enterative.cron.echo;

import br.com.chart.BootstrapApplication;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.chart.enterative.service.ECHOService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.vo.ActiveResourceVO;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ECHOController extends UserAwareComponent {

    @Autowired
    private ECHOService echoService;

    @Scheduled(fixedRate = 5 * 60 * 1000) // Executa a cada 5 minutos
    public void sendECHO() {
        ACTIVATION_PROCESS.ordered().parallelStream().forEach(process -> {
            this.log("ECHO [%s] :: Start", process.getDescription());

            if (process == ACTIVATION_PROCESS.BHN || !BootstrapApplication.isActive(process)) {
                this.log("ECHO [%s] :: RetrieveActiveServer", process.getDescription());
                Server server = this.echoService.retrieveActiveServer(process);

                if (Objects.nonNull(server)) {
                    this.log("ECHO [%s] :: ValidateServer", process.getDescription());
                    boolean valid = this.echoService.validateServer(process, server);

                    if (valid) {
                        ActiveResourceVO activeResource = this.echoService.retrieveResource(process, server);
                        BootstrapApplication.activeResources.put(process, activeResource);
                    } else {
                        this.log("ECHO [%s] :: Invalid server; no resource active", process.getDescription());
                        BootstrapApplication.activeResources.remove(process);
                    }
                } else {
                    this.log("ECHO [%s] :: No active server found!", process.getDescription());
                    BootstrapApplication.activeResources.remove(process);
                }
            } else {
                this.log("ECHO [%s] :: Still active", process.getDescription());
            }

            this.log("ECHO [%s] :: End", process.getDescription());
        });
    }
}
