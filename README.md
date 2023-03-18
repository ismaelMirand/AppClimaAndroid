#  Tarea 1 - Programación de Móviles

## WeatherApp: Aplicación Móvil con Web Services
Se requiere que construya una aplicación móvil Android Nativa, para el despliegue de información referente al clima de una determinada ciudad. Estos datos corresponden a información climatológica actual y de pronóstico para varios días. Los datos son proporcionados por Open Weather, por lo que el objetivo es crear una aplicación que nos permita consultar y visualizar información climatológica en tiempo real.

Para este propósito, se dispone de una API Open Weather a la cuál se deberá realizar consultas, obtener los datos en formato JSON, procesarlos, y mostrarlos en la App. La URL base de los endpoints a utilizar es:

<https://api.openweathermap.org/data/2.5>

Usted podrá utilizar los siguientes endpoints para realizar consultas climatológicas:

1. > weather?q={cityName}&appid={apiKey}

    Este endpoint provee la información climatológica actual. El string *cityName* es el nombre de la ciudad cuya información será consultada.
    Ejemplo de uso:

    <https://api.openweathermap.org/data/2.5/weather?q=Santiago,CL&appid=c7d28225f2d5ea29d5c3ada03438af7d>

2.  > onecall?lat={latitude}&lon={longitude}&exclude={exclude}&appid={apiKey}&units={unit}

    Este endpoint es más general y provee mucha más información. Se debe usar la latitud y longitud de la ciudad a consultar.
    Ejemplo de uso:

    <https://api.openweathermap.org/data/2.5/onecall?lat=-33.045&lon=-71.4494&exclude=minutely,hourly&appid=c7d28225f2d5ea29d5c3ada03438af7d&units=metric>

Se solicita que su aplicación permita:

- Desplegar de forma creativa la información climatológica de al menos 3 ciudades. La información climatológica corresponde al tiempo actual y al pronóstico para los próximos días.
- Mostrar imágenes/iconografía para visualizar la información del clima.
- **Puntaje adicional** si la App permite obtener la geolocalización desde el GPS del dispositivo para consultar por la información del clima de la ubicación del usuario.


La aplicación deberá ser desarrollada en Github. Para crear grupos de trabajo y crear los repositorios debe entrar al siguiente link: <https://classroom.github.com/a/g8jK9opf>


- Uso de API de servicios web
- Uso de JSON
- Interfaz Gráfica de la aplicación

## Entrega y Presentación

Para la entrega debe considerar lo siguiente:

- La tarea puede realizarse de forma individual o en grupos de 2 personas.
- Plazo de entrega es el **20 de Mayo de 2022**.
- Se deberá subir el archivo APK a Adecca y el código fuente en Github Classroom.
- Los grupos deberán presentar sus aplicaciones al resto del curso.


