package com.eclipsehotel.desafio.controllers;

import com.eclipsehotel.desafio.models.Usuario;
import com.eclipsehotel.desafio.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario, HttpSession session) {
        Usuario autenticado = usuarioService.autenticar(usuario.getEmail(), usuario.getPassword());

        if (autenticado != null) {
            session.setAttribute("usuario", autenticado);
            return ResponseEntity.ok("Login realizado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout realizado com sucesso");
    }




}
