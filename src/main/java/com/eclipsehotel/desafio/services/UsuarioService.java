package com.eclipsehotel.desafio.services;

import com.eclipsehotel.desafio.models.Usuario;
import com.eclipsehotel.desafio.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrar(Usuario usuario) {
        // Pode incluir uma validação aqui para garantir que o email é único
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario autenticar(String email, String password) {
        return usuarioRepository.findByEmailAndPassword(email, password).orElse(null);
    }
}
