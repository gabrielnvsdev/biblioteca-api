package com.desafio.biblioteca_api.service;

import com.desafio.biblioteca_api.entity.Usuario;
import com.desafio.biblioteca_api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public List<Usuario> findAl(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id){
        return usuarioRepository.findById(id);
    }

    public Usuario cadastraUsuario (Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizaUsuario (Long id, Usuario atualizado){
        return usuarioRepository.findById(id).map(usuario -> {
            if (atualizado.getNome() != null) usuario.setNome(atualizado.getNome());
            if (atualizado.getEmail() != null) usuario.setEmail(atualizado.getEmail());
            if (atualizado.getCpf() != null) usuario.setCpf(atualizado.getCpf());
            if (atualizado.getStatus() != null) usuario.setStatus(atualizado.getStatus());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
