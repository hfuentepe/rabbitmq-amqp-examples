package es.hfuentepe.amqp.examples.pubsub;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Clase que envia mensajes a un Exchange de tipo Fanout. Lo más interesante es que ya no enviamos mensajes a colas sino
 * a Exchange.
 * 
 * Por otro lado indicar que los Exchange Fanout ignoran el valor de routingKey aunque nosotros lo enviemos.
 * 
 * Indicar que si el Exchange no tiene asociadas colas los mensajes se perderan.
 * 
 * @author hfuentepe
 *
 */
public class PubSubSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private FanoutExchange fanout;

    int dots = 0;

    int count = 0;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
	StringBuilder builder = new StringBuilder("Hello");
	if (dots++ == 3) {
	    dots = 1;
	}
	for (int i = 0; i < dots; i++) {
	    builder.append('.');
	}
	builder.append(Integer.toString(++count));
	String message = builder.toString();
	template.convertAndSend(fanout.getName(), "", message);
	System.out.println(" [x] Sent '" + message + "'");
    }
}
