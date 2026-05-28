package cl.sanosysalvos.reporte.messaging;

import cl.sanosysalvos.reporte.dto.ReporteResponseDTO;
import cl.sanosysalvos.reporte.model.TipoReporte;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReportePublisherTest {

    @Test
    void publicarNuevoReporte_enviaMensajeALaColaCorrecta() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        ReportePublisher publisher = new ReportePublisher(rabbitTemplate);

        ReporteResponseDTO reporte = new ReporteResponseDTO();
        reporte.setId(10L);
        reporte.setTipoReporte(TipoReporte.PERDIDO);

        publisher.publicarNuevoReporte(reporte);

        verify(rabbitTemplate).convertAndSend(ReportePublisher.QUEUE_NAME, reporte);
    }
}