package com.desafio.biblioteca_api.service;

import com.desafio.biblioteca_api.entity.Usuario;
import com.desafio.biblioteca_api.entity.UsuarioStatus;
import com.desafio.biblioteca_api.repository.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveRetornarTodosUsuariosPaginados() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Usuario> page = new PageImpl<>(List.of(new Usuario()));
        when(usuarioRepository.findAll(pageable)).thenReturn(page);

        Page<Usuario> result = usuarioService.findAll(pageable);

        Assertions.assertThat(result.getContent()).hasSize(1);
        verify(usuarioRepository).findAll(pageable);
    }

    @Test
    void deveCriarUsuarioComStatusAtivo() {
        Usuario usuario = new Usuario();
        usuario.setNome("Gabriel");
        usuario.setEmail("gabriel@email.com");
        usuario.setCpf("12345678900");
        usuario.setStatus(UsuarioStatus.ATIVO);



        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario usuarioTeste = usuarioService.cadastraUsuario(usuario);

        assertEquals(UsuarioStatus.ATIVO, usuarioTeste.getStatus());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void deveEncontrarUsuarioPorId() {
        Usuario usuario = new Usuario();
        usuario.setNome("Gabriel");
        usuario.setEmail("gabriel@email.com");
        usuario.setCpf("12345678900");
        usuario.setStatus(UsuarioStatus.ATIVO);
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Gabriel", result.get().getNome());
    }

    @Test
    void deveFalharAoCadastrarUsuarioComEmailInvalido() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("email-invalido");
        usuario.setCpf("12345678901");
        usuario.setStatus(UsuarioStatus.ATIVO);

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));


        Usuario salvo = usuarioService.cadastraUsuario(usuario);


        assertNotNull(salvo);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Usuario existente = new Usuario();

        existente.setNome("Nome Teste");
        existente.setEmail("teste@email.com");
        existente.setCpf("123456789-01");
        existente.setStatus(UsuarioStatus.INATIVO);

        Usuario atualizado = new Usuario();

        atualizado.setNome("Gabriel");
        atualizado.setEmail("gabriel@gmail.com");
        atualizado.setStatus(UsuarioStatus.ATIVO);


        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Usuario result = usuarioService.atualizaUsuario(1L, atualizado);

        assertEquals("Gabriel", result.getNome());
        assertEquals("123456789-01", result.getCpf());
        verify(usuarioRepository).save(existente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioNaoExistente() {
        when(usuarioRepository.findById(42L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.atualizaUsuario(42L, new Usuario()));
        assertEquals("Usuário não encontrado", ex.getMessage());
    }

}
