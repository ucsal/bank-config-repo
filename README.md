# Bank Config Repository

Repositório centralizado de configurações para os microserviços do Digital Bank UCSAL.

## Estrutura

- `account-service.yml` - Configurações padrão do serviço de contas
- `account-service-dev.yml` - Configurações para ambiente de desenvolvimento
- `account-service-prod.yml` - Configurações para ambiente de produção

## Profiles

- **default**: Configurações básicas compartilhadas
- **dev**: Desenvolvimento local com logs verbosos e limites relaxados
- **prod**: Produção com segurança reforçada e logs mínimos

## Segurança

Senhas e chaves sensíveis devem ser criptografadas usando o Config Server com encryption key.

Formato: `{cipher}ENCRYPTED_VALUE`
