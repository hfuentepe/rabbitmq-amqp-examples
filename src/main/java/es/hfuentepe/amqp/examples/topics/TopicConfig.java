package es.hfuentepe.amqp.examples.topics;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Clase que contiene la definicion de los beans para el ejemplo de Enrutamiento de mensajes mediante el Exchange de
 * tipo Topic en la integración de RabbitMQ con Spring AMQP. Se define la configuración de la prueba destacando que en
 * este ejemplo se puede verificar el funcionamiento de los Exchanges de tipo Topic.
 * 
 * Topic Exchanges proporcionan mayor flexibilidad que los topic Direct que unicamente se basan en el routingKey. Los
 * mensajes enviados a un intercambiador de tipo Topic no pueden tener una clave de enrutamiento arbitraria: debe ser
 * una lista de palabras, delimitada por puntos. Puede ser cualquier palabra, pero generalmente especifican algunas
 * características del mensaje. Algunos ejemplos válidos de claves de enrutamiento: "stock.usd.nyse", "nyse.vmw",
 * "quick.orange.rabbit". Puede haber tantas palabras en la clave de enrutamiento como desee, hasta el límite de 255
 * bytes.
 * 
 * La lógica detrás de Topic es similar Direct: un mensaje enviado con una clave de enrutamiento particular se entregará
 * a todas las colas que están vinculadas con una clave de enlace coincidente. Sin embargo, hay dos casos especiales
 * para las claves vinculantes: (*) (asterisco) puede sustituir exactamente una palabra. (#) (almoadilla) puede
 * sustituir a cero o más palabras.
 * 
 * Cuando una cola está enlazada con un bindingKey "#", recibirá todos los mensajes, independientemente de la clave de
 * enrutamiento, como si fuese un FanOut Exchange.
 * 
 * Cuando los caracteres especiales "*" y "#" no se utilizan en enlaces, el intercambio de temas se comportará como un
 * Direct Exchange.
 * 
 * @author hfuentepe
 *
 */
@Profile({ "tut5", "topics" })
@Configuration
public class TopicConfig {

    @Bean
    public TopicExchange topic() {
	return new TopicExchange("tut.topic");
    }

    @Profile("receiver")
    private static class ReceiverConfig {

	@Bean
	public TopicReceiver receiver() {
	    return new TopicReceiver();
	}

	@Bean
	public Queue autoDeleteQueue1() {
	    return new AnonymousQueue();
	}

	@Bean
	public Queue autoDeleteQueue2() {
	    return new AnonymousQueue();
	}

	@Bean
	public Binding binding1a(TopicExchange topic, Queue autoDeleteQueue1) {
	    return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.orange.*");
	}

	@Bean
	public Binding binding1b(TopicExchange topic, Queue autoDeleteQueue1) {
	    return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.*.rabbit");
	}

	@Bean
	public Binding binding2a(TopicExchange topic, Queue autoDeleteQueue2) {
	    return BindingBuilder.bind(autoDeleteQueue2).to(topic).with("lazy.#");
	}

    }

    @Profile("sender")
    @Bean
    public TopicSender sender() {
	return new TopicSender();
    }
}
