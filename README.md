# E-commerce Microservices

Plataforma de e-commerce construída com **arquitetura de microserviços** usando Spring Boot e Spring Cloud. O projeto demonstra os principais padrões de sistemas distribuídos: service discovery, API gateway, banco por serviço, comunicação síncrona entre serviços e **resiliência com circuit breaker**.

## Arquitetura

```
                         ┌─────────────────┐
   Cliente ──HTTP──►     │   API Gateway   │  :8080  (ponto único de entrada)
                         └────────┬────────┘
                                  │ roteia por path + load-balance via Eureka
          ┌───────────────────────┼───────────────────────┐
          ▼                       ▼                        ▼
   ┌─────────────┐         ┌─────────────┐          ┌─────────────┐
   │  catalog    │  :8081  │  inventory  │  :8082    │   order     │  :8083
   │  (produtos) │         │  (estoque)  │◄──Feign───│  (pedidos)  │
   └──────┬──────┘         └──────┬──────┘  + Circuit └──────┬──────┘
          │                       │          Breaker         │
          ▼                       ▼                          ▼
     catalog_db             inventory_db                order_db
   └──────────────── PostgreSQL único :5433 ───────────────────┘

              Todos se registram no ► Eureka Server :8761
```

## Serviços

| Serviço | Porta | Responsabilidade | Banco |
|---|---|---|---|
| **eureka-server** | 8761 | Service Discovery — todos os serviços se registram aqui | — |
| **api-gateway** | 8080 | Ponto único de entrada; roteia as requisições para os serviços | — |
| **catalog-service** | 8081 | Cadastro e consulta de produtos | `catalog_db` |
| **inventory-service** | 8082 | Consulta e baixa de estoque | `inventory_db` |
| **order-service** | 8083 | Criação de pedidos; consome o inventory via Feign + circuit breaker | `order_db` |

## Stack

- **Java 21**
- **Spring Boot 4.1.0**
- **Spring Cloud 2025.1.2** — Netflix Eureka (discovery), Gateway, OpenFeign
- **Resilience4j** — circuit breaker, retry, timeout, fallback
- **PostgreSQL 15** — uma instância, um banco por serviço
- **Flyway** — versionamento de schema por serviço
- **Lombok**, **Bean Validation**, **Spring Boot Actuator**
- **Docker / Docker Compose**

## Pré-requisitos

- Java 21+
- Maven 3.9+ (ou rode pela IntelliJ)
- Docker + Docker Compose (para o PostgreSQL)

## Como rodar

A ordem importa: **banco → Eureka → serviços → gateway**.

### 1. Subir o banco de dados

```bash
docker compose up -d
```

Sobe o PostgreSQL na porta `5433`. O script `init-scripts/init.sql` cria automaticamente os bancos `catalog_db`, `inventory_db` e `order_db`. Cada serviço executa suas próprias migrações Flyway (cria tabelas e insere dados de exemplo) ao iniciar.

### 2. Subir os serviços

Inicie primeiro o `eureka-server`, aguarde ficar disponível e então suba os demais (em terminais separados):

```bash
cd eureka-server     && mvn spring-boot:run   # aguarde subir (:8761)
cd catalog-service   && mvn spring-boot:run
cd inventory-service && mvn spring-boot:run
cd order-service     && mvn spring-boot:run
cd api-gateway       && mvn spring-boot:run
```

> Também é possível rodar cada serviço pela IntelliJ (classe `*Application.java` → Run).

### 3. Verificar o registro no Eureka

Acesse **http://localhost:8761** — os quatro serviços (CATALOG, INVENTORY, ORDER, API-GATEWAY) devem aparecer registrados.

## API

Todas as chamadas passam pelo **API Gateway** (`http://localhost:8080`).

### Produtos — `catalog-service`

```bash
# Listar produtos
curl http://localhost:8080/api/products

# Buscar por ID
curl http://localhost:8080/api/products/1

# Criar produto
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Webcam HD", "description": "Webcam 1080p", "price": 199.90}'
```

### Estoque — `inventory-service`

```bash
# Verificar disponibilidade
curl "http://localhost:8080/api/inventory/1?quantity=10"

# Dar baixa no estoque
curl -X POST "http://localhost:8080/api/inventory/1/deduct?quantity=5"
```

### Pedidos — `order-service`

```bash
# Criar pedido (valida e dá baixa no estoque via inventory-service)
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'

# Listar pedidos
curl http://localhost:8080/api/orders
```

## Dados de exemplo

Carregados via Flyway na inicialização:

| product_id | Produto | Estoque |
|---|---|---|
| 1 | Teclado Mecânico | 100 |
| 2 | Mouse Gamer | **0** |
| 3 | Monitor 24" | 150 |
| 4 | Headset Gamer | 300 |
| 5 | Cadeira Gamer | 250 |

> O produto **2** tem estoque zero — útil para demonstrar a rejeição de pedido por estoque insuficiente (`FAILED_NO_STOCK`).

## Resiliência (Circuit Breaker)

A chamada do `order-service` ao `inventory-service` é protegida por **Resilience4j** (`InventoryGateway`): retry, circuit breaker e fallback. Configuração em `order-service/src/main/resources/application.yml`:

- Janela deslizante de 10 chamadas
- Abre o circuito com 50% de falhas
- Fica 5s aberto e então vai para half-open (3 chamadas de teste)
- Retry de 3 tentativas com 500ms de espera

### Demonstração

```bash
# 1. Estado do circuito (CLOSED = saudável)
curl http://localhost:8083/actuator/circuitbreakers

# 2. Derrube o inventory-service (Ctrl+C no terminal dele)

# 3. Tente criar pedidos algumas vezes — as chamadas falham, sofrem retry
#    e, ao acumular falhas, o circuito ABRE (passa a cair direto no fallback)
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" -d '{"productId":1,"quantity":1}'

# 4. Estado do circuito agora (OPEN)
curl http://localhost:8083/actuator/circuitbreakers

# 5. Suba o inventory novamente → após ~5s vai para HALF_OPEN e volta para CLOSED
```

Endpoints do Actuator no `order-service`: `/actuator/health`, `/actuator/circuitbreakers`, `/actuator/retries`.

## Padrões e decisões de projeto

- **Service Discovery (Eureka):** serviços se localizam dinamicamente; o Gateway usa `lb://` para balanceamento de carga.
- **API Gateway:** ponto único de entrada, roteamento por path, isolando as portas internas do cliente.
- **Database per Service:** cada serviço tem seu próprio banco (`catalog_db`, `inventory_db`, `order_db`), reduzindo acoplamento.
- **Comunicação declarativa (OpenFeign):** o `InventoryClient` é apenas uma interface; o Spring gera o cliente HTTP.
- **Resiliência (Resilience4j):** circuit breaker + retry + timeout + fallback isolam falhas do inventory para não derrubar o order.
- **Migrações versionadas (Flyway):** `ddl-auto: validate` — o Hibernate valida, o Flyway é o dono do schema.
- **DTOs + Bean Validation:** entrada validada com `@Valid`; camada de API desacoplada do modelo de domínio.
- **Tratamento de erros centralizado:** `@RestControllerAdvice` por serviço.
- **Build multi-stage (Docker):** imagens enxutas com JRE Alpine.

## Estrutura do projeto

```
ecommerce-microservices/
├── docker-compose.yml          # PostgreSQL
├── init-scripts/init.sql       # cria os 3 bancos
├── eureka-server/              # service discovery
├── api-gateway/                # gateway / roteamento
├── catalog-service/            # produtos
├── inventory-service/          # estoque
└── order-service/              # pedidos (Feign + circuit breaker)
```

## Próximos passos

- Comunicação assíncrona (Kafka/RabbitMQ) para desacoplar order ↔ inventory
- Padrão **Saga** para consistência distribuída
- Configuração centralizada (Spring Cloud Config)
- Testes de integração e observabilidade (tracing distribuído)
- Adicionar os serviços ao `docker-compose` (os Dockerfiles já existem)