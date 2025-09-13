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
* Apache Maven - versão 3.9.9
* PostgreSQL - versão 17.6

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


2. Preparando a aplicação
   
Em **application.yml**, onde se encontra o profile de produção, substitua os campos abaixo para os respectivos configurados do seu banco PostgreSQL

* Url;
* Username
* Password


3. Executando a aplicação
   
  	##### *Via Maven*:

   ~~~java
     ./mvnw spring-boot:run
   ~~~

  	 ##### *Via IDE*:
   
   Importe o projeto e execute a classe **BibliotecaApiApplication.java**

   ##### *Alterando profile na hora da execução*

   ~~~java
   mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
   ~~~


5. Executando testes
   ##### *No terminal:*
~~~
mvn clean test jacoco:report
~~~
