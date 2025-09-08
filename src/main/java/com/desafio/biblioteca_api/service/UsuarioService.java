package com.desafio.biblioteca_api.service;

import com.desafio.biblioteca_api.entity.Usuario;
import com.desafio.biblioteca_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAl(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id){
        return usuarioRepository.findById(id);
    }

    public Usuario create (Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Usuario update (Long id, Usuario atualizado){
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNome(atualizado.getNome());
            usuario.setEmail(atualizado.getEmail());
            usuario.setCpf(atualizado.getCpf());
            usuario.setStatus(atualizado.getStatus());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
