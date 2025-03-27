# Documentação da Aplicação de Gerenciamento de Restaurante

## Sumário
1. [Introdução ao Projeto](#introdução-ao-projeto)
2. [Arquitetura do Sistema](#arquitetura-do-sistema)
   - [Arquitetura Limpa](#arquitetura-limpa)
   - [Responsabilidades das Camadas](#responsabilidades-das-camadas)
   - [Estrutura de Pacotes](#estrutura-de-pacotes)
3. [Modelo de Domínio](#modelo-de-domínio)
   - [Entidades Principais](#entidades-principais)
   - [Relacionamentos entre Entidades](#relacionamentos-entre-entidades)
4. [Stack Tecnológica](#stack-tecnológica)
5. [Documentação da API](#documentação-da-api)
   - [API de Gerenciamento de Usuários](#api-de-gerenciamento-de-usuários)
   - [API de Gerenciamento de Tipos de Usuário](#api-de-gerenciamento-de-tipos-de-usuário)
   - [API de Autenticação](#api-de-autenticação)
   - [API de Gerenciamento de Restaurantes](#api-de-gerenciamento-de-restaurantes)
   - [API de Gerenciamento de Itens de Menu](#api-de-gerenciamento-de-itens-de-menu)
6. [Configuração e Instalação](#configuração-e-instalação)
   - [Pré-requisitos](#pré-requisitos)
   - [Configuração do Ambiente](#configuração-do-ambiente)
   - [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
   - [Implantação com Docker](#implantação-com-docker)
7. [Estratégia de Testes](#estratégia-de-testes)
   - [Testes Unitários](#testes-unitários)
   - [Testes de Integração](#testes-de-integração)
8. [Detalhes das Funcionalidades](#detalhes-das-funcionalidades)
   - [Gerenciamento de Usuários](#gerenciamento-de-usuários)
   - [Gerenciamento de Restaurantes](#gerenciamento-de-restaurantes)
   - [Gerenciamento de Menus](#gerenciamento-de-menus)
   - [Tratamento de Exceções](#tratamento-de-exceções)
9. [Considerações de Segurança](#considerações-de-segurança)
10. [Melhorias Futuras](#melhorias-futuras)

## Introdução ao Projeto

A Aplicação de Gerenciamento de Restaurante é um sistema backend abrangente projetado para operações de restaurantes. Construída com uma stack tecnológica moderna e princípios de arquitetura limpa, ela fornece uma base escalável para que negócios de restaurantes gerenciem sua presença digital.

A aplicação permite que proprietários de restaurantes registrem seus estabelecimentos, gerenciem cardápios, e proporciona aos clientes a capacidade de navegar por restaurantes e visualizar itens do menu. O sistema apresenta controle de acesso baseado em funções através de tipos de usuário (CLIENTE, PROPRIETÁRIO), facilitando permissões apropriadas para diferentes categorias de usuários.

Este documento fornece documentação técnica detalhada cobrindo a arquitetura, endpoints da API, instruções de configuração e descrições de funcionalidades da Aplicação de Gerenciamento de Restaurante.

## Arquitetura do Sistema

### Arquitetura Limpa

A aplicação segue o padrão de Arquitetura Limpa proposto por Robert C. Martin, organizando o código em camadas concêntricas com o domínio no centro. Esta abordagem garante:

- **Independência de frameworks**: A lógica de negócio central não depende de frameworks externos.
- **Testabilidade**: Regras de negócio podem ser testadas sem UI, banco de dados ou quaisquer elementos externos.
- **Independência da UI**: A UI pode mudar sem afetar o resto do sistema.
- **Independência do banco de dados**: O banco de dados pode ser alterado sem afetar o resto do sistema.
- **Independência de agências externas**: As regras de negócio não conhecem o mundo exterior.

### Responsabilidades das Camadas

A aplicação está organizada nas seguintes camadas:

1. **Camada Core**
   - **Domínio**: Contém entidades de negócio que representam conceitos e regras de negócio centrais.
   - **Casos de Uso**: Implementa regras de negócio específicas e lógica da aplicação.
   - **Gateways (Interfaces)**: Define contratos para acesso a dados e comunicações externas.
   - **Exceções**: Classes de exceção específicas de negócio.

2. **Camada de Adaptadores**
   - **Web**: Controladores REST que lidam com requisições e respostas HTTP.
   - **Banco de Dados**: Entidades JPA, repositórios e implementações de gateway.
   - **Presenters**: Transformam dados entre os casos de uso e o formato externo.

3. **Camada de Configuração**
   - **Configuração de Segurança**: Configuração de autenticação e autorização.
   - **Configuração de Beans**: Conexão de componentes da aplicação.
   - **Tratamento de Exceções**: Tratamento global de erros para respostas consistentes.

### Estrutura de Pacotes

A aplicação segue uma estrutura de pacotes bem definida:

```
src/
├── main/
│   ├── java/fiap/restaurant/app/
│   │   ├── adapter/
│   │   │   ├── database/jpa/
│   │   │   │   ├── entity/
│   │   │   │   ├── repository/
│   │   │   │   └── gateway/
│   │   │   ├── presenter/impl/
│   │   │   └── web/
│   │   │       ├── json/
│   │   │       └── *ApiController.java
│   │   ├── configuration/
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── core/
│   │   │   ├── domain/
│   │   │   ├── exception/
│   │   │   ├── gateway/
│   │   │   └── usecase/
│   │   └── AppApplication.java
```

## Modelo de Domínio

### Entidades Principais

A aplicação é centrada nestas entidades de domínio principais:

1. **Usuário**: Representa usuários do sistema (clientes, proprietários de restaurantes)
2. **TipoUsuario**: Define os papéis de usuário (CLIENTE, PROPRIETÁRIO)
3. **Restaurante**: Representa um estabelecimento de restaurante
4. **ItemMenu**: Representa itens no cardápio de um restaurante
5. **Endereco**: Representa informações de localização física
6. **HorarioFuncionamento**: Representa quando um restaurante está aberto para negócios
7. **TipoCulinaria**: Representa tipos de culinária (Italiana, Japonesa, etc.)

### Relacionamentos entre Entidades

As entidades de domínio se relacionam entre si das seguintes maneiras:

- Um **Usuário** tem um **TipoUsuario** (CLIENTE, PROPRIETÁRIO)
- Um **Usuário** (com tipo PROPRIETÁRIO) pode possuir múltiplos **Restaurantes**
- Um **Restaurante** tem um **Endereco**
- Um **Restaurante** tem uma configuração de **HorarioFuncionamento**
- Um **Restaurante** tem um **TipoCulinaria**
- Um **Restaurante** pode ter múltiplos **ItensMenu**

## Stack Tecnológica

A aplicação utiliza uma stack tecnológica moderna:

- **Java 21**: Versão LTS mais recente do Java
- **Spring Boot 3.3.x**: Framework de aplicação moderna
- **Spring Security**: Autenticação e autorização
- **Spring Data JPA**: Camada de acesso a dados
- **Hibernate**: ORM para interação com banco de dados
- **PostgreSQL**: Banco de dados de produção primário
- **H2 Database**: Banco de dados em memória para testes
- **Flyway**: Migração e versionamento de banco de dados
- **Maven**: Gerenciamento de build e dependências do projeto
- **JUnit 5**: Framework de testes unitários e de integração
- **Mockito**: Framework de mocking para testes
- **Lombok**: Reduz código boilerplate
- **Swagger/OpenAPI**: Documentação da API
- **BCrypt**: Criptografia de senha
- **Docker & Docker Compose**: Containerização e orquestração

## Documentação da API

A API REST é organizada em torno de recursos e utiliza métodos HTTP padrão. Todas as respostas estão no formato JSON.

### URL Base

Todas as URLs da API referenciadas na documentação têm a seguinte base:

```
http://localhost:8080/api/v1
```

### API de Gerenciamento de Usuários

#### Criar Usuário

- **Endpoint**: `POST /users`
- **Descrição**: Criar um novo usuário
- **Corpo da Requisição**:
  ```json
  {
    "name": "João Silva",
    "login": "joaosilva",
    "email": "joao.silva@exemplo.com",
    "password": "senhasegura",
    "userTypeId": "UUID_DO_TIPO_USUARIO",
    "address": {
      "street": "Rua Principal, 123",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01001-000",
      "country": "Brasil"
    }
  }
  ```
- **Resposta**: `201 Created` com os dados do usuário criado

#### Obter Todos os Usuários

- **Endpoint**: `GET /users`
- **Descrição**: Recuperar todos os usuários
- **Resposta**: `200 OK` com array de usuários

#### Obter Usuário por ID

- **Endpoint**: `GET /users/{id}`
- **Descrição**: Recuperar um usuário específico por ID
- **Parâmetros de Caminho**: `id` - UUID do usuário
- **Resposta**: `200 OK` com dados do usuário ou `404 Not Found`

#### Atualizar Usuário

- **Endpoint**: `PUT /users/{id}`
- **Descrição**: Atualizar um usuário existente
- **Parâmetros de Caminho**: `id` - UUID do usuário
- **Corpo da Requisição**: Mesmo que criar usuário
- **Resposta**: `200 OK` com dados do usuário atualizados ou `404 Not Found`

#### Excluir Usuário

- **Endpoint**: `DELETE /users/{id}`
- **Descrição**: Excluir um usuário
- **Parâmetros de Caminho**: `id` - UUID do usuário
- **Resposta**: `204 No Content` ou `404 Not Found`

### API de Gerenciamento de Tipos de Usuário

#### Criar Tipo de Usuário

- **Endpoint**: `POST /user-types`
- **Descrição**: Criar um novo tipo de usuário
- **Corpo da Requisição**:
  ```json
  {
    "name": "GERENTE"
  }
  ```
- **Resposta**: `201 Created` com os dados do tipo de usuário criado

#### Obter Todos os Tipos de Usuário

- **Endpoint**: `GET /user-types`
- **Descrição**: Recuperar todos os tipos de usuário
- **Resposta**: `200 OK` com array de tipos de usuário

#### Obter Tipo de Usuário por ID

- **Endpoint**: `GET /user-types/{id}`
- **Descrição**: Recuperar um tipo de usuário específico por ID
- **Parâmetros de Caminho**: `id` - UUID do tipo de usuário
- **Resposta**: `200 OK` com dados do tipo de usuário ou `404 Not Found`

#### Obter Tipo de Usuário por Nome

- **Endpoint**: `GET /user-types/name/{name}`
- **Descrição**: Recuperar um tipo de usuário específico por nome
- **Parâmetros de Caminho**: `name` - Nome do tipo de usuário (ex: "CLIENTE")
- **Resposta**: `200 OK` com dados do tipo de usuário ou `404 Not Found`

#### Atualizar Tipo de Usuário

- **Endpoint**: `PUT /user-types/{id}`
- **Descrição**: Atualizar um tipo de usuário existente
- **Parâmetros de Caminho**: `id` - UUID do tipo de usuário
- **Corpo da Requisição**: Mesmo que criar tipo de usuário
- **Resposta**: `200 OK` com dados do tipo de usuário atualizados ou `404 Not Found`

#### Excluir Tipo de Usuário

- **Endpoint**: `DELETE /user-types/{id}`
- **Descrição**: Excluir um tipo de usuário
- **Parâmetros de Caminho**: `id` - UUID do tipo de usuário
- **Resposta**: `204 No Content` ou `400 Bad Request` se for um tipo padrão do sistema

### API de Autenticação

#### Login de Usuário

- **Endpoint**: `POST /users/login`
- **Descrição**: Autenticar um usuário
- **Corpo da Requisição**:
  ```json
  {
    "login": "joaosilva",
    "password": "senhasegura"
  }
  ```
- **Resposta**: `200 OK` com dados do usuário ou `401 Unauthorized`

#### Atualizar Senha

- **Endpoint**: `POST /users/password`
- **Descrição**: Atualizar a senha de um usuário
- **Corpo da Requisição**:
  ```json
  {
    "login": "joaosilva",
    "currentPassword": "senhaantiga",
    "newPassword": "senhanova"
  }
  ```
- **Resposta**: `200 OK` ou `400 Bad Request` se as senhas não coincidirem

### API de Gerenciamento de Restaurantes

#### Criar Restaurante

- **Endpoint**: `POST /restaurants`
- **Descrição**: Criar um novo restaurante
- **Corpo da Requisição**:
  ```json
  {
    "name": "Delícia Italiana",
    "cuisineType": "ITALIANA",
    "ownerId": "UUID_DO_PROPRIETARIO",
    "address": {
      "street": "Avenida Paulista, 456",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01310-000",
      "country": "Brasil"
    },
    "businessHours": {
      "openingTime": "11:00",
      "closingTime": "22:00"
    }
  }
  ```
- **Resposta**: `201 Created` com os dados do restaurante criado

#### Obter Todos os Restaurantes

- **Endpoint**: `GET /restaurants`
- **Descrição**: Recuperar todos os restaurantes
- **Resposta**: `200 OK` com array de restaurantes

#### Obter Restaurante por ID

- **Endpoint**: `GET /restaurants/{id}`
- **Descrição**: Recuperar um restaurante específico por ID
- **Parâmetros de Caminho**: `id` - UUID do restaurante
- **Resposta**: `200 OK` com dados do restaurante ou `404 Not Found`

#### Obter Restaurantes por ID do Proprietário

- **Endpoint**: `GET /restaurants/owner/{ownerId}`
- **Descrição**: Recuperar todos os restaurantes pertencentes a um usuário específico
- **Parâmetros de Caminho**: `ownerId` - UUID do proprietário
- **Resposta**: `200 OK` com array de restaurantes

#### Encontrar Restaurantes por Nome

- **Endpoint**: `GET /restaurants/name/{name}`
- **Descrição**: Encontrar restaurantes por nome (correspondência parcial)
- **Parâmetros de Caminho**: `name` - Nome ou parte do nome
- **Resposta**: `200 OK` com array de restaurantes correspondentes

#### Encontrar Restaurantes por Tipo de Culinária

- **Endpoint**: `GET /restaurants/cuisine/{cuisineType}`
- **Descrição**: Encontrar restaurantes por tipo de culinária
- **Parâmetros de Caminho**: `cuisineType` - Tipo de culinária (ex: "ITALIANA")
- **Resposta**: `200 OK` com array de restaurantes correspondentes

#### Pesquisar Restaurantes

- **Endpoint**: `GET /restaurants/search`
- **Descrição**: Pesquisar restaurantes por nome ou tipo de culinária
- **Parâmetros de Consulta**: 
  - `name` (opcional) - Nome a ser pesquisado
  - `cuisineType` (opcional) - Tipo de culinária a ser pesquisado
- **Resposta**: `200 OK` com array de restaurantes correspondentes

#### Atualizar Restaurante

- **Endpoint**: `PUT /restaurants/{id}`
- **Descrição**: Atualizar um restaurante existente
- **Parâmetros de Caminho**: `id` - UUID do restaurante
- **Corpo da Requisição**: Mesmo que criar restaurante
- **Resposta**: `200 OK` com dados do restaurante atualizados ou `404 Not Found`

#### Excluir Restaurante

- **Endpoint**: `DELETE /restaurants/{id}`
- **Descrição**: Excluir um restaurante
- **Parâmetros de Caminho**: `id` - UUID do restaurante
- **Resposta**: `204 No Content` ou `404 Not Found`

### API de Gerenciamento de Itens de Menu

#### Criar Item de Menu

- **Endpoint**: `POST /restaurants/{restaurantId}/menu-items`
- **Descrição**: Criar um novo item de menu para um restaurante
- **Parâmetros de Caminho**: `restaurantId` - UUID do restaurante
- **Corpo da Requisição**:
  ```json
  {
    "name": "Pizza Margherita",
    "description": "Pizza clássica com molho de tomate, muçarela e manjericão",
    "price": 45.90,
    "imageUrl": "https://exemplo.com/imagens/pizza.jpg"
  }
  ```
- **Resposta**: `201 Created` com os dados do item de menu criado

#### Obter Todos os Itens de Menu de um Restaurante

- **Endpoint**: `GET /restaurants/{restaurantId}/menu-items`
- **Descrição**: Recuperar todos os itens de menu de um restaurante específico
- **Parâmetros de Caminho**: `restaurantId` - UUID do restaurante
- **Resposta**: `200 OK` com array de itens de menu

#### Obter Item de Menu por ID

- **Endpoint**: `GET /restaurants/{restaurantId}/menu-items/{id}`
- **Descrição**: Recuperar um item de menu específico por ID
- **Parâmetros de Caminho**: 
  - `restaurantId` - UUID do restaurante
  - `id` - UUID do item de menu
- **Resposta**: `200 OK` com dados do item de menu ou `404 Not Found`

#### Atualizar Item de Menu

- **Endpoint**: `PUT /restaurants/{restaurantId}/menu-items/{id}`
- **Descrição**: Atualizar um item de menu existente
- **Parâmetros de Caminho**: 
  - `restaurantId` - UUID do restaurante
  - `id` - UUID do item de menu
- **Corpo da Requisição**: Mesmo que criar item de menu
- **Resposta**: `200 OK` com dados do item de menu atualizados ou `404 Not Found`

#### Excluir Item de Menu

- **Endpoint**: `DELETE /restaurants/{restaurantId}/menu-items/{id}`
- **Descrição**: Excluir um item de menu
- **Parâmetros de Caminho**: 
  - `restaurantId` - UUID do restaurante
  - `id` - UUID do item de menu
- **Resposta**: `204 No Content` ou `404 Not Found`

## Configuração e Instalação

### Pré-requisitos

Para configurar e executar a aplicação, você precisa de:

- Java 21 ou superior
- Maven 3.8.x ou superior
- PostgreSQL 14.x ou superior
- Docker e Docker Compose (opcional, para implantação containerizada)

### Configuração do Ambiente

#### Opção 1: Executando com Docker Compose (Recomendado)

1. Clone o repositório:
   ```bash
   git clone https://github.com/ribeirovillar/fiap-restaurant-management-app.git
   cd fiap-restaurant-management-app
   ```

2. Inicie a pilha da aplicação com Docker Compose:
   ```bash
   docker compose up -d
   ```

   Isso irá:
   - Construir a aplicação a partir do código-fonte
   - Iniciar o PostgreSQL em um container
   - Iniciar a aplicação em um container
   - Conectar a aplicação ao banco de dados

3. Acesse a aplicação:
   - API: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html

4. Pare a aplicação:
   ```bash
   docker compose down
   ```

#### Opção 2: Configuração Manual

1. Clone o repositório:
   ```bash
   git clone https://github.com/ribeirovillar/fiap-restaurant-management-app.git
   cd fiap-restaurant-management-app
   ```

2. Construa a aplicação:
   ```bash
   mvn clean install
   ```

3. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

4. Acesse a aplicação:
   - API: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html

### Configuração do Banco de Dados

#### Configuração do PostgreSQL

1. Crie um banco de dados PostgreSQL:
   ```sql
   CREATE DATABASE fiap_food;
   ```

2. Configure a conexão com o banco de dados em `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/fiap_food
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.datasource.driver-class-name=org.postgresql.Driver
   
   spring.jpa.hibernate.ddl-auto=none
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.show-sql=true
   ```

### Implantação com Docker

A aplicação inclui configuração Docker para implantação containerizada:

- **Dockerfile**: Define a construção do container da aplicação
- **docker-compose.yml**: Orquestra ambos os containers da aplicação e do banco de dados

Configuração no `docker-compose.yml`:
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/fiap_food
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
  
  postgres:
    image: postgres:14-alpine
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: fiap_food
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

## Estratégia de Testes

A aplicação inclui uma estratégia de testes abrangente:

### Testes Unitários

Os testes unitários focam em testar componentes individuais isoladamente:

- Lógica de validação de entidades de domínio
- Implementações de casos de uso
- Regras de negócio

Execute testes unitários:
```bash
mvn test
```

### Testes de Integração

Os testes de integração verificam a interação correta entre componentes:

- Controladores de API
- Operações de banco de dados
- Fluxos end-to-end

Execute testes de integração:
```bash
mvn test -Dtest="*IntegrationTest"
```

Execute uma classe de teste específica:
```bash
mvn test -Dtest="UserTypeTest"
```

## Detalhes das Funcionalidades

### Gerenciamento de Usuários

O módulo de gerenciamento de usuários fornece funcionalidade para gerenciar usuários:

- **Registro de Usuário**: Crie novos usuários com diferentes funções
- **Tipos de Usuário**: O sistema suporta funções de CLIENTE e PROPRIETÁRIO com diferentes permissões
- **Gerenciamento de Perfil**: Atualize informações de perfil do usuário
- **Gerenciamento de Conta**: Exclua contas de usuário

Detalhes-chave de implementação:
- Senhas são codificadas usando BCrypt para segurança
- E-mails de usuário devem ser únicos
- Logins de usuário devem ser únicos
- Cada usuário pode ter um endereço físico

### Gerenciamento de Restaurantes

O módulo de gerenciamento de restaurantes lida com informações de restaurantes:

- **Registro de Restaurante**: Proprietários podem registrar novos restaurantes
- **Informações de Restaurante**: Armazene e atualize detalhes do restaurante
- **Funcionalidade de Busca**: Encontre restaurantes por nome, tipo de culinária
- **Associação de Proprietário**: Conecte restaurantes aos seus proprietários

Detalhes-chave de implementação:
- Cada restaurante tem um endereço físico
- Restaurantes estão associados a um tipo de culinária
- Horários de funcionamento definem quando um restaurante está aberto
- Apenas proprietários podem criar e gerenciar restaurantes

### Gerenciamento de Menus

O módulo de gerenciamento de menu lida com itens de menu de restaurantes:

- **Criação de Menu**: Adicione itens de menu aos restaurantes
- **Modificação de Menu**: Atualize detalhes dos itens de menu
- **Organização de Menu**: Agrupe itens de menu por restaurante

Detalhes-chave de implementação:
- Itens de menu incluem nome, descrição, preço
- URL de imagem opcional para representação visual
- Itens de menu estão sempre associados a um restaurante específico

### Tratamento de Exceções

A aplicação inclui tratamento robusto de exceções para validação de dados e regras de negócio:

- **BusinessException**: Exceção personalizada para violações de regras de negócio
- **GlobalExceptionHandler**: Tratamento centralizado de exceções
- **Códigos de Status HTTP**: Códigos de status apropriados para diferentes tipos de erro

Exemplo de resposta de erro:
```json
{
  "timestamp": "2023-03-10T14:32:45.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Tipo de usuário com nome GERENTE já existe",
  "path": "/api/v1/user-types"
}
```

## Considerações de Segurança

A aplicação implementa várias medidas de segurança:

1. **Manipulação de Senhas**
   - Senhas nunca são armazenadas em texto simples
   - Algoritmo BCrypt usado para hash seguro de senhas
   - Validação de senha durante o login

2. **Autorização**
   - Diferentes tipos de usuário têm diferentes níveis de acesso
   - Usuários PROPRIETÁRIO só podem gerenciar seus próprios restaurantes
   - Verificações de validação para permissões apropriadas

3. **Validação de Entrada**
   - Todos os dados de entrada são validados antes do processamento
   - Proteção contra entrada de dados inválidos

## Melhorias Futuras

Potenciais melhorias futuras para o sistema:

1. **Autenticação JWT**
   - Implementar autenticação baseada em token
   - Suporte para tokens de atualização

2. **Gerenciamento de Pedidos**
   - Permitir que clientes façam pedidos
   - Acompanhar status do pedido
   - Histórico de pedidos

3. **Integração de Pagamento**
   - Integrar com gateways de pagamento
   - Suportar múltiplos métodos de pagamento

4. **Sistema de Avaliação**
   - Permitir que clientes avaliem restaurantes
   - Avaliações de restaurantes

5. **Sistema de Notificação**
   - Notificações por e-mail
   - Notificações por SMS
   - Notificações push para aplicativos móveis 