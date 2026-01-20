# Sistema de Gerenciamento de Farmácia (Backend)

API REST robusta desenvolvida em **Java 21** com **Spring Boot 3.4**, focada em gestão de farmácias. Inclui autenticação JWT, controle de estoque, vendas e auditoria.

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.4** (Web, Data JPA, Security, Validation)
- **PostgreSQL** (Banco de dados)
- **Docker & Docker Compose**
- **JWT (JSON Web Token)**
- **Swagger OpenAPI** (Documentação)
- **Apache Commons CSV** (Exportação)

---

## 🛠️ Instalação e Execução

### Pré-requisitos
- Docker e Docker Compose instalados.
- Java 21 (Opcional se usar Docker).
- Maven (Opcional se usar Docker).

### 🐳 Rodando com Docker (Recomendado)

1. Na raiz do projeto (`java/`), execute:
   ```bash
   docker-compose up --build
   ```
2. A API estará disponível em `http://localhost:8081`.
3. O Banco de Dados PostgreSQL estará na porta `5432`.

### 🖥️ Rodando Localmente (Sem Docker)

> 📖 **Para um guia detalhado e completo, consulte o arquivo [GUIA_EXECUCAO_MANUAL.md](./GUIA_EXECUCAO_MANUAL.md)**

Caso prefira rodar a aplicação e o banco de dados diretamente na sua máquina, siga os passos abaixo:

#### 1. Prepare o Ambiente
- **Java 21 JDK**: Certifique-se de ter o JDK 21 instalado.
  - **Importante**: Verifique se a variável de ambiente `JAVA_HOME` aponta para a instalação do JDK 21. Caso aponte para uma versão anterior (ex: 17), o Maven gerará o erro "release version 21 not supported".
  - **Para corrigir no PowerShell:** `$env:JAVA_HOME = 'C:\Caminho\Para\JDK-21'`
  - **Para corrigir no CMD:** `set JAVA_HOME=C:\Caminho\Para\JDK-21`
- **Maven**: (Opcional) O projeto possui o Maven Wrapper (`mvnw`), mas ter o Maven instalado é útil.
- **PostgreSQL**: Instale e rode o serviço do PostgreSQL na porta padrão 5432.
- **PgAdmin 4**: (Opcional) Para visualizar o banco de dados.

#### 2. Configuração do Banco de Dados
1. Abra o **pgAdmin** ou terminal do Postgres.
2. Crie um banco de dados vazio chamado `farmacia_db`.
3. Verifique as configurações no arquivo `src/main/resources/application.properties`.
   - Por padrão, o usuário é `postgres` e a senha `postgres`.
   - **Caso sua senha local seja diferente (ex: 1104), altere a linha:**
     ```properties
     spring.datasource.password=sua_senha_aqui
     ```

#### 3. Instale as Dependências
Abra o terminal na pasta raiz do projeto (`java/`) e execute:
- **Windows (PowerShell):**
  ```powershell
  ./mvnw clean install -DskipTests
  ```
- **Windows (CMD):**
  ```cmd
  mvnw clean install -DskipTests
  ```
- **Linux/Mac:**
  ```bash
  ./mvnw clean install -DskipTests
  ```

#### 4. Execute a Aplicação
Após a instalação das dependências, inicie a aplicação:
- **Windows (PowerShell):**
  ```powershell
  ./mvnw spring-boot:run
  ```
- **Windows (CMD):**
  ```cmd
  mvnw spring-boot:run
  ```
- **Linux/Mac:**
  ```bash
  ./mvnw spring-boot:run
  ```

A aplicação iniciará na porta `8081` e criará as tabelas automaticamente.

---

## 📄 Documentação da API

Acesse o **Swagger UI** para visualizar e testar os endpoints:
Acesse o **Swagger UI** para visualizar e testar os endpoints:
👉 **[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)**

---

## 🔑 Acesso Inicial (Seed)

O sistema cria automaticamente os seguintes usuários ao iniciar pela primeira vez:

| Role | Email | Senha |
|------|-------|-------|
| **ADMIN** | `admin@farmacia.com` | `admin123` |
| **VENDEDOR** | `vendedor@farmacia.com` | `vendedor123` |

Utilize o endpoint `/auth/login` para obter o Token Bearer.

---

## 🏗️ Estrutura do Projeto

```
com.farmacia.system
├── config       # Configurações (Swagger, CORS, Seeder)
├── controller   # Endpoints da API
├── dto          # Objetos de Transferência de Dados
├── entity       # Entidades JPA
├── exception    # Tratamento global de erros
├── repository   # Interfaces Spring Data JPA
├── security     # Configuração JWT e Spring Security
└── service      # Regras de Negócio
```

## 🧪 Principais Endpoints

### Autenticação
- `POST /auth/login` - Login e obtenção de Token.

### Usuários (Admin)
- `POST /usuarios` - Criar usuário (Admin/Vendedor).
- `GET /usuarios` - Listar usuários.

### Medicamentos & Estoque
- `GET /medicamentos` - Listar medicamentos.
- `POST /medicamentos` - Cadastrar medicamento (Admin).
- `POST /estoque/entrada` - Adicionar estoque.
- `GET /alertas/estoque-baixo` - Relatório de reposição.

### Vendas
- `POST /vendas` - Registrar venda (Baixa estoque automaticamente).

### Logs
- `GET /logs` - Auditoria de ações.
- `GET /logs/export` - Download CSV de logs.
````
