package es.hfuentepe.amqp.examples.workqueues;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Clase que realiza los envios a la cola
 * 
 * @author hfuentepe
 *
 */
public class WorkQueuesSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    int dots = 0;
    int count = 0;

    /**
     * Metodo que realiza el envio de los mensajes. Envia la palabra Hello seguida de puntos (1, 2 o 3). Cada uno de los
     * puntos representa mayor carga de la tarea. Para simular la carga, el receptor leera el numero de puntos y se
     * quedara esperando tantos segundos como puntos tenga el mensaje
     */
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
	template.convertAndSend(queue.getName(), message);
	System.out.println(" [x] Sent '" + message + "'");
    }
}
