![](./images/logos_feder.png)

| Entregable     | Backend SGI Software                                                                                                                                                                                                                                                                                                                                                                                                            |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Fecha          | 25/05/2020                                                                                                                                                                                                                                                                                                                                                                                                                      |
| Proyecto       | [ASIO](https://www.um.es/web/hercules/proyectos/asio) (Arquitectura Semántica e Infraestructura Ontológica) en el marco de la iniciativa [Hércules](https://www.um.es/web/hercules/) para la Semántica de Datos de Investigación de Universidades que forma parte de [CRUE-TIC](http://www.crue.org/SitePages/ProyectoHercules.aspx)                                                                                            |
| Módulo         | Storage adapter                                                                                                                                                                                                                                                                                                                                                                                                                 |
| Tipo           | Software                                                                                                                                                                                                                                                                                                                                                                                                                        |
| Objetivo       | Storage adapter para el almacenamiento de tripletas para el proyecto Backend SGI (ASIO).                                                                                                                                                                                                                                                                                                                                        |
| Estado         | **60%** El Storage Adapter guarda los datos en Trellis y Wikidata                                                                                                                                                                                                                                                                                                                                                               |
| Próximos pasos | Ampliarlo para que realice la actualización de datos y el borrado.                                                                                                                                                                                                                                                                                                                                                              |
| Documentación  | [Manual de usuario](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/12-An%C3%A1lisis/Manual%20de%20usuario/Manual%20de%20usuario.md)<br />[Manual de despliegue](https://github.com/HerculesCRUE/ib-asio-composeset/blob/master/README.md)<br />[Documentación técnica](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/11-Arquitectura/ASIO_Izertis_Arquitectura.md) |

# ASIO - Triples Storage Adapter

Storage adapter para el almacenamiento de tripletas para el proyecto Backend SGI (ASIO).

## OnBoarding

Para iniciar el entorno de desarrollo se necesita cumplir los siguientes requisitos:

- OpenJDK 11
- Eclipse JEE 2019-09 con plugins:
  - Spring Tools 4
  - m2e-apt
  - Lombok
- Docker

### Instalar Pentaho

Se debe descargar de [https://sourceforge.net/projects/pentaho/files/Pentaho%209.0/server/](https://sourceforge.net/projects/pentaho/files/Pentaho%209.0/server/) la versión pdi-ce-9.0.0.0-423.zip

## Módulos disponibles

- **Módulo back**: módulo que añade una capa de servicios REST a la funcionalidad de la aplicación. Genera un artefacto JAR bootable
- **Módulo service**: módulo que contiene la lógica de la aplicación. Puede ser utilizado como librería independiente para ser integrado en otras aplicaciones
- **Módulo service-abstractions**: módulo con utilidades para la generación de servicios
- **Módulo swagger**: módulo que contine la funcionalidad necesaria para añadir Swagger para la interacción con el API Rest

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

- http://localhost:8080/swagger-ui.html

Para activar swagger se utilizará la variable `app.swagger.enabled`

## Instalación en entorno real

Será preciso configurar las siguientes variables de entorno cuando se instale en un entorno real:

| Variable                                  | Descripción                                                                                                   | Valor por defecto                        |
| ----------------------------------------- | ------------------------------------------------------------------------------------------------------------- | ---------------------------------------- |
| `APP_TRELLIS_ENABLED`                     | Flag que indica si se debe persistir en Trellis. Valores admisibles `true` y `false`                          | true                                     |
| `APP_TRELLIS_ENDPOINT`                    | URL del servicio de Trellis                                                                                   | http://localhost:80                      |
| `APP_TRELLIS_AUTHENTICATION_ENABLED`      | Flag que indica si se debe añadir autenticación a las peticiones Trellis. Valores admisibles `true` y `false` | false                                    |
| `APP_TRELLIS_AUTHENTICATION_USERNAME`     | Usuario Trellis                                                                                               | admin                                    |
| `APP_TRELLIS_AUTHENTICATION_PASSWORD`     | Contraseña Trellis                                                                                            | admin                                    |
| `APP_WIKIBASE_ENABLED`                    | Flag que indica si se debe persistir en Wikibase. Valores admisibles `true` y `false`                         | false                                    |
| `APP_WIKIBASE_API_URL`                    | URL del servicio del API de Wikibase                                                                          | http://localhost:8181/api.php            |
| `APP_WIKIBASE_API_USERNAME`               | Usuario en Wikibase                                                                                           | WikibaseAdmin                            |
| `APP_WIKIBASE_API_PASSWORD`               | Contraseña para usuario en Wikibase                                                                           | WikibaseDockerAdminPass                  |
| `APP_WIKIBASE_API_QUERY_DEFAULT_LANGUAGE` | Lenguaje por defecto en Wikibase                                                                              | es                                       |
| `APP_WIKIBASE_API_SITE_URI`               | URL del servicio de entidad en Wikibase                                                                       | http://localhost:8181/entity/            |
| `APP_GENERATOR_URIS_ENDPOINT_LINK_URI`    | URL del servicio de uris para enlazar url canónica                                                            | http://localhost:9326/uri-factory/local/ |

### Ejecución

Al generarse un JAR bootable la ejecución se realizará mediante el siguiente comando:

```bash
java -jar {jar-name}.jar
```

Sustituyendo `{jar-name}` por el nombre del fichero JAR generado.

No es necesario especificar la clase de inicio de la aplicación, ya que el fichero MANIFEST.MF generado ya contiene la información necesaria. Solamente se especificarán los parametros necesarios.

## Testing y cobertura

Se incluyen los resultados del testing y cobertura en los siguientes enlaces:

- [Testing](http://herc-iz-front-desa.atica.um.es:8070/triples-storage-adapter/surefire/surefire-report.html)
- [Cobertura](http://herc-iz-front-desa.atica.um.es:8070/triples-storage-adapter/jacoco/)

## Documentación adicional

- [Compilación](docs/build.md)
- [Generación Docker](docs/docker.md)
