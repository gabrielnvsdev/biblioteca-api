package com.desafio.biblioteca_api.controller;

import com.desafio.biblioteca_api.entity.Livro;
import com.desafio.biblioteca_api.service.LivroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService){
        this.livroService = livroService;
    }

    @GetMapping
    public Page<Livro> getAll(Pageable pageable){
        return livroService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> getById(@PathVariable Long id){
        return livroService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Livro> cadastraLivro(@RequestBody Livro livro){
        Livro cadastrado = livroService.cadastraLivro(livro);
        return ResponseEntity.status(201).body(cadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizaLivro(@PathVariable Long id, @RequestBody Livro livro){
        return ResponseEntity.ok(livroService.atualizaLivro(id, livro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLivro(@PathVariable Long id) {
        livroService.removeLivro(id);
        return ResponseEntity.noContent().build();
    }
}
