package es.hfuentepe.amqp.examples.helloworld;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Clase que contiene la definicion de los beans para el ejemplo de HelloWorld de integración de RabbitMQ con Spring
 * AMQP
 * 
 * @author hfuentepe
 *
 */
@Profile({ "tut1", "hello-world" })
@Configuration
public class HelloWorldConfig {

    /**
     * Creamos una cola con nombre hello
     * 
     * @return Cola de mensajes
     */
    @Bean
    public Queue hello() {
	return new Queue("hello");
    }

    /**
     * Creamos un receptor de mensajes
     * 
     * @return Receptor de mensajes
     */
    @Profile("receiver")
    @Bean
    public HelloWorldReceiver receiver() {
	return new HelloWorldReceiver();
    }

    /**
     * Creamos un enviador de mensajes
     * 
     * @return Enviador de mensajes
     */
    @Profile("sender")
    @Bean
    public HelloWorldSender sender() {
	return new HelloWorldSender();
    }

}
