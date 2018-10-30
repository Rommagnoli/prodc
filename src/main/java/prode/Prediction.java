package prode;

import java.util.*;
import static spark.Spark.*;
import spark.*;
import org.javalite.activejdbc.*;

public class Prediction extends Model {

	public Prediction(){}

	public Map addPrediction(Request req){
	
		String golloc = (String) req.queryParams("elocal1");
		String golvis = (String) req.queryParams("evisit1");

		int golLocal = (int)golloc.charAt(0);
		int golVisit = (int)golvis.charAt(0);

		Map pred = new HashMap();
		Prediction p = new Prediction();
		p.set("equipoL", golLocal);
		p.set("equipoV", golVisit);
		p.set("id_usuario", User.iuser);
		p.saveIt();


		golloc = (String) req.queryParams("elocal2");
		golvis = (String) req.queryParams("evisit2");

		golLocal = (int)golloc.charAt(0);
		golVisit = (int)golvis.charAt(0);

		Prediction q = new Prediction();
		q.set("equipoL", golLocal);
		q.set("equipoV", golVisit);
		q.saveIt();


		golloc = (String) req.queryParams("elocal3");
		golvis = (String) req.queryParams("evisit3");

		golLocal = (int)golloc.charAt(0);
		golVisit = (int)golvis.charAt(0);

		Prediction r = new Prediction();
		r.set("equipoL", golLocal);
		r.set("equipoV", golVisit);
		r.saveIt();


		golloc = (String) req.queryParams("elocal4");
		golvis = (String) req.queryParams("evisit4");

		golLocal = (int)golloc.charAt(0);
		golVisit = (int)golvis.charAt(0);

		Prediction s = new Prediction();
		s.set("equipoL", golLocal);
		s.set("equipoV", golVisit);
		s.saveIt();


		pred.put("exito", "Predicción realizada! MUCHA SUERTE!");
		return pred;
	}
}