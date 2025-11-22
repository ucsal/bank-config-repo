# Account Service

Microserviço de contas bancárias que consome configurações do Config Server.

## 🚀 Como Executar

### Profile Default:
```bash
./mvnw spring-boot:run
```

### Profile Dev:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Profile Prod:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

Servidor inicia na porta: **8081**

## 📡 Endpoints

### Visualizar Configurações Atuais
```bash
curl http://localhost:8081/api/config
```

### Health Check
```bash
curl http://localhost:8081/api/config/health
```

### Refresh Dinâmico (atualizar configs sem restart)
```bash
curl -X POST http://localhost:8081/actuator/refresh
```

## 🔄 Testando Refresh Dinâmico

1. Altere uma configuração no Git (ex: `configs/account-service-dev.yml`)
2. Commit e push
3. Execute: `curl -X POST http://localhost:8081/actuator/refresh`
4. Verifique a mudança: `curl http://localhost:8081/api/config`

✅ Configuração atualizada sem restart!

## ⚙️ Configuração

Conecta ao Config Server em:
```
http://localhost:8888
Usuário: configadmin
Senha: admin123
```
