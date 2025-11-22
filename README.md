# 🏦 Spring Cloud Config - Sistema Bancário Digital

Implementação completa de **Spring Cloud Config** para gerenciamento centralizado de configurações em um sistema bancário com múltiplos ambientes.

---

## 📁 Estrutura do Repositório
```
bank-config-repo/
├── README.md                    # Este arquivo
├── ENTREGA.md                   # Documento de entrega da atividade
├── configs/                     # Configurações externalizadas
│   ├── account-service.yml
│   ├── account-service-dev.yml
│   └── account-service-prod.yml
├── config-server/               # Spring Cloud Config Server
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── account-service/             # Microserviço cliente
│   ├── src/
│   ├── pom.xml
│   └── README.md
└── screenshots/                 # Evidências dos testes
```

---

## 🚀 Como Executar

### 1️⃣ Executar o Config Server
```bash
cd config-server
./mvnw spring-boot:run
```

Acesse: http://localhost:8888/actuator/health

### 2️⃣ Testar as configurações via Config Server
```bash
# Profile DEFAULT
curl -u configadmin:admin123 http://localhost:8888/account-service/default

# Profile DEV
curl -u configadmin:admin123 http://localhost:8888/account-service/dev

# Profile PROD
curl -u configadmin:admin123 http://localhost:8888/account-service/prod
```

### 3️⃣ Executar o Account Service

**Profile Default:**
```bash
cd account-service
./mvnw spring-boot:run
```

**Profile Dev:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Profile Prod:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### 4️⃣ Testar os endpoints do Account Service
```bash
# Visualizar configurações atuais
curl http://localhost:8081/api/config

# Health check
curl http://localhost:8081/api/config/health
```

### 5️⃣ Testar Refresh Dinâmico (sem restart)
```bash
# 1. Alterar configuração no Git
# Edite: configs/account-service-dev.yml
# Mude: max-daily-transfer: 10000.00 para 15000.00

# 2. Commit e push
git add configs/account-service-dev.yml
git commit -m "Update transfer limit"
git push origin main

# 3. Forçar refresh no serviço (sem restart!)
curl -X POST http://localhost:8081/actuator/refresh

# 4. Verificar mudança
curl http://localhost:8081/api/config
```

---

## 🎯 Funcionalidades Implementadas

✅ Config Server centralizado  
✅ Configurações versionadas no Git  
✅ Múltiplos perfis (default, dev, prod)  
✅ Refresh dinâmico sem restart  
✅ Segurança com Basic Auth  
✅ Suporte a criptografia de senhas  
✅ Separação clara de ambientes  
✅ Endpoints de monitoramento (Actuator)

---

## 🔐 Segurança

**Credenciais do Config Server:**
- **Usuário:** `configadmin`
- **Senha:** `admin123`

**Chave de Criptografia:**
```yaml
encrypt:
  key: MySecretKey123456789012345678901234567890123456789012345678
```

---

## 📊 Perfis de Configuração

### Default
- Limite diário: R$ 5.000
- PIX: Habilitado
- Transferência Internacional: Desabilitado

### Dev (Desenvolvimento)
- Limite diário: R$ 10.000 (mais flexível para testes)
- PIX: Habilitado
- Transferência Internacional: Habilitado
- Logs: DEBUG/TRACE

### Prod (Produção)
- Limite diário: R$ 5.000 (mais restritivo)
- PIX: Habilitado
- Transferência Internacional: Desabilitado
- Logs: WARN/INFO
- Senhas criptografadas

---

## 📚 Documentação Adicional

- [ENTREGA.md](./ENTREGA.md) - Documento completo da atividade
- [config-server/README.md](./config-server/README.md) - Documentação do servidor
- [account-service/README.md](./account-service/README.md) - Documentação do cliente

---

## 👨‍💻 Autor

**Alvaro Dultra**
- GitHub: [@AlvaroDultra](https://github.com/AlvaroDultra)
- LinkedIn: [alvarodultra](https://www.linkedin.com/in/alvarodultra/)
- Email: alvarodultra.dev@gmail.com

---

## 📖 Referências

- [Spring Cloud Config](https://spring.io/projects/spring-cloud-config)
- [Spring Cloud Config Reference](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
