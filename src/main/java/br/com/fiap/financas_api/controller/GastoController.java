package br.com.fiap.financas_api.controller;


import br.com.fiap.financas_api.dto.GastoCategoriaDTO;
import br.com.fiap.financas_api.dto.GastoMensalDTO;
import br.com.fiap.financas_api.dto.autenticacao.GastoResponseDTO;
import br.com.fiap.financas_api.model.Gasto;
import br.com.fiap.financas_api.model.Usuario;
import br.com.fiap.financas_api.service.GastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService service;

    @PostMapping
    public ResponseEntity<GastoResponseDTO> criar(@Valid @RequestBody GastoResponseDTO dto, @AuthenticationPrincipal Usuario usuarioLogado) {
        Gasto novoGasto = service.criar(dto, usuarioLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(new GastoResponseDTO(novoGasto));
    }

    @GetMapping
    public ResponseEntity<List<GastoResponseDTO>> listar(@AuthenticationPrincipal Usuario usuarioLogado) {
        List<GastoResponseDTO> gastos = service.listarPorUsuario(usuarioLogado.getId())
                .stream()
                .map(GastoResponseDTO::new)
                .toList();

        return ResponseEntity.ok(gastos);
    }

    @GetMapping("/relatorio")
    public ResponseEntity<List<GastoCategoriaDTO>> relatorio(@AuthenticationPrincipal Usuario usuarioLogado) {
        List<GastoCategoriaDTO> relatorio = service.relatorio(usuarioLogado.getId());
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/relatorio-mensal")
    public ResponseEntity<List<GastoMensalDTO>> relatorioMensal(@AuthenticationPrincipal Usuario usuarioLogado) {
        List<GastoMensalDTO> relatorio = service.relatorioMensal(usuarioLogado.getId());
        return ResponseEntity.ok(relatorio);
    }

    @DeleteMapping("/{gastoId}")
    public ResponseEntity<Void> deletar(@PathVariable Long gastoId, @AuthenticationPrincipal Usuario usuarioLogado) {
        service.deletar(gastoId, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-mes")
    public ResponseEntity<BigDecimal> buscarTotalMes(
            @RequestParam int mes,
            @RequestParam int ano,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        BigDecimal total = service.calcularTotalMes(usuarioLogado.getId(), mes, ano);

        return ResponseEntity.ok(total);
    }
}