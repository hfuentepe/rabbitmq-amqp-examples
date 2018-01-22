package es.hfuentepe.amqp.examples.json;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Receptor de mensajes que escucha en dos colas. Cada uno de los metodos recibe un tipo diferentes de objeto, aunque en
 * sender a enviado el mismo mensaje a dos colas. El metodo receiveGeneric recibe un objeto generico de Spring-AMQP y en
 * el metodo receiveCustom recibimos un tipo definido en nuestro modelo de dominio. En ambos casos establecemos al
 * RabbitListner una factoria con un converter de tipo Jackson2JsonMessageConverter para realizar el Unmarshalling.
 * 
 * @author hfuentepe
 *
 */
public class JsonReceiver {

    @RabbitListener(queues = "#{autoDeleteQueue1.name}", containerFactory = "jsonFactory")
    public void receiveGeneric(Message message) {
	System.out.println("Mensaje recibido como objeto generico: " + message.toString());
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}", containerFactory = "jsonFactory")
    public void receiveCustom(Mensaje message) throws InterruptedException {
	System.out.println("Mensaje recibido como objeto definido en nuestro negocio: " + message.toString());
    }

}
