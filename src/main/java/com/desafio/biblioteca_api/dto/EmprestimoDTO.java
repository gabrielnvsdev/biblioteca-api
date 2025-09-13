package com.desafio.biblioteca_api.dto;

import com.desafio.biblioteca_api.entity.EmprestimoStatus;

import java.time.LocalDate;

public class EmprestimoDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioEmail;
    private Long livroId;
    private String livroTitulo;
    private String livroAutor;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private EmprestimoStatus status;

    public EmprestimoDTO() {}

    public EmprestimoDTO(Long id, Long usuarioId, String usuarioNome, String usuarioEmail,
                         Long livroId, String livroTitulo, String livroAutor,
                         LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista,
                         LocalDate dataDevolucaoReal, EmprestimoStatus status) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.usuarioEmail = usuarioEmail;
        this.livroId = livroId;
        this.livroTitulo = livroTitulo;
        this.livroAutor = livroAutor;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }

    public Long getLivroId() { return livroId; }
    public void setLivroId(Long livroId) { this.livroId = livroId; }

    public String getLivroTitulo() { return livroTitulo; }
    public void setLivroTitulo(String livroTitulo) { this.livroTitulo = livroTitulo; }

    public String getLivroAutor() { return livroAutor; }
    public void setLivroAutor(String livroAutor) { this.livroAutor = livroAutor; }

    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }

    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }

    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }

    public EmprestimoStatus getStatus() { return status; }
    public void setStatus(EmprestimoStatus status) { this.status = status; }
}