package es.hfuentepe.amqp.examples.workqueues;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Clase que contiene la definicion de los beans para el ejemplo de Work Queues de integración de RabbitMQ con Spring
 * AMQP. En este ejemplo se muestra el patron competing consumers
 * (http://www.enterpriseintegrationpatterns.com/patterns/messaging/CompetingConsumers.html)
 * 
 * La idea principal de la colas de trabajo o de tareas es evitar realizar de forma inmediata una tarea que requiera
 * muchos recursos para no esperar hasta que termine. Se encapsula la tarea como un mensaje y se envia a la cola. Un
 * proceso de trabajo ejecutándose en segundo plano abrirá las tareas y eventualmente ejecutará el trabajo. Cuando
 * ejecuta muchos trabajadores, las tareas se compartirán entre ellos.
 * 
 * Este concepto es especialmente útil en aplicaciones web donde es imposible manejar una tarea compleja durante una
 * breve ventana de solicitud HTTP.
 * 
 * @author hfuentepe
 *
 */
@Profile({ "tut2", "work-queues" })
@Configuration
public class WorkQueuesConfig {

    /**
     * Creamos la cola donde se van a enviar los mensajes
     * 
     * @return cola de mensajes
     */
    @Bean
    public Queue hello() {
	return new Queue("hello");
    }

    /**
     * Clase para definir los receptores de los mensajes
     * 
     * En este ejemplo vamos a tener varios receptores entre los que se van a repartir los mensajes que envie el sender
     * 
     * @author hfuentepe
     *
     */
    @Profile("receiver")
    private static class ReceiverConfig {

	@Bean
	public WorkQueuesReceiver receiver1() {
	    return new WorkQueuesReceiver(1);
	}

	@Bean
	public WorkQueuesReceiver receiver2() {
	    return new WorkQueuesReceiver(2);
	}
    }

    /**
     * Enviador de mensajes
     * 
     * @return Enviador de mensajes
     */
    @Profile("sender")
    @Bean
    public WorkQueuesSender sender() {
	return new WorkQueuesSender();
    }

}
