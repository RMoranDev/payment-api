# Payment API

API REST em Java para cadastro de clientes e gestão do ciclo de vida de pagamentos. O projeto está em desenvolvimento e serve como uma base de estudo e evolução prática de uma aplicação backend orientada a domínio.

## Estágio atual

O núcleo da aplicação e sua persistência já estão implementados. Neste momento, a API disponibiliza o fluxo de clientes e possui o domínio necessário para criar pagamentos; a camada HTTP de pagamentos e a cobertura de testes ainda são as próximas entregas.

### Implementado

- Cadastro, consulta por ID e atualização parcial de clientes.
- Clientes pessoa física (`INDIVIDUAL`/CPF) e jurídica (`BUSINESS`/CNPJ).
- Normalização de e-mail e documento, além de proteção contra e-mail e documento duplicados.
- Entidades JPA, repositórios Spring Data e regras de domínio para clientes e pagamentos.
- Criação de pagamentos para clientes ativos, inicialmente no estado `PENDING`.
- Transições de domínio de pagamento: aprovação, rejeição e cancelamento.
- Persistência PostgreSQL versionada pelo Flyway, com restrições, chaves e índices.
- Tratamento global inicial para cliente não encontrado e requisição de atualização vazia.
- Teste de carregamento do contexto com PostgreSQL via Testcontainers.

### Ainda não exposto pela API

- Endpoints HTTP para criar, consultar e alterar o estado de pagamentos.
- Alteração de status de cliente.
- Catálogo/listagem com paginação e filtros.
- Documentação OpenAPI/Swagger, autenticação/autorização e implantação conteinerizada.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC, Spring Data JPA e Bean Validation
- PostgreSQL e Flyway
- Maven e Lombok
- JUnit 5 e Testcontainers
- Spring Boot Actuator (dependência já adicionada)

## Arquitetura

O código é organizado por domínio. Cada módulo reúne seus contratos, regras e acesso a dados, enquanto preocupações transversais ficam em `shared`.

```text
src/main/java/dev/rmoran/paymentapi
├── customer/       # API, DTOs, entidade, serviço e repositório de clientes
├── payment/        # DTOs, entidade, serviço e repositório de pagamentos
└── shared/          # tratamento de erros e componentes compartilhados

src/main/resources
├── application.yaml
└── db/migration/    # migrações Flyway
```

## Modelo de domínio

Um cliente possui CPF ou CNPJ, e-mail único e status `ACTIVE` ou `INACTIVE`. Um pagamento pertence a um cliente e registra valor, moeda, descrição, método e estado.

| Campo | Valores atuais |
| --- | --- |
| Tipo de cliente | `INDIVIDUAL`, `BUSINESS` |
| Status do cliente | `ACTIVE`, `INACTIVE` |
| Método de pagamento | `PIX`, `CREDIT_CARD`, `DEBIT_CARD` |
| Status do pagamento | `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED` |
| Moeda persistida | `BRL` |

## Pré-requisitos

- JDK 21
- PostgreSQL em execução, com um banco chamado `payments_db`
- Docker, caso execute os testes de integração com Testcontainers

## Como executar localmente

Crie o banco e informe as credenciais por variáveis de ambiente. No PowerShell:

```powershell
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "sua-senha"
```

Em seguida, na raiz do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicação inicia em `http://localhost:8080`. Ao iniciar, o Flyway aplica a migração disponível em `src/main/resources/db/migration` e o Hibernate valida o esquema.

Em ambientes Unix, use `./mvnw spring-boot:run`.

## Endpoints disponíveis

Base URL: `http://localhost:8080/api/v1`

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/customers` | Cria um cliente. |
| `GET` | `/customers/{id}` | Consulta um cliente pelo UUID. |
| `PATCH` | `/customers/{id}` | Atualiza nome e/ou e-mail de um cliente. |

### Criar cliente

```http
POST /api/v1/customers
Content-Type: application/json

{
  "fullName": "Ana Silva",
  "email": "ana.silva@example.com",
  "document": "12345678901"
}
```

Para pessoa jurídica, informe um CNPJ com 14 dígitos no campo `document`.

### Atualizar cliente

```http
PATCH /api/v1/customers/{id}
Content-Type: application/json

{
  "email": "ana.nova@example.com"
}
```

## Testes

```powershell
.\mvnw.cmd test
```

O teste de contexto usa Testcontainers para iniciar um PostgreSQL. Portanto, o Docker deve estar disponível na máquina antes da execução.

## Próximas fases

1. **Concluir a API de pagamentos** — expor criação, consulta e transições de estado com regras consistentes de resposta e auditoria.
2. **Fortalecer os contratos HTTP** — ativar validação dos corpos de requisição, padronizar respostas de erro e códigos HTTP, e documentar a API com OpenAPI.
3. **Completar operações de clientes** — disponibilizar mudança de status, listagem, paginação e filtros.
4. **Aumentar a qualidade** — adicionar testes unitários de serviços e entidades, testes de integração dos endpoints e cenários de erro/concorrência.
5. **Preparar para produção** — configurar autenticação e autorização, métricas/health checks, logs estruturados, Docker/Compose, CI e estratégia de configuração por ambiente.

## Autor

Ricardo Moran — [RMoranDev](https://github.com/RMoranDev)
