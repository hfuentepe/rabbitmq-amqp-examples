package es.hfuentepe.amqp.examples.rpc;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Clase que envia mensajes a un Exchange de tipo Direct.
 * 
 * En este caso, lo más interesantes es que esperamos una respuesta la utilizar el metodoSendAndReceive. Como se puede
 * observar no se especifica nada sobre una cola de callback. Esto es debido a que Spring-AMQP crear una cola de
 * callback por cliente en ver de una cola por llamada que es más ineficiente, y esto es posible gracias a la propiedad
 * correlation Id.
 * 
 * Spring-amqp asigna a cada llamada un valor unico al correlation Id y se encarga de entregar la respuesta de manera
 * correcta.
 * 
 * 
 * 
 * @author hfuentepe
 *
 */
public class RpcCliente {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange exchange;

    int start = 0;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
	System.out.println(" [x] Requesting fib(" + start + ")");
	Integer response = (Integer) template.convertSendAndReceive(exchange.getName(), "rpc", start++);
	System.out.println(" [.] Got '" + response + "'");
    }
}
