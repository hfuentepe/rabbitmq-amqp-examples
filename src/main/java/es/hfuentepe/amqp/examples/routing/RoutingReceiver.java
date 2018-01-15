package es.hfuentepe.amqp.examples.routing;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

/**
 * 
 * Clase receptora de mensajes, en este caso se definen dos metodos receptores asociado a cada una de las colas
 * definidas. Al no conocer su nombre debido a que son Anonimas lo debe obtener de los beans.
 * 
 * Es similar a las anterior, el receptor se abstrae completamente del enrutamiento simplemente subscribe a una
 * determinada cola.
 * 
 * @author hfuentepe
 *
 */
public class RoutingReceiver {

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receive1(String in) throws InterruptedException {
	receive(in, 1);
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receive2(String in) throws InterruptedException {
	receive(in, 2);
    }

    public void receive(String in, int receiver) throws InterruptedException {
	StopWatch watch = new StopWatch();
	watch.start();
	System.out.println("instance " + receiver + " [x] Received '" + in + "'");
	doWork(in);
	watch.stop();
	System.out.println("instance " + receiver + " [x] Done in " + watch.getTotalTimeSeconds() + "s");
    }

    private void doWork(String in) throws InterruptedException {
	for (char ch : in.toCharArray()) {
	    if (ch == '.') {
		Thread.sleep(1000);
	    }
	}
    }

}
