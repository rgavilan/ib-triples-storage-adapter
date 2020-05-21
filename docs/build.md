# Compilación

Se indicará a continuación los pasos que hay que seguir para llevar a cabo la generación del artefacto.

## Prerrequisitos

Se precisa disponer los siguientes elementos configurados:

* OpenJDK 11
* Maven 3.6.x

## Compilación

Para realizar la compilación se ejecutará el siguiente comando:

```bash
mvn clean package
```

En caso de querer generar al mismo tiempo JavaDoc y Sources el comando siguiente: 

```bash
mvn clean package javadoc:jar source:jar
```

También sería posible instalar o desplegar los artefactos sustituyendo `package` por `install` o `deploy` respectivamente.

Los artefactos se generarán dentro del directorio `target` de cada uno de los módulos:

### Back

Los artefactos se encuentran dentro de triples-storage-adapter-back/target

* Artefacto: triples-storage-adapter-back-{version}.jar
* JavaDoc: triples-storage-adapter-back-{version}-javadoc.jar
* Sources: triples-storage-adapter-back-{version}-sources.jar

### Service

Los artefactos se encuentran dentro de triples-storage-adapter-service/target

* Artefacto: triples-storage-adapter-service-{version}.jar
* JavaDoc: triples-storage-adapter-service-{version}-javadoc.jar
* Sources: triples-storage-adapter-service-{version}-sources.jar

### Service Abstractions

Los artefactos se encuentran dentro de triples-storage-adapter-service-abstractions/target

* Artefacto: triples-storage-adapter-service-abstractions-{version}.jar
* JavaDoc: triples-storage-adapter-service-abstractions-{version}-javadoc.jar
* Sources: triples-storage-adapter-service-abstractions-{version}-sources.jar

### Swagger

Los artefactos se encuentran dentro de triples-storage-adapter-swagger/target

* Artefacto: triples-storage-adapter-swagger-{version}.jar
* JavaDoc: triples-storage-adapter-swagger-{version}-javadoc.jar
* Sources: triples-storage-adapter-swagger-{version}-sources.jar

### Audit

Los artefactos se encuentran dentro de triples-storage-adapter-audit/target

* Artefacto: triples-storage-adapter-audit-{version}.jar
* JavaDoc: triples-storage-adapter-audit-{version}-javadoc.jar
* Sources: triples-storage-adapter-audit-{version}-sources.jar

