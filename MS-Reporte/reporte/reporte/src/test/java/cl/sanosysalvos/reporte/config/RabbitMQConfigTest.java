package cl.sanosysalvos.reporte.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class RabbitMQConfigTest {

    @Test
    void jacksonJsonMessageConverter_creaConversorJson() {
        RabbitMQConfig config = new RabbitMQConfig();

        MessageConverter converter = config.jacksonJsonMessageConverter();

        assertInstanceOf(JacksonJsonMessageConverter.class, converter);
    }
}