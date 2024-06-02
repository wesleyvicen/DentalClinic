# Sysmei

![Sysmei Logo](https://sysmei.com/assets/logo.svg)

Sysmei é um sistema de agenda para microempreendedores individuais (MEI), desenvolvido utilizando tecnologias modernas como Java, Spring Boot, Spring Security com JWT e AWS S3. O projeto está hospedado na AWS EC2.

## Funcionalidades

- Gerenciamento de agendas
- Autenticação e autorização com JWT
- Upload de arquivos para AWS S3

## Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Frameworks:**
  - Spring Boot
  - Spring Security
  - Spring Data JPA
  - Springdoc OpenAPI
- **Banco de Dados:** MySQL
- **Segurança:** JWT (JSON Web Token)
- **Armazenamento de Arquivos:** AWS S3
- **Documentação:** Springdoc OpenAPI
- **Email:** Spring Boot Starter Mail

## Dependências

- **Persistência:** `jakarta.persistence-api`, `spring-boot-starter-data-jpa`
- **Banco de Dados:** `h2`, `mysql-connector-java`
- **AWS:** `aws-java-sdk`, `commons-io`
- **Segurança:** `spring-boot-starter-security`, `jjwt`
- **Utilitários:** `lombok`, `webjars-locator-core`, `classgraph`
- **Documentação:** `springdoc-openapi-ui`, `springdoc-openapi-security`
- **Email:** `spring-boot-starter-mail`

## Pré-requisitos

- Java 17
- IDE (Spring Tools Suite, IntelliJ)

## Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/wesleyvicen/Sysmei.git
   cd Sysmei
   ```

2. Importe o projeto na sua IDE preferida.

3. Configure as variáveis de ambiente para AWS e JWT no arquivo `application.properties`.

4. Execute o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```

## Uso

### Rotas da API

#### Usuário
- `GET /user/{id}` - Obter detalhes de um usuário específico
- `PUT /user/{id}` - Atualizar informações do usuário
- `POST /user` - Criar novo usuário
- `POST /user/reset_password` - Resetar senha do usuário
- `POST /user/picture` - Upload de foto do usuário
- `POST /user/login` - Login do usuário
- `POST /user/forgot_password` - Recuperar senha do usuário
- `GET /user/token` - Obter novo token de acesso

#### Prestador
- `GET /prestador/{id}` - Obter detalhes de um prestador específico
- `PUT /prestador/{id}` - Atualizar informações do prestador
- `DELETE /prestador/{id}` - Deletar prestador
- `GET /prestador` - Listar todos os prestadores
- `POST /prestador` - Criar novo prestador
- `GET /prestador/busca/{telefone}` - Buscar prestador por telefone

#### Paciente
- `GET /paciente/{id}` - Obter detalhes de um paciente específico
- `PUT /paciente/{id}` - Atualizar informações do paciente
- `DELETE /paciente/{id}` - Deletar paciente
- `GET /paciente` - Listar todos os pacientes
- `POST /paciente` - Criar novo paciente
- `POST /paciente/picture/{id}` - Upload de foto do paciente
- `GET /paciente/page` - Listar pacientes com paginação
- `DELETE /paciente/delete` - Deletar múltiplos pacientes

#### Agenda
- `GET /agenda/{id}` - Obter detalhes de uma agenda específica
- `PUT /agenda/{id}` - Atualizar agenda
- `DELETE /agenda/{id}` - Deletar agenda
- `PATCH /agenda/{id}` - Atualizar parcialmente uma agenda
- `GET /agenda` - Listar todas as agendas
- `POST /agenda` - Criar nova agenda
- `GET /agenda/status/{tipo}` - Obter agendas por status
- `GET /agenda/soma` - Somar agendas
- `GET /agenda/public` - Obter agendas públicas
- `GET /agenda/prestador` - Listar agendas por prestador

#### Autenticação
- `POST /auth/refresh_token` - Obter novo token de autenticação

#### Arquivos
- `POST /files/upload` - Upload de arquivos para AWS S3
- `GET /files/{filename}` - Obter arquivo pelo nome
- `DELETE /files/{filename}` - Deletar arquivo pelo nome

Para mais detalhes sobre as rotas, consulte a [documentação Swagger](https://api2.sysmei.com/swagger-ui/index.html).

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Faça commit das suas alterações (`git commit -m 'Adiciona nova feature'`).
4. Envie para o branch (`git push origin feature/nova-feature`).
5. Abra um Pull Request.

## Licença

Este projeto está licenciado sob a Licença MIT - consulte o arquivo [LICENSE](LICENSE) para mais detalhes.
