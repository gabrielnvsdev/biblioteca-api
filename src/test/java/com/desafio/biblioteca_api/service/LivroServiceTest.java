package com.desafio.biblioteca_api.service;

import com.desafio.biblioteca_api.entity.Livro;
import com.desafio.biblioteca_api.entity.LivroStatus;
import com.desafio.biblioteca_api.repository.LivroRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    @Test
    void deveRetornarTodosLivrosPaginados() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livro> page = new PageImpl<>(List.of(new Livro()));
        when(livroRepository.findAll(pageable)).thenReturn(page);

        Page<Livro> result = livroService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        verify(livroRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve criar livro e garantir que quantidade disponivel e status estejão corretos")
    void deveCriarLivroCorretamente() {
        //Given
        Livro livro = new Livro();
        livro.setTitulo("Titulo teste");
        livro.setAutor("autor teste");
        livro.setIsbn("12345");
        livro.setQuantidadeTotal(5);

        when(livroRepository.save(any(Livro.class))).thenAnswer(inv -> inv.getArgument(0));

        //When
        Livro saved = livroService.cadastraLivro(livro);


        //Then
        assertEquals(5, saved.getQuantidadeDisponivel());
        assertEquals(LivroStatus.DISPONIVEL, saved.getStatus());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    @DisplayName("Deve retornar um livro por Id se existir")
    void deveBuscarLivroPorId() {
        //Given
        Livro livro = new Livro();
        livro.setId(1L);
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        //When
        Optional<Livro> result = livroService.findById(1L);

        //Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void deveAtualizarLivroComSucesso() {
        //Given
        Livro existente = new Livro();
        existente.setTitulo("Titulo Teste");
        existente.setAutor("autor Teste");
        existente.setIsbn("12345");
        existente.setQuantidadeTotal(5);

        //Given
        Livro atualizado = new Livro();
        atualizado.setTitulo("Novo Titulo");
        atualizado.setAutor("Novo Autor");
        atualizado.setIsbn("456");
        atualizado.setQuantidadeTotal(7);

        when(livroRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(livroRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //When
        Livro result = livroService.atualizaLivro(1L, atualizado);

        //Then
        assertEquals("Novo Titulo", result.getTitulo());
        assertEquals("Novo Autor", result.getAutor());
        verify(livroRepository).save(existente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarLivroNaoExistente() {
        when(livroRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> livroService.atualizaLivro(99L, new Livro()));
        assertEquals("Livro não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Deve definir livro como indisponível quando a quantidade disponivel for zero")
    void deveDefinirLivroIndisponivelQuandoQuantidadeZero() {
        //Given
        Livro livro = new Livro();
        livro.setTitulo("Titulo teste");
        livro.setAutor("autor teste");
        livro.setIsbn("12345");
        livro.setQuantidadeTotal(0);

        when(livroRepository.save(any(Livro.class))).thenAnswer(inv -> inv.getArgument(0));

        //When
        Livro saved = livroService.cadastraLivro(livro);

        //Then
        assertEquals(LivroStatus.INDISPONIVEL, saved.getStatus());
        assertEquals(0, saved.getQuantidadeDisponivel());
    }

    @Test
    void deveRemoverLivroPorId() {
        livroService.removeLivro(1L);
        verify(livroRepository).deleteById(1L);
    }

}
