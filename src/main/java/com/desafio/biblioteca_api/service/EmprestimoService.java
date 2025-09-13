package com.desafio.biblioteca_api.service;

import com.desafio.biblioteca_api.dto.EmprestimoDTO;
import com.desafio.biblioteca_api.entity.Emprestimo;
import com.desafio.biblioteca_api.entity.EmprestimoStatus;
import com.desafio.biblioteca_api.entity.LivroStatus;
import com.desafio.biblioteca_api.repository.EmprestimoRepository;
import com.desafio.biblioteca_api.repository.LivroRepository;
import com.desafio.biblioteca_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {


    private EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    private EmprestimoDTO toDTO(Emprestimo emprestimo) {
        return new EmprestimoDTO(
                emprestimo.getId(),
                emprestimo.getUsuario().getId(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getUsuario().getEmail(),
                emprestimo.getLivro().getId(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getLivro().getAutor(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucaoPrevista(),
                emprestimo.getDataDevolucaoReal(),
                emprestimo.getStatus()
        );
    }

    public EmprestimoDTO criaEmprestimo(Long usuarioId, Long livroId) {
        var usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var livro = livroRepository.findById(livroId).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        List<Emprestimo> activeEmprestimo = emprestimoRepository.findByUsuarioId(usuarioId).stream().filter(l -> l.getStatus() == EmprestimoStatus.ATIVO || l.getStatus() == EmprestimoStatus.ATRASADO).toList();

        if (activeEmprestimo.size() >= 3) {
            throw new RuntimeException("Usuário já possui 3 livros emprestados");
        }

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new RuntimeException("Nenhum exemplar disponível");
        }

        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(14));
        emprestimo.setStatus(EmprestimoStatus.ATIVO);

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        if (livro.getQuantidadeDisponivel() <= 0) {
            livro.setStatus(LivroStatus.INDISPONIVEL);
        }
        livroRepository.save(livro);

        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return toDTO(salvo);
    }

    public EmprestimoDTO finalizaEmprestimo(Long emprestimoId){
        var emprestimo = emprestimoRepository.findById(emprestimoId).orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        if (emprestimo.getStatus() != EmprestimoStatus.ATIVO && emprestimo.getStatus() != EmprestimoStatus.ATRASADO) {
            throw new RuntimeException("Este empréstimo já foi devolvido");
        }

        emprestimo.setDataDevolucaoReal(LocalDate.now());
        emprestimo.setStatus(EmprestimoStatus.DEVOLVIDO);

        var livro = emprestimo.getLivro();
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        if (livro.getStatus() == LivroStatus.INDISPONIVEL) {
            livro.setStatus(LivroStatus.DISPONIVEL);
        };
        livroRepository.save(livro);

        Emprestimo atualizado = emprestimoRepository.save(emprestimo);
        return toDTO(atualizado);

    }

    public List<EmprestimoDTO> findAll() {
        return emprestimoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<EmprestimoDTO> findByUsuarioId(Long usuarioId) {
        return emprestimoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

}
