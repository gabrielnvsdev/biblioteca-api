package com.desafio.biblioteca_api.repository;

import com.desafio.biblioteca_api.entity.Usuario;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCpf(String cpf);
    Optional<Email> findByEmail(String email);
}
