package cl.sanosysalvos.reporte.messaging;

import cl.sanosysalvos.reporte.model.ReporteModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReportePublisher {

    private final RabbitTemplate rabbitTemplate;

    public ReportePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarNuevoReporte(ReporteModel reporte) {
        // "reportes.exchange" es el nombre que configuraremos luego
        rabbitTemplate.convertAndSend("reportes.exchange", "reporte.creado", reporte);
    }
}