package com.desafio.biblioteca_api.service;

import com.desafio.biblioteca_api.entity.Livro;
import com.desafio.biblioteca_api.entity.LivroStatus;
import com.desafio.biblioteca_api.repository.LivroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

   public LivroService(LivroRepository LivroRepository) {
       this.livroRepository = LivroRepository;
   }

    public Page<Livro> findAll(Pageable pageable) {
        return livroRepository.findAll(pageable);
    }

   public List<Livro> findAll() {
       return livroRepository.findAll();
   }

   public Optional<Livro> findById(Long id){
       return livroRepository.findById(id);
   }

   public Livro cadastraLivro (Livro livro){
       livro.setStatus(LivroStatus.DISPONIVEL);
       livro.setQuantidadeDisponivel(livro.getQuantidadeTotal());
       return livroRepository.save(livro);
   }

   public Livro atualizaLivro (Long id, Livro atualizado){
       return livroRepository.findById(id).map(livro -> {
           livro.setTitulo(atualizado.getTitulo());
           livro.setAutor(atualizado.getAutor());
           livro.setIsbn(atualizado.getIsbn());
           livro.setAnoPublicacao(atualizado.getAnoPublicacao());
           livro.setQuantidadeTotal(atualizado.getQuantidadeTotal());
           livro.setQuantidadeDisponivel(atualizado.getQuantidadeDisponivel());
           livro.setStatus(atualizado.getStatus());
           return livroRepository.save(livro);
       }).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
   }

   public void deletaLivro (Long id){
       livroRepository.deleteById(id);
   }
}
