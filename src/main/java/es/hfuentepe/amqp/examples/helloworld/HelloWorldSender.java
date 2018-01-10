package es.hfuentepe.amqp.examples.helloworld;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Clase que envia mensaje a la cola con el contenido de HelloWorld
 * 
 * @author hfuentepe
 *
 */
public class HelloWorldSender {

    /**
     * Template que proporciona Spring para trabajar con RabbitMQ
     */
    @Autowired
    private RabbitTemplate template;

    /**
     * Establecemos la cola a la que envíamos los mensajes
     */
    @Autowired
    private Queue queue;

    /**
     * Metodo que realiza envios a la cola
     */
    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
	String message = "Hello World!";
	System.out.println(" Enviando mensaje:" + message + " a la cola: " + queue.getName());
	this.template.convertAndSend(queue.getName(), message);
	System.out.println(" Enviado mensaje:" + message + " a la cola: " + queue.getName());
    }

}
