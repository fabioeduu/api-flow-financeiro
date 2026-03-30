package br.com.fiap.financas_api.controller;


import br.com.fiap.financas_api.dto.TokenResponseDTO;
import br.com.fiap.financas_api.dto.autenticacao.DadosLogin;
import br.com.fiap.financas_api.dto.autenticacao.RegisterDTO;
import br.com.fiap.financas_api.model.Usuario;
import br.com.fiap.financas_api.repository.UsuarioRepository;
import br.com.fiap.financas_api.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> efetuarLogin(@Valid @RequestBody DadosLogin dados){

        var authToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(authToken);

        Usuario user = (Usuario) authentication.getPrincipal();

        String tokenAcesso = tokenService.gerarToken(user);

        TokenResponseDTO response = new TokenResponseDTO();
        response.setTokenAcesso(tokenAcesso);
        response.setEmail(user.getEmail());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {

        if (userRepository.findByEmailIgnoreCase(registerDTO.email()).isPresent()){
            return ResponseEntity.badRequest().body("Já existe usuário com esse e-mail");
        }

        String senhaCriptografada = passwordEncoder.encode(registerDTO.senha());

        Usuario user = new Usuario();
        user.setEmail(registerDTO.email());
        user.setSenha(senhaCriptografada);

        userRepository.save(user);

        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    public UserDetails saveCliente(String email, String senhaCriptografada) {
        Usuario user = new Usuario();
        user.setEmail(email);
        user.setSenha(senhaCriptografada);
        return userRepository.save(user);
    }
}
