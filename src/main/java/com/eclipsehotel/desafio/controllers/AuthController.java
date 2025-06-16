package com.eclipsehotel.desafio.controllers;

import com.eclipsehotel.desafio.models.Usuario;
import com.eclipsehotel.desafio.services.UsuarioService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // Classe auxiliar para requisição de login
    public static class LoginRequest {
        public String email;
        public String password;
    }

    // Classe auxiliar para requisição de registro
    public static class RegisterRequest {
        public String name;
        public String email;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Usuario autenticado = usuarioService.autenticar(request.email, request.password);

            if (autenticado != null) {
                // Criar resposta com dados do usuário (sem senha)
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login realizado com sucesso");
                response.put("user", Map.of(
                        "id", autenticado.getId(),
                        "name", autenticado.getName(),
                        "email", autenticado.getEmail()
                ));

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Email ou senha inválidos");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Criar novo usuário
            Usuario novoUsuario = new Usuario();
            novoUsuario.setName(request.name);
            novoUsuario.setEmail(request.email);
            novoUsuario.setPassword(request.password);

            Usuario usuarioSalvo = usuarioService.registrar(novoUsuario);

            // Criar resposta com dados do usuário (sem senha)
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuário registrado com sucesso");
            response.put("user", Map.of(
                    "id", usuarioSalvo.getId(),
                    "name", usuarioSalvo.getName(),
                    "email", usuarioSalvo.getEmail()
            ));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logout realizado com sucesso");
        return ResponseEntity.ok(response);
    }
}

