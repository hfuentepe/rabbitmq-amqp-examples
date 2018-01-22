package es.hfuentepe.amqp.examples.json;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Clase que contiene la definicion de los beans para el ejemplo de envío de mensaje json mediante la integración de
 * RabbitMQ con Spring AMQP. En este ejemplo podemos ver que utilizamos la configuracion del ejemplo 3 con dos colas
 * anonimas y Exchange FanOut.
 * 
 * Lo especial de esta configuracion es que para el "sender" vamos a crear un bean de tipo RabbitTemplate y no vamos a
 * utilizar el que crea por defecto SpringBoot. Este RabbitTemplate utilizara un convertidor de mensajes de tipo
 * Jackson2JsonMessageConverter para transformar nuestros objetos a JSON.
 * 
 * En la configuracion del receptor debemos configurar nuestro RabbitListener para que pueda interpretar los mensajes.
 * Para ello creamos un bean de tipo SimpleRabbitListenerContainerFactory en el que establecemos que el convertidor de
 * mensajes es de tipo Jackson2JsonMessageConverter. Este bean lo establecemos como factoria a los RabbitListener del
 * Receiver.
 * 
 * @author hfuentepe
 *
 */
@Profile({ "tut7", "json" })
@Configuration
public class JsonConfig {

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
	public SimpleRabbitListenerContainerFactory jsonFactory(ConnectionFactory connectionFactory,
		SimpleRabbitListenerContainerFactoryConfigurer configurer) {
	    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
	    configurer.configure(factory, connectionFactory);
	    factory.setMessageConverter(new Jackson2JsonMessageConverter());
	    return factory;
	}

	@Bean
	public JsonReceiver receiver() {
	    return new JsonReceiver();
	}
    }

    @Profile("sender")
    private static class SenderConfig {

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
	    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
	    return rabbitTemplate;
	}

	@Bean
	public JsonSender sender() {
	    return new JsonSender();
	}
    }

}
