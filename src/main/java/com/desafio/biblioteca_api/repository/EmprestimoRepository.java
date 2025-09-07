package com.desafio.biblioteca_api.repository;

import com.desafio.biblioteca_api.entity.Emprestimo;
import com.desafio.biblioteca_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuario(Usuario usuario);
}
