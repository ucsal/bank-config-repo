# Config Server

Servidor centralizado de configurações usando Spring Cloud Config Server.

## 🚀 Como Executar
```bash
./mvnw spring-boot:run
```

Servidor inicia na porta: **8888**

## 📡 Endpoints

- **Health Check:** http://localhost:8888/actuator/health
- **Buscar Configs:** http://localhost:8888/{application}/{profile}

### Exemplos:
```bash
# Configuração default do account-service
curl -u configadmin:admin123 http://localhost:8888/account-service/default

# Configuração de desenvolvimento
curl -u configadmin:admin123 http://localhost:8888/account-service/dev

# Configuração de produção
curl -u configadmin:admin123 http://localhost:8888/account-service/prod
```

## 🔐 Credenciais

- **Usuário:** `configadmin`
- **Senha:** `admin123`

## ⚙️ Configuração

Conecta ao repositório Git:
```
https://github.com/ucsal/bank-config-repo.git
Branch: main
Pasta de configs: configs/
```

## 🔑 Criptografia

Chave configurada para criptografar/descriptografar valores sensíveis com prefixo `{cipher}`.
