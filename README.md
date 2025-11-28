# Support — Venda de Veículos (guia rápido)

Este repositório contém um backend em Spring Boot para o CRUD de Venda de Veículos.

Pergunta: “aonde que tá ali na esquerda?” — Onde ficam as coisas no painel do projeto

- Ponto de entrada da aplicação (o que você deve rodar):
  - src\main\java\org\example\support\SupportApplication.java

- Controllers (endpoints REST):
  - src\main\java\org\example\support\controller

- Services (regras de negócio):
  - src\main\java\org\example\support\service

- Repositories (Spring Data JPA):
  - src\main\java\org\example\support\repository

- DTOs (Requests/Responses):
  - src\main\java\org\example\support\dto

- Domínio (entities e enums):
  - src\main\java\org\example\support\domain

- Segurança (config):
  - src\main\java\org\example\support\config\SecurityConfig.java

Observação importante: existe também um arquivo gerado pelo template do IntelliJ
src\main\java\org\example\Main.java. Ele NÃO é usado pela aplicação Spring Boot,
serve apenas como exemplo do IntelliJ. Para rodar o sistema use sempre a classe
SupportApplication.

Como rodar rapidamente

1) Pela IDE (IntelliJ IDEA): clique com o direito em SupportApplication e Run.
2) Via Maven: `mvn spring-boot:run` (perfil padrão dev/h2 quando adicionarmos as propriedades).
