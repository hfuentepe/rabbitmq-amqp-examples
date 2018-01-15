package es.hfuentepe.amqp.examples.routing;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Clase que envia mensajes a un Exchange de tipo Direct.
 * 
 * En este caso, lo más interesantes es como se envia la Routing Key con la template que proporciona Spring
 * 
 * template.convertAndSend(exchange, routingKey, mensaje)
 * 
 * @author hfuentepe
 *
 */
public class RoutingSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    private int index;

    private int count;

    private final String[] keys = { "orange", "black", "green" };

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
	StringBuilder builder = new StringBuilder("Hello to ");
	if (++this.index == 3) {
	    this.index = 0;
	}
	String key = keys[this.index];
	builder.append(key).append(' ');
	builder.append(Integer.toString(++this.count));
	String message = builder.toString();
	template.convertAndSend(direct.getName(), key, message);
	System.out.println(" [x] Sent '" + message + "'");
    }
}
