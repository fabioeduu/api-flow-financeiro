package br.com.fiap.financas_api.service;


import br.com.fiap.financas_api.infra.exception.RegraDeNegocioException;
import br.com.fiap.financas_api.model.Usuario;
import com.auth0.jwt.JWT;
import org.springframework.stereotype.Service;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String SECRET = "12345678";
    private static final String ISSUER = "Global";

    public String gerarToken(Usuario user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(expiracao(30))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RegraDeNegocioException("Erro ao gerar token JWT de acesso!");
        }
    }

    public String verificarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            throw new RegraDeNegocioException("Erro ao verificar token JWT de acesso!");
        }
    }

    private Instant expiracao(Integer minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }
}
