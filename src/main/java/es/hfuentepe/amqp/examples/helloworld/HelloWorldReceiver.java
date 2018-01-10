package es.hfuentepe.amqp.examples.helloworld;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Receptor de mensajes de la cola hello
 * 
 * @author hfuentepe
 *
 */
@RabbitListener(queues = "hello")
public class HelloWorldReceiver {

    /**
     * Handler de mensaje de RabbitMQ, este metodo recibe los mensajes enviado a la cola hello
     * 
     * @param message
     *            Mensaje recibido
     */
    @RabbitHandler
    public void receive(String message) {
	System.out.println(" Recibiendo '" + message + "'");
    }

}
