CHEGA

Plataforma multilíngue que ajuda imigrantes a entender seus próximos passos para estabelecer a vida no Brasil.

O CHEGA começa em Chapecó e no Oeste de Santa Catarina, oferecendo orientação simples e confiável sobre documentação, trabalho, saúde, educação e serviços locais.

Projeto em desenvolvimento. O conteúdo disponibilizado pelo CHEGA não substitui orientação jurídica nem o atendimento dos órgãos oficiais.

Objetivo

Reduzir a confusão causada por informações burocráticas dispersas, barreiras linguísticas e dificuldade para encontrar serviços confiáveis.

O produto deverá oferecer:

Jornada personalizada de integração.

Tarefas e prazos importantes.

Conteúdo baseado em fontes verificadas.

Diretório de serviços locais.

Orientação em vários idiomas.

Explicação simples de documentos.

Assistente multilíngue Ask CHEGA.

Público inicial

Imigrantes que vivem ou estão chegando a Chapecó.

Trabalhadores estrangeiros.

Estudantes internacionais.

Refugiados.

Pessoas em reunificação familiar.

Idiomas planejados para a primeira fase:

Português brasileiro (PT_BR).

Kreyòl ayisyen (HT).

Español (ES).

Français (FR).

English (EN).

Tecnologias

Backend

Java 21.

Spring Boot 4.

Spring Web MVC.

Bean Validation.

Spring Data JPA.

Hibernate.

BCrypt.

PostgreSQL 16.

Flyway.

Maven Wrapper.

Infraestrutura

Docker.

Docker Compose.

Git e GitHub.

Frontend

React.

TypeScript.

Interface responsiva e mobile-first.

O frontend ainda será iniciado.

Funcionalidades implementadas

Endpoint de verificação da API.

Cadastro de usuários.

Validação dos dados recebidos.

Normalização de nome e e-mail.

Proteção contra e-mails duplicados.

Hash de senha com BCrypt.

Persistência no PostgreSQL.

Versionamento do banco com Flyway.

Tratamento padronizado de erros.

Respostas HTTP 201, 400 e 409.

- Login com e-mail e senha.
- Geração de JWT assinado.
- Validação de assinatura e expiração do token.
- Autenticação stateless com Spring Security.
- Endpoint protegido para consultar o usuário autenticado.

- Testes unitários do cadastro de usuário.
- Testes unitários da autenticação.
- Testes de geração, expiração e adulteração de JWT.
- Testes do filtro de autenticação JWT.

Roadmap

-[---] Fundação do backend.

- [x] PostgreSQL com Docker Compose.

- [x] Flyway e primeira migration.

- [x] Módulo e cadastro de usuário.

- [x] Testes automatizados do cadastro.

- [x] Login e autenticação JWT.

- [x] Perfil migratório.

Jornada personalizada.

Tarefas e prazos.

Serviços locais.

Internacionalização do frontend.

Ask CHEGA com fontes verificadas.

Explicação segura de documentos.

Beta fechado em Chapecó.

Privacidade e responsabilidade

O CHEGA deverá seguir os princípios da LGPD:

Coletar somente os dados necessários.

Informar claramente a finalidade de cada dado.

Solicitar consentimento quando aplicável.

Permitir exclusão da conta e dos dados.

Proteger documentos e informações pessoais.

Manter fontes oficiais e datas de verificação.

Não apresentar respostas de IA como parecer jurídico.

Autor

Desenvolvido por Obenson Joffre.

Status

Projeto em desenvolvimento para aprendizagem, validação e construção do MVP do CHEGA.