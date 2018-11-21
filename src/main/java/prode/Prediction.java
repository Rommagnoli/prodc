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

		return pred;
	}
	
	public Map modifPrediction(Request req, int idUser, List<Schadule> listMatch){
		Map pred = new HashMap();

		String golloc = (String) req.queryParams("elocal1");
		String golvis = (String) req.queryParams("evisit1");
		
		Integer golLocal = Integer.parseInt(golloc);
		Integer golVisit = Integer.parseInt(golvis);
		Schadule fecha = listMatch.get(0);
		Integer cPart = (Integer)fecha.get("cod_partido");
		Prediction.update("equipoL = ?", "id_usuario = '"+idUser+"' AND cod_partido = '"+cPart+"'", golLocal);
		Prediction.update("equipoV = ?", "id_usuario = '"+idUser+"' AND cod_partido = '"+cPart+"'", golVisit);
		
		golloc = (String) req.queryParams("elocal2");
		golvis = (String) req.queryParams("evisit2");
		fecha = listMatch.get(1);
		cPart = (Integer)fecha.get("cod_partido");
		golLocal = Integer.parseInt(golloc);
		golVisit = Integer.parseInt(golvis);
		Prediction.update("equipoL = ?", "id_usuario = '"+idUser+"' AND cod_partido = '"+cPart+"'", golLocal);
		Prediction.update("equipoV = ?", "id_usuario = '"+idUser+"' AND cod_partido = '"+cPart+"'", golVisit);
		
		golloc = (String) req.queryParams("elocal3");
		golvis = (String) req.queryParams("evisit3");
		fecha = listMatch.get(2);
		cPart = (Integer)fecha.get("cod_partido");
		golLocal = Integer.parseInt(golloc);
		golVisit = Integer.parseInt(golvis);
		Prediction.update("equipoL = ?", "id_usuario = '"+idUser+"' AND cod_partido = '"+cPart+"'", golLocal);
		Prediction.update("equipoV = ?", "id_usuario = '"+idUser+"' AND cod_partido = '"+cPart+"'", golVisit);
		
		golloc = (String) req.queryParams("elocal4");
		golvis = (String) req.queryParams("evisit4");
		fecha = listMatch.get(3);
		cPart = (Integer)fecha.get("cod_partido");
		golLocal = Integer.parseInt(golloc);
		golVisit = Integer.parseInt(golvis);
		Prediction.update("equipoL = ?", "id_usuario = '"+idUser+"' AND cod_partido = '"+cPart+"'", golLocal);
		Prediction.update("equipoV = ?", "id_usuario = '"+idUser+"' AND cod_partido = '"+cPart+"'", golVisit);

		return pred;
	}
		
	public int compararPred(Prediction adm, Prediction user) {
		if ((Integer)adm.get("equipoL") == (Integer)user.get("equipoL") && (Integer)adm.get("equipoV") == (Integer)user.get("equipoV"))
			return 3;
		if ((Integer)adm.get("equipoL") < (Integer)adm.get("equipoV") && (Integer)user.get("equipoL") < (Integer)user.get("equipoV"))
			return 1;
		if ((Integer)adm.get("equipoL") > (Integer)adm.get("equipoV") && (Integer)user.get("equipoL") > (Integer)user.get("equipoV"))
			return 1;
		if (((Integer)adm.get("equipoL") - (Integer)adm.get("equipoV")) == 0 && ((Integer)user.get("equipoL") - (Integer)user.get("equipoV")) == 0)
			return 1;
		return 0;	
	}
	
	public void guardarPuntaje(List<Prediction> UserList, List<Prediction> AdminList, int idFecha) {
		int result = 0;
		int idUser;
		int j = 0;
		for (int i = 0; i < UserList.size(); i++) {
			result = result + compararPred(AdminList.get(j), UserList.get(i));
			if (j == (AdminList.size()-1)) {
				j = 0;
				idUser = (Integer)UserList.get(i-1).get("id_usuario");
				Point p = new Point();
				p.update("puntajeActual = ?", "idUser = '"+idUser+"' AND idFecha = '"+idFecha+"'", result);
				int pTotal = p.puntajeTotal(idUser);
				p.update("puntajeTotal = ?", "idUser = '"+idUser+"' AND idFecha = '"+idFecha+"'", pTotal);
				result = 0;
			}	
			j++;
		}
	}
}