package com.eclipsehotel.desafio.repositorys;

import com.eclipsehotel.desafio.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByEmailAndPassword(String email, String password);
    Optional<Usuario> findByEmail(String email);
}
