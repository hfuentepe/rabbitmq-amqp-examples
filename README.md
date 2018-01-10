# Tutorial RabbitMQ usando Spring AMQP

Este projecto contiene los [6 ejemplos del tutorial de RabbitMQ][1] usando Spring AMQP.

Es una aplicacion CLI que utiliza perfiles de Spring para controlar el comportamiento. En cada uno de los ejemplos hay 3 clases: Enviador, Receptor y Configuracion.

[1]: https://www.rabbitmq.com/getstarted.html

## Prerequisitos

Se asume que RabbitMQ esta [instalado](http://rabbitmq.com/download.html) y corriendo en 'localhost' y utilizando el puerto estandar ('5672').
En caso de que RabbitMQ se encuentre en otra maquina, puerto o credenciales se deberan realizar algunos ajustes que se pueden consultar en la sección Configuracion.

## Ejecución

Este tutorial utiliza Maven. Para construir ejecute:

```
mvn clean install
```

o

```
mvn package
```

### Ejemplo HelloWorld

En el primer ejemplo del tutorial contiene el tipico [Helloworld](https://www.rabbitmq.com/tutorials/tutorial-one-spring-amqp.html). en este caso se va a crear un enviador (producer) que envia un unico mensajes cuyo contenido es HelloWorld y un receptor (consumer) que recibe el mensaje y lo pinta por consola. 



## Configuracion

