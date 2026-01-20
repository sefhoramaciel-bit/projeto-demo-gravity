# Guia de Execução Manual - Sistema de Farmácia

Este guia detalha como executar a aplicação **sem usar Docker**, diretamente na sua máquina.

## 📋 Pré-requisitos

### 1. Java Development Kit (JDK) 21
- **Download**: [Eclipse Adoptium JDK 21](https://adoptium.net/) ou [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- **Verificação**: Abra o terminal e execute:
  ```cmd
  java -version
  ```
  Deve mostrar a versão 21.x.x

### 2. Maven (Opcional - o projeto inclui Maven Wrapper)
- **Download**: [Apache Maven](https://maven.apache.org/download.cgi)
- **Verificação**: 
  ```cmd
  mvn -version
  ```
- **Nota**: O projeto possui `mvnw` (Maven Wrapper), então o Maven não é obrigatório.

### 3. PostgreSQL
- **Download**: [PostgreSQL](https://www.postgresql.org/download/windows/)
- Instale e configure o PostgreSQL na porta padrão **5432**
- **Importante**: Anote a senha do usuário `postgres` que você configurou durante a instalação

### 4. PgAdmin 4 (Opcional - para gerenciar o banco)
- **Download**: [PgAdmin](https://www.pgadmin.org/download/)

---

## 🔧 Configuração do Ambiente

### 1. Configurar JAVA_HOME

**Windows (CMD):**
```cmd
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot
```
*(Ajuste o caminho conforme sua instalação)*

**Windows (PowerShell):**
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"
```

**Para tornar permanente:**
1. Abra o **Painel de Controle** → **Sistema** → **Configurações avançadas do sistema**
2. Clique em **Variáveis de Ambiente**
3. Em **Variáveis do sistema**, clique em **Novo** (ou edite se já existir)
4. Nome: `JAVA_HOME`
5. Valor: `C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot` (ajuste conforme necessário)
6. Clique em **OK** e feche todas as janelas
7. **IMPORTANTE**: Feche e reabra o terminal para aplicar as mudanças

**Verificação:**
```cmd
echo %JAVA_HOME%
```

### 2. Configurar o Banco de Dados PostgreSQL

#### Passo 1: Criar o Banco de Dados
Abra o **PgAdmin** ou use o terminal do PostgreSQL:

**Via PgAdmin:**
1. Conecte-se ao servidor PostgreSQL
2. Clique com o botão direito em **Databases**
3. Selecione **Create** → **Database**
4. Nome: `farmacia_db`
5. Clique em **Save**

**Via Terminal (psql):**
```sql
CREATE DATABASE farmacia_db;
```

#### Passo 2: Configurar a Senha no application.properties
Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/farmacia_db
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA_AQUI  # ← Altere para sua senha do PostgreSQL
```

**Nota**: Se você não alterou a senha padrão durante a instalação, pode ser `postgres` ou `1104` (conforme o docker-compose.yml).

---

## 🚀 Executando a Aplicação

### Opção 1: Usando Maven Wrapper (Recomendado)

**Windows (CMD):**
```cmd
cd C:\Projetos\Projeto-DPSP\projeto-demo-gravity\java
mvnw.cmd clean install -DskipTests
mvnw.cmd spring-boot:run
```

**Windows (PowerShell):**
```powershell
cd C:\Projetos\Projeto-DPSP\projeto-demo-gravity\java
.\mvnw clean install -DskipTests
.\mvnw spring-boot:run
```

**Linux/Mac:**
```bash
cd java
./mvnw clean install -DskipTests
./mvnw spring-boot:run
```

### Opção 2: Usando Maven Instalado

Se você tem o Maven instalado globalmente:

```cmd
cd C:\Projetos\Projeto-DPSP\projeto-demo-gravity\java
mvn clean install -DskipTests
mvn spring-boot:run
```

### Opção 3: Executar o JAR Compilado

```cmd
cd C:\Projetos\Projeto-DPSP\projeto-demo-gravity\java
mvnw.cmd clean package -DskipTests
java -jar target/farmacia-system-0.0.1-SNAPSHOT.jar
```

---

## ✅ Verificação

Após iniciar a aplicação, você deve ver mensagens como:

```
Started FarmaciaApplication in X.XXX seconds
Users Seeded: admin@farmacia.com / vendedor@farmacia.com
Products Seeded
```

A aplicação estará disponível em:
- **API**: http://localhost:8081
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **API Docs**: http://localhost:8081/api-docs

---

## 🔑 Credenciais Padrão

O sistema cria automaticamente os seguintes usuários na primeira execução:

| Role | Email | Senha |
|------|-------|-------|
| **ADMIN** | `admin@farmacia.com` | `admin123` |
| **VENDEDOR** | `vendedor@farmacia.com` | `vendedor123` |

### Testando o Login

Use o endpoint `/auth/login` para obter o token JWT:

**Request:**
```json
POST http://localhost:8081/auth/login
Content-Type: application/json

{
  "email": "admin@farmacia.com",
  "senha": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

Use este token no header `Authorization: Bearer <token>` para acessar os endpoints protegidos.

---

## 🐛 Solução de Problemas

### Erro: "release version 21 not supported"
**Causa**: JAVA_HOME aponta para uma versão antiga do Java.

**Solução**:
1. Verifique a versão: `java -version`
2. Configure JAVA_HOME para o JDK 21 (veja seção "Configurar JAVA_HOME" acima)
3. Feche e reabra o terminal

### Erro: "Connection refused" ou "FATAL: password authentication failed"
**Causa**: PostgreSQL não está rodando ou senha incorreta.

**Solução**:
1. Verifique se o serviço PostgreSQL está rodando:
   - Windows: Abra **Serviços** (services.msc) e procure por "PostgreSQL"
2. Verifique a senha no `application.properties`
3. Teste a conexão manualmente no PgAdmin

### Erro: "database 'farmacia_db' does not exist"
**Causa**: O banco de dados não foi criado.

**Solução**: Crie o banco de dados conforme instruções na seção "Configurar o Banco de Dados PostgreSQL"

### Erro: "Port 8081 already in use"
**Causa**: Outra aplicação está usando a porta 8081.

**Solução**:
1. Altere a porta no `application.properties`:
   ```properties
   server.port=8082
   ```
2. Ou encerre o processo que está usando a porta 8081

### Erro: "The JAVA_HOME environment variable is not defined correctly"
**Causa**: JAVA_HOME não está configurado ou aponta para um caminho inválido.

**Solução**:
1. Verifique se o caminho existe: `dir "%JAVA_HOME%"`
2. Configure JAVA_HOME corretamente (veja seção "Configurar JAVA_HOME" acima)
3. Feche e reabra o terminal

---

## 📝 Notas Importantes

1. **Primeira Execução**: O Hibernate criará automaticamente as tabelas no banco de dados (devido a `spring.jpa.hibernate.ddl-auto=update`).

2. **Dados de Seed**: Os usuários e medicamentos iniciais são criados automaticamente na primeira execução pelo `DataSeeder`.

3. **Logs SQL**: O SQL gerado pelo Hibernate é exibido no console (devido a `spring.jpa.show-sql=true`). Isso pode ser desabilitado em produção.

4. **CORS**: A aplicação está configurada para aceitar requisições de `http://localhost:3000` e `http://localhost:5173` (portas comuns do React/Vite).

---

## 🎯 Próximos Passos

Após a aplicação estar rodando:

1. Acesse o **Swagger UI**: http://localhost:8081/swagger-ui.html
2. Faça login usando `/auth/login`
3. Copie o token retornado
4. Clique em **Authorize** no Swagger e cole o token
5. Teste os endpoints disponíveis

---

## 📚 Estrutura do Projeto

```
java/
├── src/
│   └── main/
│       ├── java/com/farmacia/system/
│       │   ├── config/          # Configurações (Swagger, CORS, Seeder)
│       │   ├── controller/      # Endpoints REST
│       │   ├── dto/             # Data Transfer Objects
│       │   ├── entity/          # Entidades JPA
│       │   ├── exception/       # Tratamento de erros
│       │   ├── repository/      # Repositórios Spring Data
│       │   ├── security/        # JWT e Spring Security
│       │   └── service/         # Lógica de negócio
│       └── resources/
│           └── application.properties
├── pom.xml                      # Dependências Maven
├── mvnw                         # Maven Wrapper (Linux/Mac)
└── mvnw.cmd                     # Maven Wrapper (Windows)
```

---

## 🔗 Links Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Boa sorte com a execução! 🚀**

