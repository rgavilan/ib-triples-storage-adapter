# ASIO - Triples Storage Adapter

Storage adapter para el almacenamiento de tripletas para el proyecto Backend SGI (ASIO).

## OnBoarding

Para iniciar el entorno de desarrollo se necesita cumplir los siguientes requisitos:

* OpenJDK 11
* Eclipse JEE 2019-09 con plugins:
  * Spring Tools 4
  * m2e-apt
  * Lombok
* Docker

### Instalar Pentaho

Se debe descargar de  [https://sourceforge.net/projects/pentaho/files/Pentaho%209.0/server/](https://sourceforge.net/projects/pentaho/files/Pentaho%209.0/server/) la versión pdi-ce-9.0.0.0-423.zip

## Módulos disponibles

* **Módulo back**: módulo que añade una capa de servicios REST a la funcionalidad de la aplicación. Genera un artefacto JAR bootable
* **Módulo service**: módulo que contiene la lógica de la aplicación. Puede ser utilizado como librería independiente para ser integrado en otras aplicaciones

## Metodología de desarrollo

La metodología de desarrollo es Git Flow.

## Entorno de desarrollo Docker

La inicialización de los elementos adicionales al entorno de desarrollo se realiza con docker. 

En el directorio docker-devenv se ha configurado un fichero docker-compose.yml para poder arrancar el entorno de desarrollo.

Para arrancar el entorno:

```bash
docker-compose up -d
```

Para pararlo:

```bash
docker-compose down
```

## Swagger

Se ha añadido la posibilidad de utilizar Swagger. Para acceder a Swagger, se utilizará la siguiente URL:

* http://localhost:8080/swagger-ui.html

Para activar swagger se utilizará la variable `app.swagger.enabled`

## Instalación en entorno real

Será preciso configurar las siguientes variables de entorno cuando se instale en un entorno real:

|Variable|Descripción|Valor por defecto|
|---|---|---|
|`APP_TRELLIS_ENABLED`|Flag que indica si se debe persistir en Trellis. Valores admisibles `true` y `false`|true|
|`APP_TRELLIS_ENDPOINT`|URL del servicio de Trellis|http://localhost:80|
|`APP_TRELLIS_AUTHENTICATION_ENABLED`|Flag que indica si se debe añadir autenticación a las peticiones Trellis. Valores admisibles `true` y `false`|false|
|`APP_TRELLIS_AUTHENTICATION_USERNAME`|Usuario Trellis|admin|
|`APP_TRELLIS_AUTHENTICATION_PASSWORD`|Contraseña Trellis|admin|
|`APP_WIKIBASE_ENABLED`|Flag que indica si se debe persistir en Wikibase. Valores admisibles `true` y `false`|false|
|`APP_WIKIBASE_API_URL`|URL del servicio del API de Wikibase|http://localhost:8181/api.php|
|`APP_WIKIBASE_API_USERNAME`|Usuario en Wikibase|WikibaseAdmin|
|`APP_WIKIBASE_API_PASSWORD`|Contraseña para usuario en Wikibase|WikibaseDockerAdminPass|
|`APP_WIKIBASE_API_QUERY_DEFAULT_LANGUAGE`|Lenguaje por defecto en Wikibase|es|
|`APP_WIKIBASE_API_SITE_URI`|URL del servicio de entidad en Wikibase|http://localhost:8181/entity/|

### Ejecución

Al generarse un JAR bootable la ejecución se realizará mediante el siguiente comando:

```bash
java -jar {jar-name}.jar
```

Sustituyendo `{jar-name}` por el nombre del fichero JAR generado.

No es necesario especificar la clase de inicio de la aplicación, ya que el fichero MANIFEST.MF generado ya contiene la información necesaria. Solamente se especificarán los parametros necesarios.

##  Documentación adicional

* [Compilación](docs/build.md)
* [Generación Docker](docs/docker.md)
