package com.desafio.biblioteca_api.controller;

import com.desafio.biblioteca_api.entity.Usuario;
import com.desafio.biblioteca_api.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public Page<Usuario> getAll(Pageable pageable) {
        return usuarioService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return usuarioService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastraUsuario(@RequestBody Usuario usuario){
        Usuario cadastrado = usuarioService.cadastraUsuario(usuario);
        return ResponseEntity.status(201).body(cadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizaUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
        return ResponseEntity.ok(usuarioService.atualizaUsuario(id, usuario));
    }

}
