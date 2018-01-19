package es.hfuentepe.amqp.examples.rpc;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * 
 * Servidor RPC. Escucha los mensaje en la cola tut.rpc.requests y retorna un valor. Spring-AMQP se encarga de enviar a
 * la cola de replyTo por nosotros
 * 
 * @author hfuentepe
 *
 */
public class RpcServidor {

    @RabbitListener(queues = "tut.rpc.requests")
    // @SendTo("tut.rpc.replies") used when the client doesn't set replyTo.
    public int fibonacci(int n) {
	System.out.println(" [x] Received request for " + n);
	int result = fib(n);
	System.out.println(" [.] Returned " + result);
	return result;
    }

    public int fib(int n) {
	return n == 0 ? 0 : n == 1 ? 1 : (fib(n - 1) + fib(n - 2));
    }

}
