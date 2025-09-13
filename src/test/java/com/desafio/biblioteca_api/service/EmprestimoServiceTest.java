package com.desafio.biblioteca_api.service;

import com.desafio.biblioteca_api.dto.EmprestimoDTO;
import com.desafio.biblioteca_api.entity.*;
import com.desafio.biblioteca_api.repository.EmprestimoRepository;
import com.desafio.biblioteca_api.repository.LivroRepository;
import com.desafio.biblioteca_api.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private EmprestimoService emprestimoService;

    @Test
    void deveRetornarTodosEmprestimos() {
        when(emprestimoRepository.findAll()).thenReturn(List.of(new Emprestimo()));

        List<EmprestimoDTO> result = emprestimoService.findAll();

        assertEquals(1, result.size());
        verify(emprestimoRepository).findAll();
    }

    @Test
    void deveRetornarEmprestimosPorUsuario() {
        Usuario usuario = new Usuario();

        usuario.setNome("Nome teste");
        usuario.setEmail("teste@email.com");
        usuario.setCpf("111222333-44");
        usuario.setStatus(UsuarioStatus.ATIVO);

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Livro Teste");
        livro.setQuantidadeTotal(3);
        livro.setQuantidadeDisponivel(2);
        livro.setStatus(LivroStatus.DISPONIVEL);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setUsuario(usuario);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(emprestimoRepository.findByUsuarioId(1L)).thenReturn(List.of(emprestimo));

        List<EmprestimoDTO> result = emprestimoService.findByUsuarioId(1L);

        assertEquals(1, result.size());
        assertEquals(usuario, result.get(0).getUsuarioNome());
        verify(emprestimoRepository).findByUsuarioId(1L);
    }

    @Test
    void deveLancarExcecaoSeUsuarioNaoExistir() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emprestimoService.findByUsuarioId(99L));
        assertEquals("Usuário não encontrado", ex.getMessage());
    }


    @Test
    @DisplayName("Deve criar o emprestimo e garantir que ele atualize a quantidade disponivel do livro")
    void deveCriarEmprestimoCorretamente() {
        //Given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Gabriel");
        usuario.setEmail("gabriel@email.com");
        usuario.setCpf("12345678900");
        usuario.setStatus(UsuarioStatus.ATIVO);

        //Given
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Livro Teste");
        livro.setQuantidadeTotal(3);
        livro.setQuantidadeDisponivel(2);
        livro.setStatus(LivroStatus.DISPONIVEL);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.findByUsuarioId(1L)).thenReturn(List.of());
        when(emprestimoRepository.save(any(Emprestimo.class))).thenAnswer(inv -> inv.getArgument(0));

        //When
        EmprestimoDTO emprestimo = emprestimoService.criaEmprestimo(1L, 1L);

        //Then
        assertNotNull(emprestimo);
        assertEquals(EmprestimoStatus.ATIVO, emprestimo.getStatus());
        assertEquals(1, livro.getQuantidadeDisponivel());
        assertNotNull(emprestimo.getDataDevolucaoPrevista());
        assertEquals(LocalDate.now().plusDays(14), emprestimo.getDataDevolucaoPrevista());
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro estiver com status indisponivel")
    void deveLancarExcecaoQuandoLivroIndisponivel() {

        //Given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setStatus(UsuarioStatus.ATIVO);
        //Given

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setQuantidadeDisponivel(0);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        //When
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emprestimoService.criaEmprestimo(1L, 1L));

        //Then
        assertEquals("Nenhum exemplar disponível", ex.getMessage());
    }


    @Test
    @DisplayName("Deve lançar exceçao se usuario já tiver atingido o limite de três livros emprestados")
    void DeveLancarExcecaoSeUsuarioAtingiuLimiteEmprestimos() {
        //Given
        Usuario usuario = new Usuario();
        usuario.setNome("Gabriel");
        usuario.setEmail("teste@gmail.com");
        usuario.setCpf("12345678910");
        usuario.setStatus(UsuarioStatus.ATIVO);
        usuario.setId(1L);

        //Given
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setQuantidadeDisponivel(1);

        //Given
        Emprestimo emprestimo1 = new Emprestimo(usuario, livro); emprestimo1.setStatus(EmprestimoStatus.ATIVO);
        Emprestimo emprestimo2 = new Emprestimo(usuario, livro); emprestimo2.setStatus(EmprestimoStatus.ATRASADO);
        Emprestimo emprestimo3 = new Emprestimo(usuario, livro); emprestimo3.setStatus(EmprestimoStatus.ATIVO);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.findByUsuarioId(1L)).thenReturn(List.of(emprestimo1, emprestimo2, emprestimo3));

        //When
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emprestimoService.criaEmprestimo(1L, 1L));

        //Then
        assertEquals("Usuário já possui 3 livros emprestados", ex.getMessage());
    }

    @Test
    @DisplayName("Deve registrar a devolução de um emprestimo corretamente")
    void deveRegistrarDevolucaoCorretamente() {
        //Given
        Usuario usuario = new Usuario();
        usuario.setNome("Gabriel");
        usuario.setEmail("teste@gmail.com");
        usuario.setCpf("12345678910");
        usuario.setStatus(UsuarioStatus.ATIVO);
        usuario.setId(1L);

        //Given
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setQuantidadeDisponivel(2);

        //Given
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setId(1L);
        emprestimo.setStatus(EmprestimoStatus.ATIVO);

        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenAnswer(inv -> inv.getArgument(0));

        //When
        EmprestimoDTO returned = emprestimoService.finalizaEmprestimo(1L);

        //Then
        assertEquals(EmprestimoStatus.DEVOLVIDO, returned.getStatus());
        assertNotNull(returned.getDataDevolucaoReal());
        assertEquals(3, livro.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve lançar exceção ao devolver emprestimo já devolvido anteriormente")
    void deveLancarExcecaoAoDevolverEmprestimoJaDevolvido() {

        //Given
        Usuario usuario = new Usuario();
        Livro livro = new Livro();
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        emprestimo.setId(1L);
        emprestimo.setStatus(EmprestimoStatus.DEVOLVIDO);

        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        //When
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emprestimoService.finalizaEmprestimo(1L));

        //Then
        assertEquals("Este empréstimo já foi devolvido", ex.getMessage());
    }
}

