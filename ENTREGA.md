# Spring Cloud Config - Sistema Bancário Digital
**Disciplina:** Implementação de Spring  
**Aluno:** Alvaro Dultra  
**Data:** 22/11/2025

---

## 📌 Repositório do Projeto

**Repositório único:** https://github.com/ucsal/bank-config-repo

**Estrutura:**
- `configs/` - Arquivos de configuração YAML
- `config-server/` - Código do Config Server
- `account-service/` - Código do microserviço cliente


---

## a) Conceito de Configuração Externalizada e Centralizada

### Definição

**Configuração Externalizada:** Manter configurações fora do código-fonte, em arquivos externos ou serviços dedicados. Permite modificar comportamentos sem recompilar a aplicação.

**Configuração Centralizada:** Armazenamento de todas as configurações de múltiplos microserviços em um único local, facilitando gerenciamento e consistência.

### Spring Cloud Config

O Spring Cloud Config fornece suporte server-side e client-side para configuração externalizada em sistemas distribuídos.

**Componentes:**
1. **Config Server** - Servidor HTTP que expõe configurações armazenadas em Git
2. **Config Client** - Microserviços que consomem configurações
3. **Repositório Git** - Armazena arquivos de configuração versionados

### Benefícios

- ✅ **Versionamento** via Git com histórico completo
- ✅ **Auditoria** de todas as mudanças
- ✅ **Rollback** instantâneo (git revert)
- ✅ **Separação de ambientes** (dev, prod)
- ✅ **Refresh dinâmico** sem restart
- ✅ **Segurança** com criptografia {cipher}
- ✅ **Single source of truth**

### Antes vs Depois

**Antes (sem Config Server):**
```
Mudança de configuração = Alterar código + Build + Deploy + Restart
⏱️ Tempo: 30-60 minutos
💰 Custo: Downtime significativo
```

**Depois (com Config Server):**
```
Mudança de configuração = Editar Git + Commit + Refresh endpoint
⏱️ Tempo: 30 segundos
💰 Custo: Zero downtime
```

---

## b) Importância no Sistema Bancário

### Cenário: Banco Digital com Múltiplos Ambientes

Um banco digital opera com dezenas de microserviços em diferentes ambientes:
- **Desenvolvimento (DEV)** - Testes de desenvolvedores
- **Homologação (HML)** - Validação pré-produção
- **Produção (PROD)** - Ambiente real com clientes

### Por que Config Server é Essencial?

#### 1. **Segurança e Compliance**

Bancos lidam com dados sensíveis:
- Credenciais de banco de dados
- Chaves de API (PIX, TED, BACEN)
- Certificados e tokens

**Solução:**
- Criptografia com `{cipher}`
- Controle de acesso centralizado
- Auditoria completa via Git

#### 2. **Mudanças em Tempo Real**

**Exemplo: Black Friday com Fraudes**
```yaml
# Reduzir limite emergencialmente
bank:
  max-daily-transfer: 5000  # Era 10000

# Commit + Push + Refresh
# ✅ Atualizado em 30 segundos em TODA a frota
# ✅ Zero downtime
```

**Exemplo: Manutenção do BACEN**
```yaml
# Desabilitar PIX temporariamente
bank:
  features:
    pix-enabled: false

# Após manutenção
bank:
  features:
    pix-enabled: true

# ✅ Mudança instantânea
```

#### 3. **Disaster Recovery**

**Incidente em Produção:**
```bash
# 14:00 - Deploy com erro
# 14:05 - Alertas disparados
# 14:06 - Identificação do problema
# 14:07 - Rollback: git revert HEAD
# 14:08 - Refresh em todos os serviços
# 14:09 - Sistema recuperado

✅ Downtime: 4 minutos
🆚 Sem Config Server: ~60 minutos
```

#### 4. **Conformidade Regulatória (BACEN)**

**Requisitos:**
- 📋 Rastreabilidade completa
- 🔒 Segregação de acesso
- 📊 Logs de auditoria
- ✅ Processo documentado

**Como Config Server atende:**
```bash
git log configs/*-prod.yml
# ✅ Histórico completo
# ✅ Quem, quando, o quê
# ✅ Aceito em auditorias
```

#### 5. **Gestão de Múltiplos Serviços**

**Sem Config Server:**
```
account-service/application-dev.properties
account-service/application-prod.properties
payment-service/application-dev.properties
payment-service/application-prod.properties
... (20+ microserviços × 3 ambientes)

❌ Duplicação
❌ Inconsistência
❌ Difícil manter
```

**Com Config Server:**
```
bank-config-repo/configs/
  ├── account-service.yml
  ├── account-service-dev.yml
  ├── account-service-prod.yml
  ├── payment-service.yml
  ├── payment-service-dev.yml
  └── payment-service-prod.yml

✅ Centralizado
✅ Consistente
✅ Fácil manter
```

### Resumo Comparativo

| Aspecto | Sem Config Server | Com Config Server |
|---------|-------------------|-------------------|
| **Mudança** | 30-60 min | 30 seg |
| **Downtime** | Minutos/Horas | Zero |
| **Segurança** | Senhas em código | Criptografia |
| **Rollback** | Redeploy | git revert |
| **Auditoria** | Difícil | Git log |
| **Escalabilidade** | Difícil | Trivial |

**Conclusão:** Em um sistema bancário com transações de milhões por dia e requisitos rigorosos de segurança, o Config Server **NÃO é opcional, é ESSENCIAL**.

---

## c) Implementação Prática

### Arquitetura
```
┌─────────────────────┐
│   GitHub Repo       │
│  bank-config-repo   │
│   /configs/*.yml    │
└──────────┬──────────┘
           │ Git Pull
           ▼
┌─────────────────────┐
│   Config Server     │
│   (Port 8888)       │
│  - Busca configs    │
│  - Merge profiles   │
│  - Retorna JSON     │
└──────────┬──────────┘
           │ HTTP REST
           ▼
┌─────────────────────┐
│  Account Service    │
│   (Port 8081)       │
│  - Consome configs  │
│  - Expõe endpoints  │
└─────────────────────┘
```

### Componentes Implementados

#### 1. Config Server (`config-server/`)
- Anotação: `@EnableConfigServer`
- Conecta ao Git: `https://github.com/ucsal/bank-config-repo.git`
- Segurança: Basic Auth (configadmin/admin123)
- Porta: 8888

#### 2. Configurações (`configs/`)
- `account-service.yml` - Base (default)
- `account-service-dev.yml` - Desenvolvimento
- `account-service-prod.yml` - Produção

#### 3. Account Service (`account-service/`)
- Anotação: `@RefreshScope` (permite refresh dinâmico)
- Conecta ao Config Server
- Endpoints: `/api/config`, `/api/config/health`
- Porta: 8081

### Configurações por Perfil

#### DEFAULT
```yaml
bank:
  max-daily-transfer: 5000.00
  features:
    pix-enabled: true
    international-transfer: false
```

#### DEV
```yaml
bank:
  max-daily-transfer: 10000.00  # Limite maior
  features:
    international-transfer: true  # Habilitado
logging:
  level:
    root: DEBUG  # Logs verbosos
```

#### PROD
```yaml
bank:
  max-daily-transfer: 5000.00  # Limite restritivo
  features:
    international-transfer: false  # Desabilitado
datasource:
  password: '{cipher}...'  # Criptografada
logging:
  level:
    root: WARN  # Logs mínimos
```

### Como Executar

#### 1. Config Server
```bash
cd config-server
./mvnw spring-boot:run
```

#### 2. Account Service (Default)
```bash
cd account-service
./mvnw spring-boot:run
```

#### 3. Account Service (Dev)
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### 4. Account Service (Prod)
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### Testes Realizados

#### Teste 1: Profile DEFAULT
```bash
curl http://localhost:8081/api/config
```
**Resultado:**
- ✅ Limite: R$ 5.000
- ✅ PIX: Habilitado
- ✅ Internacional: Desabilitado

#### Teste 2: Profile DEV
```bash
# Iniciar com profile dev
curl http://localhost:8081/api/config
```
**Resultado:**
- ✅ Limite: R$ 10.000
- ✅ Internacional: Habilitado
- ✅ Logs: DEBUG

#### Teste 3: Profile PROD
```bash
# Iniciar com profile prod
curl http://localhost:8081/api/config
```
**Resultado:**
- ✅ Limite: R$ 5.000
- ✅ Internacional: Desabilitado
- ✅ Logs: WARN

#### Teste 4: Refresh Dinâmico

**Passo 1:** Verificar valor atual
```bash
curl http://localhost:8081/api/config | jq '.maxDailyTransfer'
# Resultado: 10000.0
```

**Passo 2:** Alterar no Git
```bash
# Editar: configs/account-service-dev.yml
# Mudar: max-daily-transfer: 15000.00
git commit -m "Update transfer limit"
git push
```

**Passo 3:** Forçar refresh (SEM RESTART!)
```bash
curl -X POST http://localhost:8081/actuator/refresh
```

**Passo 4:** Verificar mudança
```bash
curl http://localhost:8081/api/config | jq '.maxDailyTransfer'
# Resultado: 15000.0  ✅ Atualizado!
```

**Tempo total: ~30 segundos, zero downtime!**

---

## d) Proteção de Configurações Sensíveis

### Conceito

O Spring Cloud Config suporta criptografia de propriedades sensíveis usando o prefixo `{cipher}`.

### Como Funciona

#### 1. Configurar chave no Config Server
```yaml
encrypt:
  key: MySecretKey123456789012345678901234567890123456789012345678
```

#### 2. Criptografar valores (Conceitual)
```bash
curl -u configadmin:admin123 \
  http://localhost:8888/encrypt \
  -d "senha_super_secreta"

# Retorna:
682bc583aa8aa1d8df69c553f6c71b4e8f7a3d2b1c9e5a7f4d6b8c0e2a4f6d8b
```

#### 3. Usar no YAML
```yaml
spring:
  datasource:
    password: '{cipher}682bc583aa8aa1d8df69c553f6c71b4e8f7a3d2b1c9e5a7f4d6b8c0e2a4f6d8b'
```

#### 4. Descriptografia Automática
O Config Server descriptografa antes de enviar para o cliente.

### Exemplo Prático no Banco

**account-service-prod.yml:**
```yaml
spring:
  datasource:
    password: '{cipher}AQICAHhw...encrypted...'

bank:
  api:
    bacen-api-key: '{cipher}AQICAHhw...encrypted...'
  security:
    jwt-secret: '{cipher}AQICAHhw...encrypted...'
```

### Vantagens

1. ✅ **Segurança em repouso** - Senhas não ficam em texto plano no Git
2. ✅ **Auditoria** - Git log não expõe credenciais
3. ✅ **Controle de acesso** - Apenas Config Server descriptografa
4. ✅ **Conformidade** - Atende BACEN e PCI-DSS

### Nota sobre Implementação

Embora o endpoint `/encrypt` tenha apresentado incompatibilidades com a versão mais recente do Spring Boot, o conceito foi documentado. Em produção, recomenda-se:

1. **Chaves assimétricas (RSA)**
2. **Integração com Vault/AWS KMS**
3. **Versionamento de Spring Cloud compatível**

---

## 📊 Resultados e Conclusões

### Implementado com Sucesso

✅ Config Server centralizado conectado ao Git  
✅ Múltiplos perfis (default, dev, prod)  
✅ Account Service consumindo configurações  
✅ Refresh dinâmico sem restart  
✅ Separação de ambientes com diferentes configs  
✅ Versionamento via Git  
✅ Arquitetura escalável

### Benefícios Comprovados

1. **Agilidade** - Mudanças em segundos vs minutos/horas
2. **Segurança** - Controle centralizado e auditável
3. **Resiliência** - Rollback imediato
4. **Escalabilidade** - Fácil adicionar novos serviços
5. **Conformidade** - Rastreabilidade completa

### Melhorias Futuras

- Implementar criptografia assimétrica
- Integrar com HashiCorp Vault
- Adicionar bus de eventos para refresh automático
- Circuit breaker para falhas do Config Server
- Monitoramento com Spring Boot Admin

---

## 🔗 Repositório

**GitHub:** https://github.com/ucsal/bank-config-repo

**Conteúdo:**
- Código completo do Config Server
- Código completo do Account Service
- Configurações YAML para todos os perfis
- Screenshots dos testes realizados
- Documentação técnica

---

## 📚 Referências

- [Spring Cloud Config Documentation](https://spring.io/projects/spring-cloud-config)
- [Spring Cloud Config Reference Guide](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/)
- [Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)

---


