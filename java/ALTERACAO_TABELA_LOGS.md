# Alteração: Uso da Tabela 'logs' ao Invés de 'logs_auditoria'

## ✅ Alterações Realizadas

Todos os serviços e endpoints de logs foram ajustados para usar a tabela **`logs`** ao invés de **`logs_auditoria`**.

### 1. **Nova Entidade Log**

Criada entidade `Log.java` que mapeia para a tabela `logs`:

```java
@Entity
@Table(name = "logs")
public class Log {
    // Mesma estrutura que LogAuditoria, mas mapeando para tabela 'logs'
}
```

### 2. **Novo Repository**

Criado `LogRepository.java`:

```java
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findAllByOrderByDataHoraDesc(Pageable pageable);
}
```

### 3. **Services Atualizados**

#### **LogService**
- ✅ Usa `LogRepository` ao invés de `LogAuditoriaRepository`
- ✅ Retorna `List<Log>` ao invés de `List<LogAuditoria>`
- ✅ Exportação CSV usando dados da tabela `logs`

#### **AuditService**
- ✅ Usa `LogRepository` ao invés de `LogAuditoriaRepository`
- ✅ Grava logs na tabela `logs` usando entidade `Log`

#### **AuthService**
- ✅ Usa `LogRepository` ao invés de `LogAuditoriaRepository`
- ✅ Grava logs de login na tabela `logs`

#### **UsuarioService**
- ✅ Usa `LogRepository` ao invés de `LogAuditoriaRepository`
- ✅ Grava logs de ações de usuário na tabela `logs`

### 4. **Controller Atualizado**

#### **LogController**
- ✅ Endpoint `GET /logs` retorna `List<Log>` da tabela `logs`
- ✅ Endpoint `GET /logs/export` exporta dados da tabela `logs`

## 📋 Estrutura da Tabela 'logs'

A aplicação espera que a tabela `logs` tenha a seguinte estrutura:

- `id` (BIGSERIAL/BIGINT, PRIMARY KEY)
- `usuario` (VARCHAR, nullable)
- `data_hora` (TIMESTAMP, NOT NULL)
- `tipo` (VARCHAR, NOT NULL)
- `detalhes` (TEXT, nullable)

## 🔄 Arquivos Mantidos

Os seguintes arquivos foram **mantidos** mas **não são mais usados**:
- `LogAuditoria.java` - Entidade antiga (pode ser removida se não for necessária)
- `LogAuditoriaRepository.java` - Repository antigo (pode ser removido se não for necessário)

Você pode removê-los se não precisar mais da tabela `logs_auditoria`.

## ✅ Status

Todos os endpoints e gravações de log agora usam a tabela **`logs`**:

- ✅ `GET /logs` - Lista logs da tabela `logs`
- ✅ `GET /logs/export` - Exporta logs da tabela `logs`
- ✅ `AuditService.log()` - Grava na tabela `logs`
- ✅ Login - Grava na tabela `logs`
- ✅ Ações de usuário - Gravam na tabela `logs`

## 🚀 Próximos Passos

1. Reinicie a aplicação para carregar as novas classes
2. Teste os endpoints `/logs` e `/logs/export`
3. Verifique se os logs estão sendo gravados na tabela `logs`


