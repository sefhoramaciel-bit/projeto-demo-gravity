# Verificação de Tipos de Dados - Banco Existente

## ⚠️ Pontos Críticos a Verificar

### 1. **Tipo do ID na Tabela `usuarios`**

A entidade `Usuario.java` usa `UUID`:
```java
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private java.util.UUID id;
```

**Se o banco usar um tipo diferente** (VARCHAR, BIGINT, etc.), você precisa ajustar:

#### Se o banco usar VARCHAR:
```java
@Id
@Column(name = "id", columnDefinition = "VARCHAR(36)")
private String id;
```

#### Se o banco usar BIGINT/INTEGER:
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

### 2. **Tipo do Campo `vendedor_id` na Tabela `vendas`**

A entidade `Venda.java` referencia `Usuario` que pode ser UUID:
```java
@ManyToOne
@JoinColumn(name = "vendedor_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
private Usuario vendedor;
```

**Se o banco usar um tipo diferente de UUID**, ajuste o tipo na tabela `vendas` ou na entidade `Usuario`.

### 3. **Tipos Numéricos**

Verifique se os tipos correspondem:
- `BigDecimal` no Java → `NUMERIC` ou `DECIMAL` no PostgreSQL
- `Integer` no Java → `INTEGER` no PostgreSQL
- `Long` no Java → `BIGINT` no PostgreSQL

### 4. **Tipos de Data**

- `LocalDate` no Java → `DATE` no PostgreSQL
- `LocalDateTime` no Java → `TIMESTAMP` no PostgreSQL

## 🔍 Como Verificar

Execute o script `consultar_estrutura_banco.sql` e compare:

1. **Tipo do `id` em `usuarios`**: Deve ser UUID, VARCHAR, ou BIGINT
2. **Tipo do `vendedor_id` em `vendas`**: Deve corresponder ao tipo do `id` em `usuarios`
3. **Tipos numéricos**: Verifique se `NUMERIC`, `DECIMAL`, `INTEGER`, `BIGINT` correspondem
4. **Tipos de data**: Verifique se `DATE` e `TIMESTAMP` correspondem

## 🔧 Ajustes Comuns

### Se `usuarios.id` for VARCHAR ao invés de UUID:

**Opção 1: Alterar a entidade para String**
```java
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @Column(name = "id", length = 36)
    private String id;  // Mudar de UUID para String
    
    // ... resto do código
}
```

**Opção 2: Usar @Type do Hibernate (se disponível)**
```java
@Id
@Type(type = "uuid-char")
@Column(name = "id", length = 36)
private UUID id;
```

### Se `vendedor_id` em `vendas` for NULL mas a entidade espera NOT NULL:

A entidade já está configurada para aceitar NULL:
```java
@JoinColumn(name = "vendedor_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
```

Isso está correto se o banco permitir NULL.

## 📝 Checklist de Verificação

Execute o script SQL e verifique:

- [ ] `usuarios.id` - Tipo correto?
- [ ] `usuarios.role` - Tipo VARCHAR com valores 'ADMIN'/'VENDEDOR'?
- [ ] `vendas.vendedor_id` - Tipo corresponde ao `usuarios.id`?
- [ ] `medicamentos.preco` - Tipo NUMERIC/DECIMAL?
- [ ] `medicamentos.estoque` - Tipo INTEGER?
- [ ] `vendas.total` - Tipo NUMERIC/DECIMAL?
- [ ] `itens_venda.preco_unitario` - Tipo NUMERIC/DECIMAL?
- [ ] Todas as datas - Tipo DATE ou TIMESTAMP conforme esperado?

## 🚨 Erros Comuns e Soluções

### Erro: "Wrong column type in usuarios.id"
**Solução**: Ajuste o tipo na entidade conforme o tipo no banco.

### Erro: "Foreign key constraint violation"
**Solução**: Verifique se os tipos de `vendedor_id` e `usuarios.id` correspondem.

### Erro: "Column 'data_nascimento' not found"
**Solução**: Verifique se o nome da coluna no banco é exatamente `data_nascimento` (pode ser `dataNascimento` ou outro nome).

## 📚 Próximos Passos

1. Execute `consultar_estrutura_banco.sql`
2. Compare os tipos com as entidades Java
3. Ajuste as entidades conforme necessário
4. Teste a aplicação - o Hibernate validará automaticamente


