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

Para su ejecucion abrimos dos consolas y ejecutamos los siguiente:

```
# shell 1
java -jar target/rabbitmq-amqp-examples-*.jar --spring.profiles.active=hello-world,receiver

# shell 2
java -jar target/rabbitmq-amqp-examples-*.jar --spring.profiles.active=hello-world,sender
```

### Ejemplo Work Queues

En este ejemplo de [Work Queues (Task Queues)](https://www.rabbitmq.com/tutorials/tutorial-two-spring-amqp.html) se distribuyen las tareas entre los receptores [The Competing Consumers Pattern](http://www.enterpriseintegrationpatterns.com/patterns/messaging/CompetingConsumers.html)

La idea principal de la colas de trabajo o de tareas es evitar realizar de forma inmediata una tarea que requiera muchos recursos para no esperar hasta que termine. Se encapsula la tarea como un mensaje y se envia a la cola. Un proceso de trabajo ejecutándose en segundo plano abrirá las tareas y eventualmente ejecutará el trabajo. Cuando ejecuta muchos trabajadores, las tareas se compartirán entre ellos.
 
Este concepto es especialmente útil en aplicaciones web donde es imposible manejar una tarea compleja durante una breve ventana de solicitud HTTP.

Para su ejecucion abrimos dos consolas y ejecutamos los siguiente:

```
# shell 1
java -jar target/rabbitmq-amqp-examples-*.jar --spring.profiles.active=work-queues,receiver

# shell 2
java -jar target/rabbitmq-amqp-examples-*.jar --spring.profiles.active=work-queues,sender
```




## Configuracion





### Reconocimiento de Mensajes (Message acknowledgment)

Spring-AMQP por defecto adopta un enfoque conservador para el reconocimiento de mensajes, si el oyente lanza una excepcion el contenedor vuelve a encolar el mensaje 

```
channel.basicReject(deliveryTag, requeue)
```

a menos que se establezca de forma explicita que no se quiere mediente defaultRequeueRejected=false o si el oyende lanza una excepcion de tipo AmqpRejectAndDontRequeueException. En este modo, no es necesario preocuparse por un reconocimiento olvidado ya que el Listener despues de procesar el mensaje realiza channel.basicAck().

Es un error comun olvidar el basicAck y Spring-AMQP ayuda a evitarlo con la configuracion por defecto. Las consecuencias de no realizarlo son serias ya que los mensajes se volveran a entregar en cuento el cliente se cierre y RabbitMQ consumira más y más memoria, ya que no podra liberar ningun mensaje. Para depurar este tipo de errores se puede utilizar rabbitmqct para imprimir el campo messages_unacknowledged:

En Unix:

```
sudo rabbitmqctl list_queues name messages_ready messages_unacknowledged
```
En Windows:

```
rabbitmqctl.bat list_queues name messages_ready messages_unacknowledged
```

### Durabilidad de los mensajes

Spring-AMQP establece por defecto valores que establecen la durabilidad de los mensajes:

| Property     | default    | Descripcion                                                                                |
| ------------ | -----------| -------------------------------------------------------------------------------------------|
| durae        | true       | Establece declareExchange a este valor                                                     |
| deliveryMode | PERSISTENT |Se puede establecer PERSISENT o NON_PERSISTENT para que RabbitMQ persista o no los mensajes |

[Nota]

Marcar mensajes como persistentes no garantiza plenamente que un mensaje no se perderá. Aunque se dice a RabbitMQ que guarde el mensaje en el disco, todavía existe una pequeña ventana de tiempo, en la cual RabbitMQ aceptada un mensaje pero no lo ha guardado. Además, RabbitMQ no hace fsync (2) para cada mensaje, puede guardarlo en caché y no escribirlo realmente en el disco. Las garantías de persistencia no son fuertes, pero estos ejemplos es suficiente. Si necesita una garantía más sólida, puede usar (Publisher Confirms)[https://www.rabbitmq.com/confirms.html].

### Fair Disptch vs. Round-robin dispatching

Por defecto, RabbitMQ envia los mensajes en secuencia mediante Round-Robin. En una situación con dos receptores, cuando todos los mensajes impares son tareas costosas un trabajador esta constantemente ocupado y el otro con apenas trabajo. RabbitMQ no sabra nada al respecto y seguira enviando de manera uniforme. Esto es debido a RabbitMQ envia el mensaje en cuanto entra en la cola sin mirar la cantidad de mensajes no reconocidos para un consumidor. Simplemente envía  cada n-ésimo mensaje al n-ésimo consumidor.

Sin embargo, "Fair dispatch" es la configuración predeterminada para Spring-AMQP. SimpleMessageListenerContainer define el valor para que DEFAULT_PREFETCH_COUNT sea 1. Si el DEFAULT_PREFECTH_COUNT se configuró en 0, el comportamiento sería un intercambio de mensajes como se describió anteriormente.

Sin embargo, con prefetchCount establecido en 1 de manera predeterminada, esto le dice a RabbitMQ que no debe dar más de un mensaje a un trabajador a la vez. O, en otras palabras, no envíe un nuevo mensaje a un trabajador hasta que haya procesado y reconocido el anterior. En cambio, lo enviará al siguiente trabajador que aún no está ocupado.

