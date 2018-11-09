package prode;

import java.util.*;
import static spark.Spark.*;
import spark.*;
import org.javalite.activejdbc.*;

public class Prediction extends Model {

	public Prediction(){}

	public Map addPrediction(Request req, int idUser, List<Schadule> listMatch){
		
		String golloc = (String) req.queryParams("elocal1");
		String golvis = (String) req.queryParams("evisit1");
		
		Integer golLocal = Integer.parseInt(golloc);
		Integer golVisit = Integer.parseInt(golvis);
		Schadule a = listMatch.get(0);
		Integer b = (Integer)a.get("cod_partido");
		
		Map pred = new HashMap();
		Prediction p = new Prediction();
		p.set("equipoL", golLocal);
		p.set("equipoV", golVisit);
		p.set("id_usuario", idUser);
		p.set("cod_partido", b);
		p.saveIt();
		
		
		golloc = (String) req.queryParams("elocal2");
		golvis = (String) req.queryParams("evisit2");
		
		golLocal = Integer.parseInt(golloc);
		golVisit = Integer.parseInt(golvis);
		a = listMatch.get(1);
		b = (Integer)a.get("cod_partido");
		
		Prediction q = new Prediction();
		q.set("equipoL", golLocal);
		q.set("equipoV", golVisit);
		q.set("id_usuario", idUser);
		q.set("cod_partido", b);
		q.saveIt();


		golloc = (String) req.queryParams("elocal3");
		golvis = (String) req.queryParams("evisit3");

		golLocal = Integer.parseInt(golloc);
		golVisit = Integer.parseInt(golvis);
		a = listMatch.get(2);
		b = (Integer)a.get("cod_partido");
	
		
		Prediction r = new Prediction();
		r.set("equipoL", golLocal);
		r.set("equipoV", golVisit);
		r.set("id_usuario", idUser);
		r.set("cod_partido", b);
		r.saveIt();


		golloc = (String) req.queryParams("elocal4");
		golvis = (String) req.queryParams("evisit4");

		golLocal = Integer.parseInt(golloc);
		golVisit = Integer.parseInt(golvis);
		a = listMatch.get(3);
		b = (Integer)a.get("cod_partido");
	
		
		Prediction s = new Prediction();
		s.set("equipoL", golLocal);
		s.set("equipoV", golVisit);
		s.set("id_usuario", idUser);
		s.set("cod_partido", b);
		s.saveIt();


		pred.put("exito", "Predicción realizada! MUCHA SUERTE!");
		return pred;
	}
}