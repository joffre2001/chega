ALTER TABLE document_requirements
    ADD COLUMN official_source_url VARCHAR(500);

INSERT INTO document_requirements (
    code,
    title,
    description,
    migration_situation,
    required,
    display_order,
    active,
    official_source_url
)
VALUES
(
    'IDENTIFICATION_DOCUMENT',
    'Documento de identificação',
    'Separe um documento de identificação válido utilizado nos seus procedimentos.',
    NULL,
    TRUE,
    10,
    TRUE,
    NULL
),
(
    'CPF',
    'Cadastro de Pessoa Física (CPF)',
    'Confira sua inscrição ou situação cadastral no CPF e consulte os requisitos oficiais.',
    NULL,
    FALSE,
    20,
    TRUE,
    'https://servicos.receita.fazenda.gov.br/servicos/cpf/inscricaocpfestrangeiro/default.asp'
),
(
    'CRNM_OR_PROTOCOL',
    'CRNM ou protocolo de registro migratório',
    'Verifique se o seu procedimento exige registro migratório, protocolo ou CRNM.',
    NULL,
    FALSE,
    30,
    TRUE,
    'https://www.gov.br/pt-br/servicos/registrar-se-como-estrangeiro-no-brasil'
),
(
    'PROOF_OF_ADDRESS',
    'Comprovante de endereço',
    'Separe um comprovante de endereço quando ele for solicitado pelo serviço utilizado.',
    NULL,
    FALSE,
    40,
    TRUE,
    NULL
)
ON CONFLICT (code) DO NOTHING;