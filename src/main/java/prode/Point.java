package prode;

import java.util.*;
import org.javalite.activejdbc.Base;
import static spark.Spark.*;
import spark.*;
import org.javalite.activejdbc.*;

public class Point extends Model {
	
	
	public Point() {	}
	
	public void crearPoint(int idUser, int idFecha) {
		Point p = new Point();
		p.set("idUser", idUser);
		p.set("idFecha", idFecha);
		p.saveIt();
	}
	
	public int puntajeTotal(int idUser) {
		int suma = 0;
		Point p = new Point();
	    List<Point> sum = Point.findBySQL("SELECT * from points WHERE idUser = '"+idUser+"'");
	    for (int i = 0; i<sum.size();i++) {
	    	 p = sum.get(i);
	         suma = suma + (Integer)p.get("puntajeActual"); 
	    }
		return suma;
	}
	
	public int puntajeTotal() {
    	return this.getInteger("puntajeTotal");
	}
}
