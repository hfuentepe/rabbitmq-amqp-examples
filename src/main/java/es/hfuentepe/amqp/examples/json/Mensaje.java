package es.hfuentepe.amqp.examples.json;

import java.io.Serializable;

public class Mensaje implements Serializable {

    private String contenido;
    private int prioridad;
    private boolean flag;

    // Constructor por defecto necesario para deserealización con JSON
    public Mensaje() {

    }

    public Mensaje(String contenido, int prioridad, boolean flag) {
	super();
	this.contenido = contenido;
	this.prioridad = prioridad;
	this.flag = flag;
    }

    public String getContenido() {
	return contenido;
    }

    public void setContenido(String contenido) {
	this.contenido = contenido;
    }

    public int getPrioridad() {
	return prioridad;
    }

    public void setPrioridad(int prioridad) {
	this.prioridad = prioridad;
    }

    public boolean isFlag() {
	return flag;
    }

    public void setFlag(boolean flag) {
	this.flag = flag;
    }

    @Override
    public String toString() {
	return "Mensaje [contenido=" + contenido + ", prioridad=" + prioridad + ", flag=" + flag + "]";
    }

}
