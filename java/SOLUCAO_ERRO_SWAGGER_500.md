# Solução para Erro 500 no Swagger

## 🔍 Problema

Erro 500 ao acessar `/api-docs` ou `/swagger-ui.html`:
```
Failed to load API definition.
Errors: Fetch error - response status is 500 /api-docs
```

**Erro específico:**
```
NoSuchMethodError: 'void org.springframework.web.method.ControllerAdviceBean.<init>(java.lang.Object)'
```

Este erro indica **incompatibilidade de versões** entre Spring Boot e SpringDoc OpenAPI.

## ✅ Soluções Aplicadas

### 1. **Correção de Versões (PRINCIPAL)**

**Problema**: Spring Boot 3.4.0 tem incompatibilidade com SpringDoc 2.6.0

**Solução**: Downgrade para versões estáveis e compatíveis:
- Spring Boot: `3.4.0` → `3.3.5` (versão estável)
- SpringDoc OpenAPI: `2.6.0` → `2.5.0` (compatível com Spring Boot 3.3.5)

### 2. **Anotação @Hidden no GlobalExceptionHandler**

Adicionada anotação `@Hidden` em todos os métodos do `GlobalExceptionHandler` para que o SpringDoc ignore esses métodos durante a geração do OpenAPI:

```java
@ExceptionHandler(ResourceNotFoundException.class)
@Hidden
public ResponseEntity<Object> handleResourceNotFoundException(...)
```

Isso evita que o SpringDoc tente processar os handlers de exceção e cause o erro 500.

### 3. **Configuração do SpringDoc OpenAPI**

Adicionadas configurações no `application.properties`:
```properties
springdoc.default-produces-media-type=application/json
springdoc.default-consumes-media-type=application/json
springdoc.paths-to-match=/**
springdoc.packages-to-scan=com.farmacia.system.controller
springdoc.show-actuator=false
springdoc.model-and-view-allowed=false
```

### 2. **Prevenção de Referências Circulares**

Adicionadas anotações `@JsonIgnoreProperties` nas entidades para evitar loops infinitos na serialização:

- **Venda.java**: `@JsonIgnoreProperties("venda")` na lista de itens
- **ItemVenda.java**: 
  - `@JsonIgnoreProperties({"itens", "cliente", "usuario"})` em Venda
  - `@JsonIgnoreProperties({"categoria"})` em Medicamento
- **Medicamento.java**: `@JsonIgnoreProperties({"medicamentos"})` em Categoria

### 3. **Configuração de UUID no OpenAPI**

Adicionado schema customizado para UUID no `OpenApiConfig.java`:
```java
.addSchemas("UUID", new Schema<>()
    .type("string")
    .format("uuid")
    .example("550e8400-e29b-41d4-a716-446655440000"))
```

## 🔧 Verificações Adicionais

Se o erro persistir, verifique:

### 1. **Logs da Aplicação**

Verifique os logs do console para identificar o erro específico:
```cmd
# Os logs devem mostrar o stack trace completo do erro
```

### 2. **Validação do Schema do Banco**

Se houver erro de validação do schema, o Hibernate mostrará:
```
Schema-validation: missing table/column
```

**Solução**: Verifique se todas as tabelas e colunas existem no banco conforme esperado.

### 3. **Problemas com UUID**

Se houver problemas com UUID, verifique:
- Se o PostgreSQL está configurado para usar UUID
- Se a extensão `uuid-ossp` está habilitada (se necessário)

### 4. **Recompilar a Aplicação**

Após as alterações, recompile:
```cmd
cd java
mvnw.cmd clean install -DskipTests
mvnw.cmd spring-boot:run
```

## 🚀 Teste

Após aplicar as correções:

1. Reinicie a aplicação
2. Acesse: http://localhost:8081/swagger-ui.html
3. Ou acesse diretamente: http://localhost:8081/api-docs

## 📝 Notas

- As anotações `@JsonIgnoreProperties` evitam referências circulares que podem causar StackOverflowError
- A configuração do SpringDoc limita o escaneamento apenas aos controllers, evitando problemas com entidades
- O schema UUID customizado ajuda o Swagger a entender como serializar UUIDs

## ⚠️ Se o Problema Persistir

1. Verifique os logs completos da aplicação
2. Tente acessar diretamente: http://localhost:8081/api-docs
3. Verifique se há erros de validação do schema do banco
4. Verifique se todas as dependências estão corretas no `pom.xml`

