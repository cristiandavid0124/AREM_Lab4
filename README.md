# Taller 4 - Taller de de modularización con virtualización e Introducción a Docker

## Diseño de Clases

Los principales componentes de la aplicación son:

+ `App`: Clase principal que inicia la aplicación web, la cual gestiona solicitudes GET en el endpoint */log* obteniendo las 10 últimas cadenas almacenadas en la base de datos y las retorna en formato JSON.
+ `RRInvoker`: Se encarga de invocar un servicio web para registrar mensajes de registro en varios servidores. La clase utiliza conexiones HTTP para enviar solicitudes GET a servidores específicos, cada uno identificado por una URL diferente dentro de un arreglo. Cuando se llama al método *invoke(String logmsg)*, se construye la URL del servidor correspondiente con el mensaje de registro proporcionado. Luego, se realiza una solicitud GET a esa URL, se procesa la respuesta y se devuelve como una cadena de texto. Además, la clase implementa un mecanismo de rotación entre los servidores disponibles para distribuir la carga, cambiando de servidor después de cada solicitud.
+ `LogService`: Servicio REST que recibe una cadena, la almacena en la base de datos y responde en un objeto JSON con las 10 ultimas cadenas almacenadas en la base de datos y la fecha en que fueron almacenadas.
+ `MongoUtil`: Proporciona métodos para interactuar con una base de datos MongoDB. La clase se encarga de establecer conexión con la base de datos, agregar registros y recuperar los últimos 10 registros guardados.

## Arquitectura

El objetivo del taller es implementar la siguiente arquitectura en AWS utilizando contenedores de Docker y servicios de AWS:


Se realiza la configuración correspondiente en el archivo `docker-compose.yml` este archivo define una infraestructura compuesta por varios servicios interconectados que trabajan juntos para proporcionar una aplicación web que registra logs en una base de datos MongoDB, utilizando contenedores Docker para la fácil gestión y despliegue de los componentes. Se definen los siguientes servicios:

+ `logservice1, logservice2, logservice3`: Estos servicios representan instancias de un servicio de registro de logs, cada uno configurado para ejecutarse en su propio contenedor Docker. Cada contenedor se basa en la imagen `....`, que contiene la lógica para registrar logs y está expuesta en el puerto 35000. Cada servicio se mapea a un puerto diferente en el host, permitiendo acceder a ellos individualmente.

+ `mongodb`: Este servicio configura un contenedor para ejecutar una instancia de MongoDB, una base de datos NoSQL. Se utiliza la imagen oficial de MongoDB (mongo:3.6.1) y se expone en el puerto 27017 para permitir la comunicación con la base de datos desde el host. Además, se define un volumen llamado mongodb para persistir los datos de la base de datos y otro volumen mongodb_config para persistir la configuración.

+ `webapp`: Este servicio representa una aplicación web que depende de los servicios de `log` y la base de datos `MongoDB`. Se basa en la imagen angiemojica/arep-lab06-app y se expone en el puerto 38000. Este contenedor se conecta a la misma red `mynetarep` que los servicios de log y la base de datos y depende de ellos para funcionar correctamente. La instrucción depends_on asegura que los contenedores de log y MongoDB se inicien antes de iniciar este servicio.

## Generación de las imagen de la aplicación

Se han generado las imágenes de la aplicación y los servicios de log utilizando el comando `docker build` para construir las imágenes a partir de los archivos `Dockerfile` correspondientes. A continuación, se muestra el resumen de comandos para generar las imágenes:

### Imagen de la aplicación

```bash
# Construir la imagen de la aplicación
docker build -f .\Dockerfiles\Dockerfile1 -t dockerapprr .
# Etiquetar la imagen con el nombre de usuario del repositorio de Docker Hub
docker tag dockerapprr cristindavid0124/taller4_arep
# Subir la imagen al repositorio de Docker Hub
docker push cristindavid0124/taller4_arep
```

### Imagen de los servicios de log

```bash
# Construir la imagen de los servicios de log
docker build -f .\Dockerfiles\Dockerfile2 -t dockerlogservice .
# Etiquetar la imagen con el nombre de usuario del repositorio de Docker Hub
docker tag dockerlogservice cristindavid0124/arep-lab04-servicelog
# Subir la imagen al repositorio de Docker Hub
docker push cristindavid0124/arep-lab04-servicelog
```

Los Dockerfile (1 App, 2 Logs) definen las instrucciones para construir las imágenes Docker que ejecutará una aplicación Java. Utiliza OpenJDK 17 como la imagen base, configura el directorio de trabajo y una variable de entorno para el puerto. Luego, copia los archivos de clases y las dependencias de la aplicación al contenedor. Finalmente, especifica el comando java para iniciar la aplicación al iniciar el contenedor.

## Despliegue local

Para desplegar la aplicación en un entorno local, se utiliza Docker Compose para definir y ejecutar la infraestructura de la aplicación. El archivo `docker-compose.yml` define los servicios de la aplicación, las redes y los volúmenes necesarios para que los contenedores se comuniquen entre sí y persistan los datos. A continuación, se muestran los comandos para desplegar la aplicación en un entorno local:

```bash
# Clona el repositorio de la aplicación
git clone https://github.com/cristiandavid0124/AREM_Lab4.git
# Cambia al directorio del repositorio
cd AREP-TALLER-06
# Inicia los contenedores de la aplicación
docker-compose up
```

> [!IMPORTANT]
> Es necesario tener iniciado Docker-Dekstop para poder ejecutar los comandos anteriores.

Una vez que los contenedores se han iniciado, se puede acceder a la aplicación web en la dirección [http://localhost:38000](http://localhost:38000).

## Despliegue en AWS

Se ha creado una instancia EC2 en AWS para desplegar la aplicación. La instancia se ha configurado con una dirección IP pública y se ha habilitado el tráfico de entrada en los puertos 22 y 38000. Además, se ha instalado Docker y Docker Compose en la instancia para poder ejecutar los contenedores de la aplicación y se ha instalado Git para clonar el repositorio de la aplicación que contiene el archivo `docker-compose.yml`.

El resumen de comandos para desplegar la aplicación en la instancia EC2 es el siguiente:

```bash
# Actualizar el sistema
sudo yum update -y
# Instalar Docker
sudo yum install docker -y
# Iniciar el servicio de Docker
sudo service docker start
# Agregar el usuario actual al grupo de Docker
sudo usermod -a -G docker ec2-user
# Instalar Docker Compose
sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
# Clonar el repositorio de la aplicación
git clone https://github.com/cristiandavid0124/AREM_Lab4.git
# Cambiar al directorio del repositorio
cd AREM_Lab4
# Iniciar los contenedores de la aplicación
docker-compose up
```
Una vez que los contenedores se han iniciado, se puede acceder a la aplicación web en la dirección IP pública de la instancia en el puerto 38000. A continuación, se muestra el resultado de la aplicación web en funcionamiento:
https://github.com/user-attachments/assets/5ac947f9-5525-43e5-806a-d738e747ae1d



## Autor

* **Cristian David Naranjo**

## Agradecimientos

* Al profesor [Luis Daniel Benavides Navarro](https://www.linkedin.com/in/danielbenavides/) por la guía y la enseñanza en el curso de Arquitecturas Empresariales.
