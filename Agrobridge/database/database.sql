-- =====================================================================
-- AgroBridge — Script de criação do banco de dados (PostgreSQL)
-- AEP 4º Semestre (2026.2) — Unicesumar
-- Integrantes: Vinicyus Eduardo Vicentini Faria (RA 25143744-2)
--              Arthur Ferreira dos Santos (RA 25180010-2)
--
-- Estrutura: herança de tabelas (usuario é a tabela-base de
-- agricultor, fornecedor e agronomo), refletindo a herança e o
-- polimorfismo do diagrama de classe (interface Notificavel).
-- =====================================================================

BEGIN;

-- ---------------------------------------------------------------------
-- 1. Limpeza (permite reexecutar o script em ambiente de desenvolvimento)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS consultoria CASCADE;
DROP TABLE IF EXISTS cotacao CASCADE;
DROP TABLE IF EXISTS solicitacao CASCADE;
DROP TABLE IF EXISTS propriedade_rural CASCADE;
DROP TABLE IF EXISTS agronomo CASCADE;
DROP TABLE IF EXISTS fornecedor CASCADE;
DROP TABLE IF EXISTS agricultor CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

-- ---------------------------------------------------------------------
-- 2. Tabela-base: usuario
--    (equivalente à classe abstrata Usuario, que implementa Notificavel)
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
    id             BIGSERIAL PRIMARY KEY,
    nome_completo  VARCHAR(150) NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    senha          VARCHAR(255) NOT NULL,
    telefone       VARCHAR(20),
    tipo_usuario   VARCHAR(20) NOT NULL
                   CHECK (tipo_usuario IN ('AGRICULTOR', 'FORNECEDOR', 'AGRONOMO')),
    criado_em      TIMESTAMP NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------
-- 3. Subtipos de usuario (herança 1:1 — cada subtipo estende usuario)
-- ---------------------------------------------------------------------
CREATE TABLE agricultor (
    id   BIGINT PRIMARY KEY REFERENCES usuario(id) ON DELETE CASCADE,
    cpf  VARCHAR(11) NOT NULL UNIQUE
);

CREATE TABLE fornecedor (
    id             BIGINT PRIMARY KEY REFERENCES usuario(id) ON DELETE CASCADE,
    cnpj           VARCHAR(14) NOT NULL UNIQUE,
    area_atuacao   VARCHAR(100) NOT NULL
);

CREATE TABLE agronomo (
    id                     BIGINT PRIMARY KEY REFERENCES usuario(id) ON DELETE CASCADE,
    registro_profissional  VARCHAR(30) NOT NULL UNIQUE,
    especialidade          VARCHAR(100) NOT NULL
);

-- ---------------------------------------------------------------------
-- 4. Propriedade rural (composição 1:N com agricultor)
-- ---------------------------------------------------------------------
CREATE TABLE propriedade_rural (
    id             BIGSERIAL PRIMARY KEY,
    agricultor_id  BIGINT NOT NULL REFERENCES agricultor(id) ON DELETE CASCADE,
    nome           VARCHAR(150) NOT NULL,
    localizacao    VARCHAR(255) NOT NULL,
    area_hectares  NUMERIC(10,2) NOT NULL CHECK (area_hectares > 0)
);

-- ---------------------------------------------------------------------
-- 5. Solicitação de cotação (1:N a partir de propriedade_rural)
-- ---------------------------------------------------------------------
CREATE TABLE solicitacao (
    id                 BIGSERIAL PRIMARY KEY,
    propriedade_id     BIGINT NOT NULL REFERENCES propriedade_rural(id) ON DELETE CASCADE,
    produto_desejado   VARCHAR(150) NOT NULL,
    quantidade         NUMERIC(10,2) NOT NULL CHECK (quantidade > 0),
    data_criacao       TIMESTAMP NOT NULL DEFAULT now(),
    status             VARCHAR(20) NOT NULL DEFAULT 'ABERTA'
                       CHECK (status IN ('ABERTA', 'FINALIZADA', 'CANCELADA'))
);

-- ---------------------------------------------------------------------
-- 6. Cotação (composição 1:N a partir de solicitacao; associada a um
--    fornecedor — é assim que o agricultor compara múltiplas propostas)
-- ---------------------------------------------------------------------
CREATE TABLE cotacao (
    id                    BIGSERIAL PRIMARY KEY,
    solicitacao_id        BIGINT NOT NULL REFERENCES solicitacao(id) ON DELETE CASCADE,
    fornecedor_id         BIGINT NOT NULL REFERENCES fornecedor(id) ON DELETE CASCADE,
    preco                 NUMERIC(12,2) NOT NULL CHECK (preco > 0),
    prazo_entrega         INTEGER NOT NULL CHECK (prazo_entrega > 0),
    condicoes_pagamento   VARCHAR(150) NOT NULL,
    UNIQUE (solicitacao_id, fornecedor_id)
);

-- ---------------------------------------------------------------------
-- 7. Consultoria (1:N a partir de propriedade_rural e de agronomo)
-- ---------------------------------------------------------------------
CREATE TABLE consultoria (
    id                 BIGSERIAL PRIMARY KEY,
    propriedade_id     BIGINT NOT NULL REFERENCES propriedade_rural(id) ON DELETE CASCADE,
    agronomo_id        BIGINT NOT NULL REFERENCES agronomo(id) ON DELETE CASCADE,
    data_atendimento   TIMESTAMP NOT NULL DEFAULT now(),
    diagnostico        TEXT NOT NULL,
    recomendacoes      TEXT NOT NULL
);

-- Índices sobre as chaves estrangeiras mais consultadas
CREATE INDEX idx_propriedade_agricultor ON propriedade_rural(agricultor_id);
CREATE INDEX idx_solicitacao_propriedade ON solicitacao(propriedade_id);
CREATE INDEX idx_cotacao_solicitacao ON cotacao(solicitacao_id);
CREATE INDEX idx_cotacao_fornecedor ON cotacao(fornecedor_id);
CREATE INDEX idx_consultoria_propriedade ON consultoria(propriedade_id);
CREATE INDEX idx_consultoria_agronomo ON consultoria(agronomo_id);

-- =====================================================================
-- 8. INSERTS — dados de exemplo (seed) para demonstrar o modelo
-- =====================================================================

-- 8.1 Usuários (3 agricultores, 3 fornecedores, 2 agrônomos)
INSERT INTO usuario (id, nome_completo, email, senha, telefone, tipo_usuario) VALUES
 (1, 'João Batista Almeida',       'joao.almeida@agrobridge.com',   'hash_senha_1', '(44) 99811-1001', 'AGRICULTOR'),
 (2, 'Maria Aparecida Souza',      'maria.souza@agrobridge.com',    'hash_senha_2', '(44) 99811-1002', 'AGRICULTOR'),
 (3, 'Pedro Henrique Costa',       'pedro.costa@agrobridge.com',    'hash_senha_3', '(44) 99811-1003', 'AGRICULTOR'),
 (4, 'AgroInsumos Maringá Ltda',   'contato@agroinsumosmga.com',    'hash_senha_4', '(44) 3011-2001',  'FORNECEDOR'),
 (5, 'Sementes Norte do Paraná',   'vendas@sementesnortepr.com',    'hash_senha_5', '(44) 3011-2002',  'FORNECEDOR'),
 (6, 'Distribuidora Rural Sul',    'contato@distribuidorarural.com','hash_senha_6', '(44) 3011-2003',  'FORNECEDOR'),
 (7, 'Camila Ribeiro',             'camila.ribeiro@agrobridge.com', 'hash_senha_7', '(44) 99822-3001', 'AGRONOMO'),
 (8, 'Rodrigo Tanaka',             'rodrigo.tanaka@agrobridge.com', 'hash_senha_8', '(44) 99822-3002', 'AGRONOMO');

-- Ajusta a sequência do BIGSERIAL após inserts com id explícito
SELECT setval('usuario_id_seq', (SELECT MAX(id) FROM usuario));

-- 8.2 Agricultores
INSERT INTO agricultor (id, cpf) VALUES
 (1, '11122233344'),
 (2, '22233344455'),
 (3, '33344455566');

-- 8.3 Fornecedores
INSERT INTO fornecedor (id, cnpj, area_atuacao) VALUES
 (4, '11222333000144', 'Fertilizantes e defensivos agrícolas'),
 (5, '22333444000155', 'Sementes certificadas'),
 (6, '33444555000166', 'Insumos agrícolas em geral');

-- 8.4 Agrônomos
INSERT INTO agronomo (id, registro_profissional, especialidade) VALUES
 (7, 'CREA-PR 123456', 'Fertilidade e manejo do solo'),
 (8, 'CREA-PR 654321', 'Manejo integrado de pragas');

-- 8.5 Propriedades rurais (João possui 2 propriedades → composição 1:N)
INSERT INTO propriedade_rural (id, agricultor_id, nome, localizacao, area_hectares) VALUES
 (1, 1, 'Sítio Boa Esperança',    'Maringá - PR',   12.5),
 (2, 2, 'Fazenda Santa Rita',     'Sarandi - PR',   45.0),
 (3, 3, 'Chácara Três Irmãos',    'Marialva - PR',   8.2),
 (4, 1, 'Sítio Água Limpa',       'Iguatemi - PR',  20.0);

SELECT setval('propriedade_rural_id_seq', (SELECT MAX(id) FROM propriedade_rural));

-- 8.6 Solicitações de cotação
INSERT INTO solicitacao (id, propriedade_id, produto_desejado, quantidade, status) VALUES
 (1, 1, 'Adubo NPK 20-05-20',                500.00, 'ABERTA'),
 (2, 2, 'Sementes de soja transgênica',     1200.00, 'ABERTA'),
 (3, 3, 'Calcário dolomítico',              3000.00, 'FINALIZADA'),
 (4, 4, 'Herbicida pós-emergente',            80.00, 'ABERTA');

SELECT setval('solicitacao_id_seq', (SELECT MAX(id) FROM solicitacao));

-- 8.7 Cotações (mais de um fornecedor cotando a mesma solicitação,
--     para demonstrar a comparação de propostas — RF5)
INSERT INTO cotacao (id, solicitacao_id, fornecedor_id, preco, prazo_entrega, condicoes_pagamento) VALUES
 (1, 1, 4, 2450.00,  5, 'À vista com 5% de desconto'),
 (2, 1, 6, 2600.00,  3, '30 dias'),
 (3, 2, 5, 8900.00, 10, '28/56 dias'),
 (4, 2, 4, 9150.00,  7, 'À vista'),
 (5, 3, 6, 1200.00, 15, '30/60 dias'),
 (6, 4, 4, 3400.00,  4, 'À vista');

SELECT setval('cotacao_id_seq', (SELECT MAX(id) FROM cotacao));

-- 8.8 Consultorias técnicas
INSERT INTO consultoria (id, propriedade_id, agronomo_id, diagnostico, recomendacoes) VALUES
 (1, 1, 7, 'Solo com pH baixo (5,2) e baixa disponibilidade de fósforo.',
           'Aplicação de calcário dolomítico e adubação fosfatada antes do plantio.'),
 (2, 2, 8, 'Foco inicial de lagarta-do-cartucho na lavoura de milho.',
           'Monitoramento semanal com armadilhas e controle biológico com Bacillus thuringiensis.'),
 (3, 3, 7, 'Compactação do solo identificada na camada de 15 a 25 cm.',
           'Subsolagem da área e rotação com plantas de cobertura de raiz profunda.'),
 (4, 4, 8, 'Infestação de plantas daninhas de folha larga na lavoura.',
           'Aplicação de herbicida seletivo pós-emergente e ajuste do espaçamento de plantio.');

SELECT setval('consultoria_id_seq', (SELECT MAX(id) FROM consultoria));

COMMIT;

-- =====================================================================
-- 9. Consultas de verificação (opcional — comente antes de rodar em
--    scripts de deploy; úteis para conferir se os dados carregaram bem)
-- =====================================================================
-- Compara todas as cotações recebidas para a solicitação 1, da mais barata para a mais cara:
-- SELECT f.nome_completo AS fornecedor, c.preco, c.prazo_entrega, c.condicoes_pagamento
-- FROM cotacao c JOIN usuario f ON f.id = c.fornecedor_id
-- WHERE c.solicitacao_id = 1 ORDER BY c.preco ASC;

-- Histórico completo (cotações + consultorias) de uma propriedade:
-- SELECT 'COTACAO' AS tipo, s.produto_desejado AS descricao, s.data_criacao AS data
-- FROM solicitacao s WHERE s.propriedade_id = 1
-- UNION ALL
-- SELECT 'CONSULTORIA', co.diagnostico, co.data_atendimento
-- FROM consultoria co WHERE co.propriedade_id = 1
-- ORDER BY data;
