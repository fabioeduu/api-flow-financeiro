package br.com.fiap.financas_api.service;


import br.com.fiap.financas_api.dto.GastoCategoriaDTO;
import br.com.fiap.financas_api.dto.GastoMensalDTO;
import br.com.fiap.financas_api.dto.autenticacao.GastoResponseDTO;
import br.com.fiap.financas_api.model.Gasto;
import br.com.fiap.financas_api.model.Usuario;
import br.com.fiap.financas_api.repository.GastoRepository;
import br.com.fiap.financas_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class GastoService {

    @Autowired
    private GastoRepository repository;

    @Autowired
    private RestClient restClient;

    private final String API_KEY = "bb19b3031ec2cda7b8e30eb5";

    public Gasto criar(GastoResponseDTO dto, Usuario usuario) {
        Gasto gasto = new Gasto();
        gasto.setDescricao(dto.descricao());
        gasto.setValor(dto.valor());
        gasto.setCategoria(dto.categoria());
        gasto.setUsuario(usuario);
        gasto.setMoeda("BRL");
        gasto.setData(dto.data() != null ? dto.data() : LocalDate.now());

        BigDecimal valorConvertido = converterParaUSD(dto.valor());
        gasto.setValorConvertido(valorConvertido);

        return repository.save(gasto);
    }

    public List<Gasto> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<GastoCategoriaDTO> relatorio(Long usuarioId) {
        return repository.somarPorCategoria(usuarioId);
    }

    private BigDecimal converterParaUSD(BigDecimal valor) {
        try {
            String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/BRL";

            Map response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(Map.class);

            Map rates = (Map) response.get("conversion_rates");
            String usdRateStr = rates.get("USD").toString();
            BigDecimal usdRate = new BigDecimal(usdRateStr);

            return valor.multiply(usdRate);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public void deletar(Long gastoId, Usuario usuarioLogado) {
        Gasto gasto = repository.findById(gastoId)
                .orElseThrow(() -> new RuntimeException("Gasto não encontrado."));

        if (!gasto.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new RuntimeException("Acesso negado.");
        }
        repository.delete(gasto);
    }

    public List<GastoMensalDTO> relatorioMensal(Long usuarioId) {
        return repository.somarPorMes(usuarioId);
    }

    public BigDecimal calcularTotalMes(Long usuarioId, int mes, int ano) {
        String mesAnoProcurado = String.format("%02d/%d", mes, ano);

        return repository.somarPorMes(usuarioId).stream()
                .filter(dto -> dto.mesAno().equals(mesAnoProcurado))
                .map(GastoMensalDTO::total)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }
}

