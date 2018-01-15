package es.hfuentepe.amqp.examples.routing;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Clase que contiene la definicion de los beans para el ejemplo de Enrutamiento de mensajes en la integración de
 * RabbitMQ con Spring AMQP. Se define la configuración de la prueba destacando que en este ejemplo se puede verificar
 * el funcionamiento de los Exchanges de tipo Direct.
 * 
 * Como vimos en el ejemplo anterior un Binding es la relaciones entre un Exchange y una cola. Tambien nombramos el
 * parametro routingKey y que tipo de Exchange lo utiliza de diferentes manera (FanOut lo ignora)
 * 
 * En este caso como hemos dicho vamos a utilizar un Exchange de tipo Direct. Su algoritmo de enrutamiento es muy
 * sencillo, un mensaje va a a las colas cuya clave de enlace (bindingKey) coincide exactamente con la routingKey del
 * mensaje.
 * 
 * Como podemos ver en el ejemplo, se pueden vincular varias colas a la misma binding Key. En este caso tanto
 * autoDeleteQueue1 y autoDeleteQueue2 recibiran los mensajes cuya routingKey sea black, además de otras. Q1 --> orange
 * y black, Q1 --> green y black.
 * 
 * Este es un caso de ejemplo, pero en el mundo real es posible es posible que queramos gestionar los logs de aplicacion
 * y los escribe en un fichero y solo reciba errores críticos y no pierda espacio en el disco con mensajes de WARNING o
 * INFO. O que envie un notificación en caso de CRITICAL
 * 
 * @author hfuentepe
 *
 */
@Profile({ "tut4", "routing" })
@Configuration
public class RoutingConfig {

    @Bean
    public DirectExchange direct() {
	return new DirectExchange("tut.direct");
    }

    @Profile("receiver")
    private static class ReceiverConfig {

	@Bean
	public Queue autoDeleteQueue1() {
	    return new AnonymousQueue();
	}

	@Bean
	public Queue autoDeleteQueue2() {
	    return new AnonymousQueue();
	}

	@Bean
	public Binding binding1a(DirectExchange direct, Queue autoDeleteQueue1) {
	    return BindingBuilder.bind(autoDeleteQueue1).to(direct).with("orange");
	}

	@Bean
	public Binding binding1b(DirectExchange direct, Queue autoDeleteQueue1) {
	    return BindingBuilder.bind(autoDeleteQueue1).to(direct).with("black");
	}

	@Bean
	public Binding binding2a(DirectExchange direct, Queue autoDeleteQueue2) {
	    return BindingBuilder.bind(autoDeleteQueue2).to(direct).with("green");
	}

	@Bean
	public Binding binding2b(DirectExchange direct, Queue autoDeleteQueue2) {
	    return BindingBuilder.bind(autoDeleteQueue2).to(direct).with("black");
	}

	@Bean
	public RoutingReceiver receiver() {
	    return new RoutingReceiver();
	}
    }

    @Profile("sender")
    @Bean
    public RoutingSender sender() {
	return new RoutingSender();
    }
}
