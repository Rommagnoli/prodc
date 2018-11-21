package prode;

import java.util.*;

import org.javalite.activejdbc.Model;

import spark.Request;
	
public class Schadule extends Model {
	
	public Schadule() { }

	public void addSchadule(ArrayList<String> req){
		List<Schadule> busqueda = Schadule.findBySQL("select num_fecha from schadules ");
		Integer cantFecha = 0;
		if (busqueda.size() != 0) {
			Schadule actualFecha = busqueda.get(busqueda.size()-1);
			cantFecha = (Integer)actualFecha.get("num_fecha");
		}
		for (int i = 0; i < req.size(); i++) {
			Schadule s = new Schadule();
			String cdp = req.get(i);
			int codp = Integer.parseInt(cdp);
			s.set("cod_partido", codp);
			s.set("num_fecha", (cantFecha+1));
			s.saveIt();
		}
	
	}

	public List<Schadule> listSchadule(){
		List<Schadule> busqueda = Schadule.findBySQL("select distinct num_fecha from schadules ");
		return busqueda;
	}

	public int num_fecha() {
    	return this.getInteger("num_fecha");
    }
	
	public int ultimaFecha() {
		int fecha = 0;
		Schadule f = new Schadule();
		List<Schadule> ultimaFecha = Schadule.findBySQL("Select num_fecha from schadules where cod_partido in(select cod_partido from predictions where id_usuario = 1) ");
		if (ultimaFecha.size() != 0) {
			f = ultimaFecha.get(0);
			fecha = (Integer)f.get("num_fecha");
			return  fecha;
		}
		return fecha;
	}
	
}