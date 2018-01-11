package es.hfuentepe.amqp.examples.workqueues;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

/**
 * Receptor de mensajes de la cola Hello
 * 
 * @author hfuentepe
 *
 */
@RabbitListener(queues = "hello")
public class WorkQueuesReceiver {

    private final int instance;

    /**
     * Constructor de un receptor
     * 
     * @param i
     *            Identificador del receptor
     */
    public WorkQueuesReceiver(int i) {
	this.instance = i;
    }

    /**
     * Recibe mensajes de la cola hello y espera tantos segundos como puntos tenga el mensaje
     * 
     * @param mensaje
     *            Mensaje recibido
     * @throws InterruptedException
     */
    @RabbitHandler
    public void receive(String mensaje) throws InterruptedException {
	StopWatch watch = new StopWatch();
	watch.start();
	System.out.println("instance " + this.instance + " [x] Received '" + mensaje + "'");
	doWork(mensaje);
	watch.stop();
	System.out.println("instance " + this.instance + " [x] Done in " + watch.getTotalTimeSeconds() + "s");
    }

    private void doWork(String in) throws InterruptedException {
	for (char ch : in.toCharArray()) {
	    if (ch == '.') {
		Thread.sleep(1000);
	    }
	}
    }

}
