package com.desafio.biblioteca_api.controller;

import com.desafio.biblioteca_api.entity.Emprestimo;
import com.desafio.biblioteca_api.entity.Livro;
import com.desafio.biblioteca_api.entity.Usuario;
import com.desafio.biblioteca_api.service.EmprestimoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public List<Emprestimo> findAll(){
        return emprestimoService.findAll();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Emprestimo> getByUsuario(@PathVariable Long usuarioId){
        return emprestimoService.findByUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<Emprestimo> criaEmprestimo(@RequestParam Long usuarioId, @RequestParam Long livroId){
        Emprestimo criado = emprestimoService.criaEmprestimo(usuarioId, livroId);
        return ResponseEntity.status(201).body(criado);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Emprestimo> finalizaEmprestimo(@PathVariable Long id){
        return ResponseEntity.ok(emprestimoService.finalizaEmprestimo(id));
    }
}
