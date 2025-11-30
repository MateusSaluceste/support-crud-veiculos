# Support — Venda de Veículos

Sistema exemplo “nível processo seletivo” com Spring Boot, demonstrando CRUDs, regras de negócio de estoque, segurança, migrações e testes.

Visão geral (features)
- Gestão de Veículos (CRUD com soft delete e ajuste de estoque)
- Gestão de Clientes (CRUD com soft delete)
- Vendas: criar, pagar (baixa estoque), cancelar (devolve estoque)
- Validações: placa e CPF únicos; Bean Validation nos DTOs
- Autenticação e autorização (HTTP Basic), roles ADMIN e VENDEDOR
- Migrações Flyway + seeds para demo
- Perfil dev com H2; perfil prod com PostgreSQL
- Testes unitários (regras de venda/estoque) e 1 de controller (MockMvc)
- Tratamento de erros com ControllerAdvice (400/404/409)

Estrutura (onde encontrar no painel da esquerda)
- Ponto de entrada: src\main\java\org\example\support\SupportApplication.java
- Controllers: src\main\java\org\example\support\controller
- Services: src\main\java\org\example\support\service
- Repositories: src\main\java\org\example\support\repository
- DTOs: src\main\java\org\example\support\dto
- Domínio: src\main\java\org\example\support\domain
- Segurança: src\main\java\org\example\support\config\SecurityConfig.java
- Migrações: src\main\resources\db\migration
- Testes: src\test\java\org\example\support

Observação: src\main\java\org\example\Main.java é apenas um exemplo do IntelliJ e NÃO é usado.

Como rodar
1) Via IDE (IntelliJ): Run em SupportApplication (profile dev é padrão)
2) Via Maven: `mvn spring-boot:run`

Perfis e banco
- Padrão: profile dev (H2 em memória)
  - Config: src/main/resources/application-dev.properties
  - H2 Console: http://localhost:8080/h2-console (JDBC: jdbc:h2:mem:supportdb, user sa, senha vazia)
  - JPA ddl-auto=validate (Flyway cria/atualiza o schema)
- Prod (PostgreSQL): application-prod.properties (ajuste JDBC_URL/DB_USER/DB_PASS)

Migrações Flyway
- V1__init.sql: cria tabelas e constraints
- V2__seed.sql: insere clientes e veículos de demonstração
- Usuários são criados via Java seeder (DataSeeder)

Login e credenciais (seed)
- admin / admin123 — ROLE_ADMIN
- vendedor / vendedor123 — ROLE_VENDEDOR

Segurança
- HTTP Basic em /api/**
- Permite acesso público ao H2 Console e (opcional) Swagger UI

Swagger / OpenAPI
- Rotas liberadas em SecurityConfig: /swagger-ui/** e /v3/api-docs/**
- Dependência do Swagger pode estar comentada no pom.xml dependendo do ambiente; se necessário, habilite-a e acesse: http://localhost:8080/swagger-ui/index.html

Endpoints principais (exemplos curl)
- Veículos
  - Listar: `curl -u admin:admin123 "http://localhost:8080/api/veiculos"`
  - Criar:
    curl -u admin:admin123 -H "Content-Type: application/json" -d '{
      "marca":"Toyota","modelo":"Corolla","ano":2020,"placa":"AAA1A11",
      "precoVendaSugerido":95000,"quantidadeEmEstoque":3
    }' http://localhost:8080/api/veiculos
  - Ajustar estoque: `curl -u admin:admin123 -X PATCH "http://localhost:8080/api/veiculos/1/estoque?quantidade=10"`

- Clientes
  - Listar: `curl -u vendedor:vendedor123 "http://localhost:8080/api/clientes"`
  - Criar:
    curl -u vendedor:vendedor123 -H "Content-Type: application/json" -d '{
      "nome":"João","cpf":"12345678901"
    }' http://localhost:8080/api/clientes

- Vendas
  - Criar venda:
    curl -u vendedor:vendedor123 -H "Content-Type: application/json" -d '{
      "clienteId":1,
      "formaPagamento":"PIX",
      "itens":[{"veiculoId":1,"quantidade":1}]
    }' http://localhost:8080/api/vendas
  - Pagar: `curl -u vendedor:vendedor123 -X PATCH http://localhost:8080/api/vendas/1/pagar`
  - Cancelar: `curl -u vendedor:vendedor123 -X PATCH http://localhost:8080/api/vendas/1/cancelar`

Regras de negócio (estoque)
- Não permite pagar venda com estoque insuficiente
- Ao pagar, baixa o estoque do(s) veículo(s)
- Ao cancelar uma venda paga, devolve o estoque

Erros e validações
- 400: validação de campos e regras de negócio (mensagens claras)
- 404: recurso não encontrado
- 409: violação de integridade (unicidade placa/cpf)

Testes
- Rodar: `mvn -q test`
- Cobrem regras de estoque (VendaService) e 1 endpoint (VeiculoController com MockMvc)

Coleção Postman
- Arquivo: docs/postman_collection.json
- Inclui requests para login (Basic), Veículos, Clientes e fluxo de Vendas

Como explicar na entrevista
- Arquitetura em camadas (controller/service/repository/domain/dto/config/exception)
- Regras de venda concentradas no VendaService; transações com @Transactional
- Segurança simples e efetiva com Basic Auth e roles
- Migrações versionadas (Flyway) e perfil dev com H2 para demo rápida
- Tratamento de erros centralizado (ControllerAdvice)
- Testes que provam as regras críticas de estoque

Repositório e higiene
- .gitignore ignora IDE/build/logs; .editorconfig padroniza formatação

Notas de build (CI)
- Encoding/Filtering: o pom.xml define UTF-8 (source/reporting) e desabilita filtering em src/main/resources para evitar erros de MalformedInputException no Linux (GitHub Actions) ao processar arquivos .properties que não usam placeholders.
