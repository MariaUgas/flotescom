# Flotescom - Sistema de Gestión de Camiones

Sistema reactivo para la gestión de camiones y cargas, desarrollado con Spring WebFlux y R2DBC.

## Tecnologías Utilizadas

- Java 21
- Spring Boot 3.2.0
- Spring WebFlux
- R2DBC con PostgreSQL
- Flyway para migraciones
- Swagger/OpenAPI para documentación
- Docker y Docker Compose
- Reactor Test para pruebas

## Modelo de Datos

### Entidades Principales

#### Truck (Camión)
- `id`: UUID - Identificador único
- `licensePlate`: String - Placa del camión
- `model`: String - Modelo del camión
- `capacityLimit`: Double - Capacidad máxima de carga
- `currentLoad`: Double - Carga actual
- `status`: Enum - Estado del camión (AVAILABLE, LOADED, UNLOADED)

#### Load (Carga)
- `id`: UUID - Identificador único
- `truckId`: UUID - Referencia al camión
- `volume`: Double - Volumen de la carga
- `description`: String - Descripción de la carga
- `loadTimestamp`: LocalDateTime - Fecha y hora de carga
- `unloadTimestamp`: LocalDateTime - Fecha y hora de descarga

## Lógica de Negocio

El sistema permite:
1. Gestionar camiones (CRUD completo)
2. Asignar cargas a camiones
3. Descargar camiones
4. Verificar capacidades y estados
5. Mantener histórico de cargas

## Configuración y Ejecución

### Prerrequisitos
- Docker y Docker Compose instalados
- Java 21 para desarrollo local
- Maven para construcción local

## Ejecución con Docker

1. Clonar el repositorio:
```bash
git clone https://github.com/MariaUgas/flotescom.git
cd flotescom
```

2. Construir y levantar los contenedores:

 #### Usando Docker Compose
```bash
# Construir y ejecutar todos los servicios
docker-compose up --build

# Detener los servicios
docker-compose down

# Ver logs
docker-compose logs

# Detener y eliminar volúmenes
docker-compose down -v

```
####  Usando Dockerfile Directamente
Esta opción requiere configuración adicional para la base de datos.

```bash
# Construir la imagen
docker build -t flotescom:latest .

# Ejecutar el contenedor
docker run -p 8281:8281 --name flotescom-app flotescom:latest

# Detener el contenedor
docker stop flotescom-app

# Eliminar el contenedor
docker rm flotescom-app

# Ver logs
docker logs flotescom-app

La aplicación estará disponible en `http://localhost:8281`

### Ejecución Local

1. Configurar PostgreSQL local 
   - database: postgres
   - user: postgres
   - password: postgres

2. Ejecutar la aplicación:
```bash
./mvnw spring-boot:run
```

## Pruebas

### Ejecutar Pruebas Unitarias
```bash
./mvnw test
```

### Ejecutar Pruebas de Integración
```bash
./mvnw verify
```

## Documentación API (Swagger)

1. Acceder a Swagger UI:
```
http://localhost:8281/swagger-ui.html
```

2. Acceder a OpenAPI JSON:
```
http://localhost:8281/api-docs
```

## Endpoints Principales

### Camiones
- POST `/trucks` - Crear nuevo camión
- GET `/trucks` - Listar todos los camiones
- GET `/trucks/{id}` - Obtener camión por ID
- PUT `/trucks/{id}` - Actualizar camión
- DELETE `/trucks/{id}` - Eliminar camión

### Cargas
- POST `/trucks/{truckId}/loads` - Asignar carga a camión
- POST `/trucks/{truckId}/unload` - Descargar camión

## Desarrollo

### Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/maun/flotescom/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       ├── db/migration/
│       └── application.properties
└── test/
    ├── java/
    └── resources/
```

### Base de Datos
- Las migraciones se ejecutan automáticamente al iniciar
- Los scripts de migración están en `src/main/resources/db/migration/`


## Solución de Problemas

1. Si la aplicación no se conecta a la base de datos:
    - Verificar que el contenedor de PostgreSQL esté corriendo
    - Revisar las credenciales en el docker-compose.yml

2. Si las migraciones fallan:
    - Verificar los scripts en db/migration
    - Limpiar la base de datos si es necesario

3. Para reiniciar la aplicación:
```bash
docker-compose down
docker-compose up --build
```

## Contribución

1. Crear una rama para nuevas características
2. Ejecutar todas las pruebas antes de enviar cambios
3. Seguir el estilo de código existente
4. Documentar cambios significativos


## Contacto

www.linkedin.com/in/maría-de-los-angeles-ugas-navarro-4143a534
