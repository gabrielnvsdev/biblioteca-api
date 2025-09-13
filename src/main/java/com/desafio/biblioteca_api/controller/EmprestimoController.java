package com.desafio.biblioteca_api.controller;

import com.desafio.biblioteca_api.dto.EmprestimoDTO;
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
    public List<EmprestimoDTO> findAll(){
        return emprestimoService.findAll();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<EmprestimoDTO> getByUsuarioId(@PathVariable Long usuarioId){
        return emprestimoService.findByUsuarioId(usuarioId);
    }

    @PostMapping
    public ResponseEntity<EmprestimoDTO> criaEmprestimo(@RequestParam Long usuarioId, @RequestParam Long livroId){
        EmprestimoDTO criado = emprestimoService.criaEmprestimo(usuarioId, livroId);
        return ResponseEntity.status(201).body(criado);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<EmprestimoDTO> finalizaEmprestimo(@PathVariable Long id){
        return ResponseEntity.ok(emprestimoService.finalizaEmprestimo(id));
    }
}
