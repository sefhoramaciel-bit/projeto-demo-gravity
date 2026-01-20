# Correção do Erro 500 no Swagger - NoSuchMethodError

## 🔍 Erro Identificado

```
NoSuchMethodError: 'void org.springframework.web.method.ControllerAdviceBean.<init>(java.lang.Object)'
```

## ✅ Correções Aplicadas

### 1. **Downgrade do Spring Boot (CRÍTICO)**

**Antes:**
```xml
<version>3.4.0</version>
```

**Depois:**
```xml
<version>3.3.5</version>
```

**Motivo**: Spring Boot 3.4.0 tem incompatibilidade interna com o SpringDoc OpenAPI, causando `NoSuchMethodError`.

### 2. **Ajuste da Versão do SpringDoc**

**Antes:**
```xml
<version>2.6.0</version>
```

**Depois:**
```xml
<version>2.5.0</version>
```

**Motivo**: Versão compatível com Spring Boot 3.3.5.

### 3. **Anotação @Hidden no GlobalExceptionHandler**

Adicionada `@Hidden` em todos os métodos de tratamento de exceção:

```java
@ExceptionHandler(ResourceNotFoundException.class)
@Hidden
public ResponseEntity<Object> handleResourceNotFoundException(...)
```

**Motivo**: Evita que o SpringDoc tente processar os handlers de exceção durante a geração do OpenAPI.

## 🚀 Próximos Passos

1. **Limpar e recompilar:**
   ```cmd
   cd java
   mvnw.cmd clean install -DskipTests
   ```

2. **Reiniciar a aplicação:**
   ```cmd
   mvnw.cmd spring-boot:run
   ```

3. **Testar o Swagger:**
   - Acesse: http://localhost:8081/swagger-ui.html
   - Ou: http://localhost:8081/api-docs

## 📝 Notas

- Spring Boot 3.3.5 é uma versão estável e bem testada
- SpringDoc 2.5.0 é totalmente compatível com Spring Boot 3.3.5
- A anotação `@Hidden` é necessária para evitar que o SpringDoc processe handlers de exceção

## ⚠️ Se o Erro Persistir

1. Verifique se o Maven baixou as novas dependências:
   ```cmd
   mvnw.cmd dependency:tree
   ```

2. Limpe o cache do Maven:
   ```cmd
   mvnw.cmd clean
   rmdir /s /q target
   mvnw.cmd install -DskipTests
   ```

3. Verifique os logs completos da aplicação para outros erros


