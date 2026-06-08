package cl.sanosysalvos.reporte;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

class ReporteApplicationTests {

	@Test
	void applicationClassIsAnnotated() {
		assertTrue(ReporteApplication.class.isAnnotationPresent(SpringBootApplication.class));
	}

	@Test
	void mainMethodExecutes() {
		try (var mocked = mockStatic(SpringApplication.class)) {
			ReporteApplication.main(new String[]{});
			mocked.verify(() -> SpringApplication.run(ReporteApplication.class, new String[]{}));
		}
	}

}
