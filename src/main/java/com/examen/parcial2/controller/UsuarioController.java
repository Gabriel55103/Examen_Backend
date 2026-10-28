
package com.examen.parcial2.controller;

import com.examen.parcial2.models.Usuarios;
import com.examen.parcial2.models.enums.Rol;
import com.examen.parcial2.security.JwtService;
import com.examen.parcial2.services.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UsuarioController(
            UsuarioService usuarioService,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/registro")
    public ResponseEntity<UsuarioResponse> registrar(
            @Valid @RequestBody RegistroRequest request) {

        Usuarios usuario = usuarioService.registrar(
                request.username(),
                request.password(),
                request.rol()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioResponse(
                        usuario.getIdUsuario(),
                        usuario.getUsername(),
                        usuario.getPassword(),
                        usuario.getRol().name()
                ));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.username(),
                                    request.password()
                            )
                    );

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new TokenResponse(token));

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario o contraseña incorrectos"
            );
        }
    }

    public record RegistroRequest(
            @NotBlank
            @Size(max = 50)
            String username,

            @NotBlank
            @Size(min = 8, max = 72)
            String password,

            @NotNull
            Rol rol
    ) {}

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record UsuarioResponse(
            Long id,
            String username,
            String password,
            String rol
    ) {}

    public record TokenResponse(String token) {}
}
