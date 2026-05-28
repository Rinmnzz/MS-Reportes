package cl.sanosysalvos.reporte;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReporteApplicationTests {

	@Test
	void applicationClassIsAnnotated() {
		assertTrue(ReporteApplication.class.isAnnotationPresent(SpringBootApplication.class));
	}

}
