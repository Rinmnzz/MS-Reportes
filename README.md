GUÍA RÁPIDA: MS-REPORTES (SANOS Y SALVOS)
========================================

Este microservicio gestiona reportes de mascotas, calcula coordenadas 
automáticamente y notifica eventos a través de RabbitMQ.

1. REQUISITOS PREVIOS
---------------------
- Tener Docker y Docker Compose instalados.
- El archivo 'docker-compose.yml' y el 'Dockerfile' deben estar en la raíz.

2. COMANDOS DE DOCKER (OPERACIÓN)
---------------------------------

> INICIAR TODO EL SISTEMA:
  (Levanta Base de Datos, RabbitMQ y la API)
  $ docker-compose up -d

> INICIAR Y RECONSTRUIR:
  (Úsalo si hiciste cambios en el código)
  $ docker-compose up -d --build

> VER LOGS DEL MICROSERVICIO:
  (Para ver qué está pasando o revisar errores)
  $ docker-compose logs -f reporte-api

> DETENER EL SISTEMA:
  (Apaga los contenedores pero mantiene los datos)
  $ docker-compose stop

> BORRAR TODO EL SISTEMA:
  (Detiene y elimina contenedores y redes)
  $ docker-compose down

3. CÓMO PROBAR EL SERVICIO (URLS)
---------------------------------
- API de Reportes: http://localhost:8080/reportes
- Panel de RabbitMQ: http://localhost:15672 (User: guest / Pass: guest)
- Base de Datos: localhost:5432 (DB: coincidencias)

4. EJEMPLO RÁPIDO DE USO (POST)
-------------------------------
Para crear un reporte, envía un JSON a http://localhost:8080/reportes con:
{
    "idUsuario": 1,
    "tipoReporte": "PERDIDO",
    "tipoMascota": "Perro",
    "nombreMascota": "Tobyyyy",
    "color": "Negro",
    "tamano": "MEDIANO",
    "raza": "Quiltro",
    "descripcion": "Se asustó con los fuegos artificiales",
    "direccion": "Avenida Providencia 1234, Providencia, Santiago",
    "estado": "ACTIVO",
    "sexo": "HEMBRA",
    "fotoMascota": "http://miweb.com/foto.jpg"
}



devuelve esto: 

{
    "color": "Negro",
    "coordenadas": "-33.4290115,-70.6211027",
    "descripcion": "Se asustó con los fuegos artificiales",
    "direccion": "Avenida Providencia 1234, Providencia, Santiago",
    "estado": "ACTIVO",
    "fotoMascota": "http://miweb.com/foto.jpg",
    "id": 2,
    "idUsuario": 1,
    "nombreMascota": "Tobyyyy",
    "raza": "Quiltro",
    "sexo": "HEMBRA",
    "tamano": "MEDIANO",
    "tipoMascota": "Perro",
    "tipoReporte": "PERDIDO"
}

