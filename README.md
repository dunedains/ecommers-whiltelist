# Wishlist Service

Microservicio de lista de deseos. Permite a los usuarios guardar productos favoritos para comprarlos después.

## Información general

| Campo | Valor |
|-------|-------|
| Puerto | `8083` |
| Base de datos | `db_whitelist` (PostgreSQL) |
| Contexto | `/api/wishlist` |

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/wishlist/user/{userId}` | Ver wishlist de un usuario |
| `GET` | `/api/wishlist/check?userId=&productId=` | Verificar si un producto esta en la wishlist |
| `POST` | `/api/wishlist` | Agregar producto a la wishlist |
| `DELETE` | `/api/wishlist/{id}` | Eliminar producto de la wishlist |
| `GET` | `/api/usuario/{userId}` | Obtener datos del usuario |
| `GET` | `/api/usuario/{userId}/wishlist` | Ver wishlist de un usuario (ruta alternativa) |

## Ejemplo de uso

**Agregar a la wishlist:**
```bash
curl -X POST http://localhost:8083/api/wishlist \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "userId": 1,
  "productId": 1
}
```

**Ver wishlist:**
```bash
curl http://localhost:8083/api/wishlist/user/1
```

**Eliminar de la wishlist:**
```bash
curl -X DELETE http://localhost:8083/api/wishlist/1
```

## Validaciones

- No se puede agregar el mismo producto dos veces para el mismo usuario
- El usuario y el producto deben existir (verificado vía Feign)

## Modelo de datos

```sql
CREATE TABLE wishlist (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT uq_wishlist_user_product UNIQUE (user_id, product_id)
);
```

## Dependencias externas

| Servicio | Uso | Puerto |
|---------|-----|--------|
| **productos** | Valida que el producto exista | `8081` |
| **users** | Valida que el usuario exista | `8082` |

## Configuración (variables de entorno Docker)

| Variable | Descripción |
|----------|-------------|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos |
| `FEIGN_CLIENT_PRODUCT_URL` | URL del servicio de productos |
| `FEIGN_CLIENT_USER_URL` | URL del servicio de usuarios |

## Tecnologías

- Java 25 · Spring Boot 4.0.6
- Spring Data JPA · Hibernate 7
- Spring Cloud OpenFeign
- Flyway (migraciones)
- PostgreSQL 16
- Lombok · Bean Validation
