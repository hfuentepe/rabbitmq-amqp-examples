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

En el primer ejemplo del tutorial contiene el tipico [Helloworld](https://www.rabbitmq.com/tutorials/tutorial-one-spring-amqp.html). en este caso se va a crear un enviador (producer) que envia un unico mensajes cuyo contenido es HelloWorld y un receptor (consumer) que recibe el mensaje y lo pinta por consola. Este es el [patrón Point-to-Point Channel](http://www.enterpriseintegrationpatterns.com/patterns/messaging/PointToPointChannel.html)i

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

### Ejemplo Publish/Subscribe

Este [ejemplo implemente el patrón FanOut](https://www.rabbitmq.com/tutorials/tutorial-three-spring-amqp.html) tambien conocido como ["publish/subscribe"](http://www.enterpriseintegrationpatterns.com/patterns/messaging/PublishSubscribeChannel.html) para enviar un mensaje a multiples consumidores. Basicamente los mensajes publicados se transmitiran a todos los receptores.

Aunque no lo habiamos comentado hasta ahora, la idea centra del modelo de mensajeria de RabbitMQ es que el productor nunca envia directamente un mensaje a una cola. A menudo el productor ni siquiera sabe si un mensaje sera entregado a un cola, se lo entrega a una Exchange (intercambiador).l

Un intercambiador (Exchange) es algo muy simple, recibe un mensaje del productor y se encarga de entregarlo a una/s cola/s o descartarlo. Las reglas de como realizarlo las define el tipo de intercambiador (direct, topic, headers y fanout).
 
En este caso el FanOut lo que hace es entregar el mensaje a todas las colas que conoce. En este caso de definen 2 colas de tipo AutoDelete o Anonimas y dos Bindings o enlaces para vincular estas colas al Exchange.
 
Indicar que en los tutoriales anteriores si utilizabamos Exchange aunque no haciamos ninguna referencia a los mismo. Esto es debido a que existe un intercambiador por defecto que se identifica con la cadena vacia.
 
Como se puede observar utilizamos un tipo de cola llamado AnonymousQueue, estas colas son colas no durareras, exclusivas y con un nombre autogenerado. Esto que significa? En primer lugar no permite tener una cola nueva y vacia cada vez que nos conectamos a RabbitMQ y cada vez que nos desconectamos se elimina automaticamente.
 
Los Bindings definen la relacion entre un Exchange y una Cola, podriamos definirlo como un enlace, en este caso tenemos dos colas y dos bindings asociados al Exchange.
 
Por otro lado indicar que los Exchange Fanout ignoran el valor de routingKey aunque nosotros lo enviemos. Indicar que si el Exchange no tiene asociadas colas los mensajes se perderan.

Para su ejecucion abrimos dos consolas y ejecutamos los siguiente:

```
# shell 1
java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=pub-sub,receiver --tutorial.client.duration=60000

# shell 2
java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=pub-sub,sender --tutorial.client.duration=60000
```

### Ejemplo Routing

Este ejemplo muestra como enrutar mensajes entre colas mediante la utilización de los [Exchanges de tipo Direct](https://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html).
  
Como vimos en el ejemplo anterior un Binding es la relaciones entre un Exchange y una cola. Tambien nombramos el parametro routingKey y que cada tipo de Exchange lo utiliza de diferentes manera (FanOut lo ignora).
  
En este caso utilizamos un Exchange de tipo Direct. Su algoritmo de enrutamiento es muy sencillo, un mensaje va a a las colas cuya clave de enlace (bindingKey) coincide exactamente con la routingKey del mensaje.
  
En el ejemplo se vinculan varias colas a la misma binding Key. En este caso tanto  autoDeleteQueue1 y autoDeleteQueue2 recibiran los mensajes cuya routingKey sea black, además de otras. Q1 --> orange y black, Q1 --> green y black.
 
¿Donde nos podria ser util? Es posible es posible que queramos gestionar los logs de aplicacion y escribirlos en un fichero, pero escribir en disco los errores críticos y no perder espacio de disco con mensajes de WARNING o INFO, o que envie un notificación en caso de CRITICAL, ...

En cuanto al Productor lo más interesenta es como establecer la Routing Key con la template que proporciona Spring: template.convertAndSend(exchange, routingKey, mensaje).

Para su ejecucion abrimos dos consolas y ejecutamos los siguiente:

```
# shell 1
java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=routing,receiver --tutorial.client.duration=60000

# shell 2
java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=routing,sender --tutorial.client.duration=60000
```

### Ejemplo Topics

Este es un ejemplo de Enrutamiento de mensajes mediante el [Exchange de tipo Topic](https://www.rabbitmq.com/tutorials/tutorial-five-spring-amqp.html). Se define la configuración de la prueba destacando que en este ejemplo se puede verificar el funcionamiento de los Exchanges de tipo Topic.
 
Topic Exchanges proporcionan mayor flexibilidad que los topic Direct que unicamente se basan en el routingKey. Los mensajes enviados a un intercambiador de tipo Topic no pueden tener una clave de enrutamiento arbitraria: debe ser una lista de palabras, delimitada por puntos. Puede ser cualquier palabra, pero generalmente especifican algunas características del mensaje. Algunos ejemplos válidos de claves de enrutamiento: "stock.usd.nyse", "nyse.vmw", "quick.orange.rabbit". Puede haber tantas palabras en la clave de enrutamiento como desee, hasta el límite de 255 bytes.

La lógica detrás de Topic es similar Direct: un mensaje enviado con una clave de enrutamiento particular se entregará a todas las colas que están vinculadas con una clave de enlace coincidente. Sin embargo, hay dos casos especiales para las claves vinculantes: 
- "*" (asterisco) puede sustituir exactamente una palabra. 
- "#" (almoadilla) puede sustituir a cero o más palabras.

Cuando una cola está enlazada con un bindingKey "#", recibirá todos los mensajes, independientemente de la clave de enrutamiento, como si fuese un FanOut Exchange.
 
Cuando los caracteres especiales "*" y "#" no se utilizan en enlaces, el intercambio de temas se comportará como un Direct Exchange.

Para su ejecucion abrimos dos consolas y ejecutamos los siguiente:

```
# shell 1
java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=topics,receiver --tutorial.client.duration=60000

# shell 2
java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=topics,sender --tutorial.client.duration=60000
```

### Ejemplo RPC

Ejemplo de implementación del [patrón Request/reply](http://www.enterpriseintegrationpatterns.com/patterns/messaging/RequestReply.html) con RabbitMQ y Spring AMQP. Un sistema RPC tiene un cliente y servidor que se encarga de procesar las peticiones y enviar la confirmación a una cola de CallBack.

RPC es un patrón bastante común pero no siempre es la mejor solución. Debe ser obvio que la función a la que llamamos es local o remota. Es importante documentar bien el sistema y dejar claras las dependencias entre componentes. ¿Comó debe reaccionar el cliente cuando el servidor RPC esta inactivo durante un tiempo prolongado? Si no lo tiene claro, evite RPC. Puede que la mejor solución sea una comunicación asincóna en lugar de un bloqueo RPC.

RPC sobre RabbitMQ es muy sencillo. Un cliente envía un mensaje de solicitud y un servidor responde con un mensaje de respuesta. Para recibir una respuesta, necesitamos enviar una dirección de cola de "callback" con la solicitud. RabbitTemplate maneja la cola de devolución de llamada cuando usamos el método 'convertSendAndReceive ()'. No es necesario realizar ninguna otra configuración al usar RabbitTemplate. [Más Info](https://docs.spring.io/spring-amqp/reference/htmlsingle/#request-reply).

En este caso, Spring-amqp es especialmente util al abstraernos de muchos de los detalles de la implementación del RPC, por ejemplo, normalmente el cliente RPC tendria que crear un cola de callback por cada solicitud RPC. Esto es bastante ineficiente y lo mejor es crear una cola por cliente. Esto nos lleva a otro problema, ¿cada respuesta a que petición corresponde? Aquí es donde entra en acción la propiedad correlationId que se asigna a cada una de las mensajes y que Spring-AMQP gestiona para nosotros tanto en el envío como en la gestión de las respuestas.A

Para su ejecucion abrimos dos consolas y ejecutamos los siguiente:

```
# shell 1
java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=rpc,server --tutorial.client.duration=60000

# shell 2
java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=rpc,client --tutorial.client.duration=60000
```


El diseño que se ha visto en este ejemplo no es la unica implementación posible para RPC, pero presenta alguna ventajas:

- Si el servidor RPC es demasiado lento, puede escalar ejecutando otro. Intente ejecutar un segundo RPCServer en una nueva consola.
- En el lado del cliente, el RPC requiere enviar y recibir solo un mensaje con un método. No se requieren llamadas sincrónicas como queueDeclare. Como resultado, el cliente de RPC solo necesita un viaje de ida y vuelta de red para una única solicitud de RPC.

Nuestro código sigue es muy sencillo y no trata de resolver problemas más complejos (pero importantes), como:

- ¿Cómo debería reaccionar el cliente si no hay servidores en funcionamiento?
- ¿Debe un cliente tener algún tipo de tiempo de espera para el RPC?
- Si el servidor no funciona correctamente y genera una excepción, ¿se debe reenviar al cliente?
- Protección contra mensajes entrantes no válidos (por ejemplo, comprobación de límites) antes del procesamiento.


## Configuración

Es conveniente establecer un tiempo de duración a nuestra aplicación a la hora de ejecutar los enviadores y receptores, por defecto es 10000, que esta configurado en application.yml. Para establecerlo esta la propiedad tutorial.client.duration (milisegundos):

```
java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=rpc,server --tutorial.client.duration=60000
```

Si no especificamos donde se encuentra nuestro broker RabbitMQ, Spring AMQP entiende que se encuentra en localhost y el puerto 5672 (por defecto). Si establecemos el perfil remote (--spring.profiles.active=remote), Spring carga las propiedades del fichero application-remote.yml. Para utilizar un instalación remota de RabbitMQ establezca las siguientes propiedades:

```
spring:
  rabbitmq:
    host: <rabbitmq-server>
    username: <tutorial-user>
    password: <tutorial-user>
```

Para hacer las pruebas debe establecer sus propiedades en el fichero application-remote.yml y ejecutar los ejemplos especificando el permite remote. Para más información consulte la documentación de de [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) y [Spring AMQP](https://docs.spring.io/spring-amqp/reference/html/). 


## Notas

En este apartado he documentado algunos temas que me han parecido interesante conocer y que han aparecido durante el desarrollo de los ejemplos:

### Propiedades de los mensajes

El protocolo AMQP 0-9-1 predefine un conjunto de 14 propiedades que acompañan a un mensaje. La mayoría de las propiedades rara vez se utilizan, con la excepción de las siguientes:

- deliveryMode: marca un mensaje como persistente (con un valor de 2) o transitorio (cualquier otro valor).
- contentType: se usa para describir el tipo mime de la codificación. Por ejemplo, para la codificación JSON de uso frecuente, es una buena práctica establecer esta propiedad en: application / json.
- replyTo: comúnmente utilizado para nombrar una cola de devolución de llamada.
- correlationId: útil para correlacionar las respuestas RPC con las solicitudes.


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
| durable        | true       | Establece declareExchange a este valor                                                     |
| deliveryMode | PERSISTENT |Se puede establecer PERSISENT o NON_PERSISTENT para que RabbitMQ persista o no los mensajes |

[Nota]

Marcar mensajes como persistentes no garantiza plenamente que un mensaje no se perderá. Aunque se dice a RabbitMQ que guarde el mensaje en el disco, todavía existe una pequeña ventana de tiempo, en la cual RabbitMQ aceptada un mensaje pero no lo ha guardado. Además, RabbitMQ no hace fsync (2) para cada mensaje, puede guardarlo en caché y no escribirlo realmente en el disco. Las garantías de persistencia no son fuertes, pero estos ejemplos es suficiente. Si necesita una garantía más sólida, puede usar [Publisher Confirms](https://www.rabbitmq.com/confirms.html).

### Fair Disptch vs. Round-robin dispatching

Por defecto, RabbitMQ envia los mensajes en secuencia mediante Round-Robin. En una situación con dos receptores, cuando todos los mensajes impares son tareas costosas un trabajador esta constantemente ocupado y el otro con apenas trabajo. RabbitMQ no sabra nada al respecto y seguira enviando de manera uniforme. Esto es debido a RabbitMQ envia el mensaje en cuanto entra en la cola sin mirar la cantidad de mensajes no reconocidos para un consumidor. Simplemente envía  cada n-ésimo mensaje al n-ésimo consumidor.

Sin embargo, "Fair dispatch" es la configuración predeterminada para Spring-AMQP. SimpleMessageListenerContainer define el valor para que DEFAULT_PREFETCH_COUNT sea 1. Si el DEFAULT_PREFECTH_COUNT se configuró en 0, el comportamiento sería un intercambio de mensajes como se describió anteriormente.

Sin embargo, con prefetchCount establecido en 1 de manera predeterminada, esto le dice a RabbitMQ que no debe dar más de un mensaje a un trabajador a la vez. O, en otras palabras, no envíe un nuevo mensaje a un trabajador hasta que haya procesado y reconocido el anterior. En cambio, lo enviará al siguiente trabajador que aún no está ocupado.

