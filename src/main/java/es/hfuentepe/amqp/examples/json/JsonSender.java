package es.hfuentepe.amqp.examples.json;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Clase que envia mensaje un mensaje (Clase Mensaje) mediante la rabbitTemplate que tiene como converter un
 * Jackson2JsonMessageConverter para realizar el marshalling. Utilizar un Exchange FanOut que esta asociado a dos colas
 * Anonimas, por tanto los mensajes llegan a dos colas.
 * 
 * @author hfuentepe
 *
 */
public class JsonSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanout;

    int prioridad = 0;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
	prioridad++;
	Mensaje message = new Mensaje("Mensaje enviado con prioridad" + prioridad, prioridad, true);
	rabbitTemplate.convertAndSend(fanout.getName(), "", message);
	System.out.println(" [x] Sent '" + message + "'");
    }

}
