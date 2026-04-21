package cl.sanosysalvos.reporte.repository;

import cl.sanosysalvos.reporte.model.ReporteModel;
import cl.sanosysalvos.reporte.model.TipoReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteModel, Long> {


    List<ReporteModel> findByTipoReporte(TipoReporte tipoReporte);

    List<ReporteModel> findByRaza(String raza);

    List<ReporteModel> findByIdUsuario(Integer idUsuario);
    

}