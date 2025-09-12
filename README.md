# biblioteca-api

## Sobre o projeto

O projeto é um **sistema de gerenciamento de biblioteca** desenvolvido como desafio técnico para vaga de desenvolvedor Júnior. O objetivo era o controle de livros, usuários e empréstimos através de uma **API REST**.

## Funcionalidade implementadas:
* Entidades (Livro, Usuário, Empréstimo)
* Repositórios (Livro, Usuario, Emprestimo)
* Serviços (Livro, Usuário, Empréstimo)
* Controladores (Livro, Usuário, Empréstimo)
* Testes unitários(Livro, Usuário, Empréstimo)

## Como executar

### Requisitos
* Java - versão 21.0.8
* Spring Boot - versão 3.5.5

### Passo a passo
1. Clone o repositório
   #### git clone:

   ##### SSH:
   
   ~~~java
   git@github.com:gabrielnvsdev/biblioteca-api.git
   ~~~
   
   ##### HTTP:
   
   ~~~  
    https://github.com/gabrielnvsdev/biblioteca-api.git
   ~~~

3. Execute a aplicação
   
  	 ##### *Via Maven*:

   ~~~java
     ./mvnw spring-boot:run
   ~~~

  	 ##### *Via IDE*:
   
   Importe o projeto e execute a classe **BibliotecaApiApplication.java**
