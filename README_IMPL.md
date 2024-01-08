# README

## Instrucciones de Construcción y Ejecución del Proyecto

### 1. Crear el archivo Jar

Para construir el archivo JAR del proyecto, ejecuta el siguiente comando en la línea de comandos:

```
mvn clean install
```

El archivo JAR generado estará en el directorio **_target_** con un nombre similar a demo-[VERSION].jar.

### 2. Despliegue en el Servidor

Para Deplegar el servicio, ejecuta el siguente comando en la línea de comandos:

```
java -jar target/demo-[VERSION].jar
```

**_Ejemplo:_**

```
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### 3. Acceder a la Aplicación

Abre Postman y accede a la siguiente ruta: **_localhost:8080/v1/_**.
Aqui una [Postman Collection](src/main/resources/NuevoSPA.postman_collection.json) para importar.

### 4. Endpoints Disponibles

Los endpoints disponibles son:

- auth:
    - sign-up (POST)
    - login (POST)
    - auth (GET)

- task:
    - create (POST)
    - update (PUT)
    - delete (DELETE)
    - allTask (GET)
    - byNumberTask (GET)
    - byUser (GET)
    - byStatus (GET)

Los endpoints antes mencionados están detallados en el siguente
swagger: [Swagger](http://localhost:8080/swagger-ui/index.html#/)

## Sobre la Aplicacion:

### Base de Datos

Al momento de correr la aplicación, se crea una base de datos en memoria [h2](http://localhost:8080/h2-console), cuya
credenciales son:

```
username=sa
password=
```

Las tablas de esta aplicación son:

- **USER**
  ~~~
  id (KEY)
  username
  password
  ~~~

  Tabla que contiene los usuarios de la Aplicación cuya password se guarda encriptada.


- **TaskStatus**
  ~~~
  id (KEY)
  status
  ~~~

  Tabla que contiene los status disponible para las tareas como por ejemplo "**ON HOLD**" , "**IN PROGRESS**",
  "**COMPLETE**".


- **Task**
  ~~~
  id (KEY)
  numberTask
  title
  description
  user_id (ManyToOne)
  status_id (ManyToOne)
  ~~~  

  Tabla que contiene la información de las tareas, además de el id, tiene un identificador compuesto para las consultas
  por identificador y asi evitar ataques "sql injection", además esta relacionada con las otras dos tablas.

### Security

La aplicación tiene una capa de seguridad con Spring Security utilizando JWT.

## Notas Adicionales

* SpringBoot está diseñado para ejecutar aplicaciones en forma autónoma, incluye un servidor Tomcat embebido. Se puede
  ejecutar la aplicación directamente, sin necesidad de un servidor Tomcat externo.
* La documentación Swagger se genera una vez que se ejecuta la aplicacion (Despliegue de la aplicación) .
* Revisa la documentación detallada de cada endpoint para comprender mejor su funcionamiento y contratos de entrada.
* Por efecto de esta Demo, la version de este es **0.0.1-SNAPSHOT**.
* La base de datos se pre-cargan con estos [datos](src/main/resources/data.sql) para las tablas **USER**, **TaskStatus**
* Para campo **numberTask** de la tabla **Task**, se crea con un generador de secuencia ascendente, que se reinicia cada
  vez que arranca la aplicación, solo para simplificar el ejercicio.
* La version de spring boot **2.7.x** (siendo la ultima 2.7.18) presenta Vulnerabilidades en
  dependencias ([CVE-2016-1000027](https://devhub.checkmarx.com/cve-details/CVE-2016-1000027/?utm_source=jetbrains&utm_medium=referral&utm_campaign=idea), [CVE-2022-1471](https://devhub.checkmarx.com/cve-details/CVE-2022-1471/), [CVE-2023-24998](https://devhub.checkmarx.com/cve-details/CVE-2023-24998/), [CVE-2023-6378](https://devhub.checkmarx.com/cve-details/CVE-2023-6378/))
  que en versiones posteriores se solucionan, recomiendan simpre utilizar la versión más reciente de Spring Boot para
  evitar ataques maliciosos.