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

## Status

🔧 Em desenvolvimento — 1ª entrega (Bimestre 1): documento de concepção, diagrama de classe e DER.
