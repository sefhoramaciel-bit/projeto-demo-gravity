# Como Resolver o Problema da Porta 8081 em Uso

## 🔍 Situação Atual

O processo **25248** (java.exe) está usando a porta 8081.

## ✅ Soluções

### Opção 1: Encerrar o Processo Java (Recomendado)

**Windows (CMD como Administrador):**
```cmd
taskkill /PID 25248 /F
```

**Windows (PowerShell como Administrador):**
```powershell
Stop-Process -Id 25248 -Force
```

**Verificar se funcionou:**
```cmd
netstat -ano | findstr :8081
```
Se não retornar nada, a porta está livre.

### Opção 2: Mudar a Porta da Aplicação

Se você não quiser encerrar o processo, altere a porta no `application.properties`:

```properties
# Altere de 8081 para outra porta (ex: 8082)
server.port=8082
```

Depois, acesse a aplicação em `http://localhost:8082`.

### Opção 3: Encontrar e Encerrar Todos os Processos Java

**Windows (CMD):**
```cmd
tasklist | findstr java.exe
taskkill /F /IM java.exe
```

**⚠️ Atenção**: Isso encerrará TODOS os processos Java em execução.

## 🔄 Após Resolver

1. Execute novamente:
   ```cmd
   cd java
   mvnw.cmd spring-boot:run
   ```

2. A aplicação deve iniciar normalmente na porta 8081 (ou na porta que você configurou).

## 📝 Nota

Se você estiver rodando a aplicação em outro terminal, simplesmente feche aquele terminal ou pressione `Ctrl+C` para encerrar a aplicação anterior.


