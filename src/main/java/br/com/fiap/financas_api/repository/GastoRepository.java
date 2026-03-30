package br.com.fiap.financas_api.repository;


import br.com.fiap.financas_api.dto.GastoCategoriaDTO;
import br.com.fiap.financas_api.dto.GastoMensalDTO;
import br.com.fiap.financas_api.model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    List<Gasto> findByUsuarioId(Long usuarioId);

    @Query("""
        SELECT new br.com.fiap.financas_api.dto.GastoCategoriaDTO(g.categoria, SUM(g.valor))
        FROM Gasto g
        WHERE g.usuario.id = :usuarioId
        GROUP BY g.categoria
    """)
    List<GastoCategoriaDTO> somarPorCategoria(Long usuarioId);

    @Query("""
    SELECT new br.com.fiap.financas_api.dto.GastoMensalDTO(
        FUNCTION('TO_CHAR', g.data, 'MM/YYYY'), 
        SUM(g.valor)
    )
    FROM Gasto g
    WHERE g.usuario.id = :usuarioId
    GROUP BY FUNCTION('TO_CHAR', g.data, 'MM/YYYY')
    ORDER BY FUNCTION('TO_CHAR', g.data, 'MM/YYYY') DESC
""")
    List<GastoMensalDTO> somarPorMes(Long usuarioId);

}

