package es.hfuentepe.amqp.examples.rpc;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Clase que contiene la definicion de los beans para el ejemplo de implementación del patron RPC (Remote Procedure) con
 * RabbitMQ y Spring AMQP. En este caso se cambian los nombre de los perfiles de Client y Server.
 * 
 * El cliente tiene definido un exchange de tipo Direct que envia a la cola tut.rpc que sera la misma que escuche el
 * servidor.
 * 
 * El servidor recibira todos los mensajes con la routingKey rpc. Como se puede observar no especificamos la Cola de
 * "CallBack", de esto se encarga Spring cuando utilizamos el metodo converSendAndReceive.
 * 
 * @author hfuentepe
 *
 */
@Profile({ "Rpc", "rpc" })
@Configuration
public class RpcConfig {

    @Profile("client")
    private static class ClientConfig {

	@Bean
	public DirectExchange exchange() {
	    return new DirectExchange("tut.rpc");
	}

	@Bean
	public RpcCliente client() {
	    return new RpcCliente();
	}

    }

    @Profile("server")
    private static class ServerConfig {

	@Bean
	public Queue queue() {
	    return new Queue("tut.rpc.requests");
	}

	@Bean
	public DirectExchange exchange() {
	    return new DirectExchange("tut.rpc");
	}

	@Bean
	public Binding binding(DirectExchange exchange, Queue queue) {
	    return BindingBuilder.bind(queue).to(exchange).with("rpc");
	}

	@Bean
	public RpcServidor server() {
	    return new RpcServidor();
	}

    }
}
