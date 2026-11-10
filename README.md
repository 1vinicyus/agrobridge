# AgroBridge

Plataforma inteligente para conexão entre agricultores, fornecedores de insumos e assistência técnica rural.

**AEP — Atividade de Estudo Programada · 4º Semestre (2026.2) · Unicesumar**
Cursos: Engenharia de Software (Esoft) e Análise e Desenvolvimento de Sistemas (ADS)
ODS alinhada: **ODS 2 — Fome Zero e Agricultura Sustentável**

## Integrantes

- Vinicyus Eduardo Vicentini Faria — RA 25143744-2
- Arthur Ferreira dos Santos — RA 25180010-2

## Estrutura do repositório

```
/src        -> código-fonte da aplicação (classes Java organizadas por pacote/camada)
/docs       -> documentação do projeto (PDF da entrega, diagrama de classe, DER)
/database   -> script(s) SQL de criação das tabelas (compatível com as classes Java)
```

## Lista de Requisitos

1. O sistema deve permitir o cadastro de agricultores, com nome completo, CPF, e-mail, telefone e ao menos uma propriedade rural vinculada (nome, localização e área em hectares).
2. O sistema deve permitir o cadastro de fornecedores de insumos, com nome completo/razão social, CNPJ, e-mail, telefone e área de atuação.
3. O sistema deve permitir o cadastro de agrônomos, com nome completo, e-mail, telefone, registro profissional (CREA) e especialidade.
4. O sistema deve permitir que o agricultor registre uma solicitação de cotação para um insumo, informando o produto desejado, a quantidade e a propriedade relacionada.
5. O sistema deve permitir que fornecedores respondam a uma solicitação com uma cotação (preço, prazo de entrega e condições de pagamento), possibilitando ao agricultor comparar múltiplas propostas recebidas para a mesma solicitação.
6. O sistema deve permitir que agrônomos registrem consultorias técnicas prestadas a uma propriedade rural, contendo data do atendimento, diagnóstico e recomendações.
7. O sistema deve manter um histórico de atendimentos (cotações e consultorias) vinculado a cada propriedade rural, consultável pelo agricultor responsável.
8. O sistema deve notificar cada perfil de usuário de forma personalizada e polimórfica: cotações recebidas para o agricultor, novas solicitações para o fornecedor e novas consultorias para o agrônomo.

## Cronograma de Execução

| Semana | Período | Atividade | Responsável |
|---|---|---|---|
| 1 | 04/09 a 10/09 | Levantamento de requisitos, definição das dores das partes interessadas e escopo do projeto | Vinicyus e Arthur |
| 2 | 11/09 a 17/09 | Modelagem do Diagrama de Classe (herança, composição 1:N e polimorfismo) | Arthur Ferreira dos Santos |
| 3 | 18/09 a 24/09 | Modelagem do Diagrama do Banco de Dados (DER) e definição da arquitetura em camadas | Vinicyus Eduardo V. Faria |
| 4 | 25/09 a 01/10 | Estruturação do repositório GitHub (/src, /docs, /database) e redação da Justificativa Técnica | Vinicyus e Arthur |
| 5 | 02/10 a 08/10 | Revisão geral do documento, ajustes finais e envio da 1ª entrega | Vinicyus e Arthur |

## Justificativa técnica

- **Java** — paradigma orientado a objetos, necessário para a herança entre `Usuario` e as classes `Agricultor`, `Fornecedor` e `Agronomo`, e para o polimorfismo via interface `Notificavel`.
- **PostgreSQL** — robustez em relacionamentos, essencial para a integridade entre `Solicitacao`, `Cotacao` e `Consultoria`.
- **Arquitetura em camadas** — isola regras de negócio de apresentação e acesso a dados, favorecendo o desenvolvimento incremental (1ª entrega: modelagem; 2ª entrega: implementação).

## Estrutura do projeto

```
agrobridge/
├── pom.xml                      -> build Maven (driver PostgreSQL + exec)
├── README.md
├── database/database.sql        -> script SQL (tabelas + dados de exemplo)
├── docs/                        -> PDF da 1ª entrega e diagramas (classe e DER)
└── src/main/
    ├── java/com/agrobridge/
    │   ├── Main.java            -> ponto de entrada
    │   ├── model/               -> entidades (Usuario abstrata, Notificavel, subclasses)
    │   ├── dao/                 -> acesso ao PostgreSQL via JDBC (CRUD)
    │   ├── service/             -> regras de negócio (cotações, consultorias, histórico, notificações)
    │   ├── view/                -> menus de console
    │   └── util/                -> ConnectionFactory, SenhaUtil, Validador
    └── resources/db.properties  -> configuração da conexão
```

## Pilares de POO aplicados

| Pilar | Onde |
|---|---|
| Interface | `Notificavel` (implementada por `Usuario`); `CrudDAO<T>` (implementada pelos DAOs) |
| Classe abstrata | `Usuario`, `UsuarioDAO<T>`, `MenuCrud`, `MenuUsuario<T>` |
| Herança | `Agricultor`, `Fornecedor`, `Agronomo` estendem `Usuario`; `AgricultorDAO`, `FornecedorDAO`, `AgronomoDAO` estendem `UsuarioDAO` |
| Polimorfismo (`@Override`) | `enviarNotificacao()` sobrescrito em cada subclasse e chamado via `Notificavel` em `AgroBridgeService.notificar()` |
| Composição 1:N | `Agricultor` possui uma lista de `PropriedadeRural` |

## Requisitos x implementação

| Requisito | Onde está no código |
|---|---|
| 1 - Cadastro de agricultores com propriedade | Menu 1 (`MenuAgricultor`, `AgricultorDAO`) |
| 2 - Cadastro de fornecedores | Menu 2 (`MenuFornecedor`, `FornecedorDAO`) |
| 3 - Cadastro de agrônomos | Menu 3 (`MenuAgronomo`, `AgronomoDAO`) |
| 4 - Solicitação de cotação | Menu 5 (`AgroBridgeService.solicitarCotacao`) |
| 5 - Cotações e comparação de propostas | Menu 6 (`registrarCotacao`, `compararCotacoes`) |
| 6 - Consultorias técnicas | Menu 7 (`registrarConsultoria`) |
| 7 - Histórico por propriedade | Menu 8 (`historicoPropriedade`) |
| 8 - Notificações por perfil | `enviarNotificacao()` polimórfico (menu 10 e fluxos de cotação/consultoria) |

O CRUD completo (Create, Read, Update, Delete) da entidade principal, `Agricultor`, está no menu 1; as demais entidades também possuem CRUD completo.

## Como executar

### Pré-requisitos

- Java JDK 17 ou superior (`java -version`)
- PostgreSQL 14 ou superior em execução
- Maven 3.8+ (`mvn -version`)
- Windows, Linux ou macOS

### 1. Criar o banco de dados

```bash
# Linux/macOS
psql -U postgres -c "CREATE DATABASE agrobridge;"
psql -U postgres -d agrobridge -f database/database.sql
```

```bat
:: Windows (prompt com o psql no PATH)
psql -U postgres -c "CREATE DATABASE agrobridge;"
psql -U postgres -d agrobridge -f database\database.sql
```

### 2. Configurar a conexão

Edite `src/main/resources/db.properties` com seu usuário e senha do PostgreSQL
(ou defina as variáveis de ambiente `AGROBRIDGE_DB_URL`, `AGROBRIDGE_DB_USER` e `AGROBRIDGE_DB_PASSWORD`).

### 3. Compilar e executar

```bash
mvn compile exec:java -Dexec.mainClass=com.agrobridge.Main
```

O Maven baixa o driver JDBC automaticamente. Ao iniciar, o programa testa a conexão e abre o menu.
A opção **9 (Demonstração automática)** executa um roteiro completo de CRUD, polimorfismo e fluxo de cotações
no banco, e remove os dados criados ao final.

No Windows, se os acentos aparecerem errados, execute antes `chcp 65001`.

## Status

Em desenvolvimento - 2ª entrega (Bimestre 2): código-fonte com POO, CRUD completo e persistência em PostgreSQL.
