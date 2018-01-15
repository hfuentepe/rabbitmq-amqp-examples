package es.hfuentepe.amqp.examples.pubsub;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Clase que contiene la definicion de los beans para el ejemplo de Publish/Subscribe de integración de RabbitMQ con
 * Spring AMQP. Se define la configuración de la prueba destacando que en este ejemplo se introduce un nuevo elementos,
 * los Exchanges.
 * 
 * Un intercambiador (Exchange) es algo muy simple, recibe un mensaje del productor y se encarga de entregarlo a una/s
 * cola/s o descartarlo. Las reglas de como realizarlo las define el tipo de intercambiador (direct, topic, headers o
 * fanout).
 * 
 * En este caso el FanOut lo que hace es entregar el mensaje a todas las colas que conoce. En este caso de definen 2
 * colas de tipo AutoDelete o Anonimas y dos Bindings o enlaces para vincular estas colas al Exchange.
 * 
 * Indicar que en los tutoriales anteriores si utilizabamos Exchange aunque no haciamos ninguna referencia a los mismo.
 * Esto es debido a que existe un intercambiador por defecto que se identifica con la cadena vacia.
 * 
 * Como se puede observar utilizamos un tipo de cola llamado AnonymousQueue, estas colas son colas no durareras,
 * exclusivas y con un nombre autogenerado. Esto que significa? En primer lugar no permite tener una cola nueva y vacia
 * cada vez que nos conectamos a RabbitMQ y cada vez que nos desconectamos se elimina automaticamente.
 * 
 * Los Bindings definen la relacion entre un Exchange y una Cola, podriamos definirlo como un enlace, en este caso
 * tenemos dos colas y dos bindings asociados al Exchange.
 * 
 * @author hfuentepe
 *
 */
@Profile({ "tut3", "pub-sub", "publish-subscribe" })
@Configuration
public class PubSubConfig {

    @Bean
    public FanoutExchange fanout() {
	return new FanoutExchange("tut.fanout");
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
	public Binding binding1(FanoutExchange fanout, Queue autoDeleteQueue1) {
	    return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
	}

	@Bean
	public Binding binding2(FanoutExchange fanout, Queue autoDeleteQueue2) {
	    return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
	}

	@Bean
	public PubSubReceiver receiver() {
	    return new PubSubReceiver();
	}
    }

    @Profile("sender")
    @Bean
    public PubSubSender sender() {
	return new PubSubSender();
    }

}
