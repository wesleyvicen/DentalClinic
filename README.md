[![N|Sysmei](https://sysmei.com/assets/logo.svg)](https://nodesource.com/products/nsolid)
## _Sistema para Microempreendedor individual_

Sysmei é um sistema de agenda para microempreendedores individuais, podendo ter varios prestadores e cada um com suas agendas.
#### Para quem se enquadra?
- Dentista
- Clinicas
- Estética em Geral

## Tecnologias

- Java
- Spring boot
- Spring Security com JWT
- Upload para S3 da AWS
- Swagger
- RestFul

Atualmente está hospedado na EC2 na Amazon AWS, no link:  [sysmei.com](https://sysmei.com)

## Instalação

Sysmei requer o [java](https://www.oracle.com/br/java/technologies/javase/jdk11-archive-downloads.html) v11+ para rodar.

Utilize uma IDE de sua preferencia, recomendamos [Spring tools Suite](https://spring.io/tools) ou [Intellij](https://www.jetbrains.com/pt-br/idea/) e instale a dependencias do Maven

---

### Rotas da API.

base_url: `http:\\localhost:8080`

Todas as rotas exceto a agenda/public precisa mandar um Authorization
#### Agenda
| Método | URI | Descrição |
| --- | --- | --- |
| GET | /agenda/ | Trás todas as Agendas conforme status - Params: status |
| GET | /agenda/ | Trás todas as Agendas conforme status e login - Params: status= & login= |
| GET | /agenda/prestador | Trás todas as Agendas do prestador - Params: dataInicio= & dataFim= & login= & prestadorId |
| GET | /agenda/public | Trás todas as agendas de usuário - Params: dataInicio= & dataFim= & login= |
| GET | /agenda/soma | Trás a soma das agendas por data - Params: dataInicio= & dataFim= & login= |
| POST | /agenda/ | Cria um novo usuário na API - corpo: { allDay: detalhes: end: login_usuario: paciente_id: pagamento: prestador_id: start: status: title: valor:} |
| PUT | /agenda/{id} | Atualiza a agenda - Corpo: { allDay: detalhes: end: pagamento: start: status: title: valor:} |
| PATCH | /agenda/{id} | Atualiza a agenda - Corpo: { allDay: detalhes: end: pagamento: start: status: title: valor:} |
