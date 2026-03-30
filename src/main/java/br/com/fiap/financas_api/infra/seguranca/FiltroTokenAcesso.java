package br.com.fiap.financas_api.infra.seguranca;

import br.com.fiap.financas_api.model.Usuario;
import br.com.fiap.financas_api.repository.UsuarioRepository;
import br.com.fiap.financas_api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UsuarioRepository userRepository;

    public FiltroTokenAcesso(TokenService tokenService, UsuarioRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        if(path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        //recuperar o token da requisição
        String token = recuperarTokenRequisicao(request);

        if(token != null){
            String email = tokenService.verificarToken(token);
            Usuario user = userRepository.findByEmailIgnoreCase(email).orElseThrow();

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

}