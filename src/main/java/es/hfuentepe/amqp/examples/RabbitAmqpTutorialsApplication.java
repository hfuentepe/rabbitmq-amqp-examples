package es.hfuentepe.amqp.examples;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RabbitAmqpTutorialsApplication {

    @Profile("usage_message")
    @Bean
    public CommandLineRunner usage() {
	return new CommandLineRunner() {

	    @Override
	    public void run(String... arg0) throws Exception {
		System.out.println("Esta app utiliza Spring Profiles para controlar su comportamiento.\n");
		System.out.println("Estas son las opciones: ");
		System.out.println(
			"java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=hello-world,receiver");
		System.out
			.println("java -jar rabbitmq-amqp-examples-*.jar --spring.profiles.active=hello-world,sender");
	    }
	};
    }

    @Profile("!usage_message")
    @Bean
    public CommandLineRunner tutorial() {
	return new RabbitAmqpTutorialsRunner();
    }

    public static void main(String[] args) throws Exception {
	SpringApplication.run(RabbitAmqpTutorialsApplication.class, args);
    }
}