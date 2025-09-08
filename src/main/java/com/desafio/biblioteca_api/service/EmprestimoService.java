package com.desafio.biblioteca_api.service;

import com.desafio.biblioteca_api.entity.Emprestimo;
import com.desafio.biblioteca_api.entity.EmprestimoStatus;
import com.desafio.biblioteca_api.entity.Livro;
import com.desafio.biblioteca_api.entity.LivroStatus;
import com.desafio.biblioteca_api.repository.EmprestimoRepository;
import com.desafio.biblioteca_api.repository.LivroRepository;
import com.desafio.biblioteca_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    public Emprestimo createEmprestimo(Long usuarioId, Long livroId) {
        var usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var livro = livroRepository.findById(livroId).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        List<Emprestimo> activeEmprestimo = emprestimoRepository.findByUsuario(usuario).stream().filter(l -> l.getStatus() == EmprestimoStatus.ATIVO || l.getStatus() == EmprestimoStatus.ATRASADO).toList();

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

        return emprestimoRepository.save(emprestimo);
    }

    public Emprestimo returnEmprestimo(Long emprestimoId){
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

        return emprestimoRepository.save(emprestimo);
    }

    public List<Emprestimo> findAll() {
        return emprestimoRepository.findAll();
    }

    public List<Emprestimo> findByUsuario(Long usuarioId) {
        var usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return emprestimoRepository.findByUsuario(usuario);
    }
}
