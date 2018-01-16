package es.hfuentepe.amqp.examples.topics;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Clase que envia mensajes a un Exchange de tipo Topic.
 * 
 * En este caso, lo más interesantes son las claves de enrutamiento que son más complejas y flexibles que las utilizadas
 * en Direct.
 * 
 * @author hfuentepe
 *
 */
public class TopicSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private TopicExchange topic;

    private int index;

    private int count;

    private final String[] keys = { "quick.orange.rabbit", "lazy.orange.elephant", "quick.orange.fox", "lazy.brown.fox",
	    "lazy.pink.rabbit", "quick.brown.fox" };

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
	StringBuilder builder = new StringBuilder("Hello to ");
	if (++this.index == keys.length) {
	    this.index = 0;
	}
	String key = keys[this.index];
	builder.append(key).append(' ');
	builder.append(Integer.toString(++this.count));
	String message = builder.toString();
	template.convertAndSend(topic.getName(), key, message);
	System.out.println(" [x] Sent '" + message + "'");
    }
}
