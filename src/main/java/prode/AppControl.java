package prode;

import java.util.*;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import static spark.Spark.*;	
import prode.User;
//import spark.ModelAndView;
import spark.*;
import spark.template.mustache.MustacheTemplateEngine;

public class AppControl { 
	
	public static int idUser;
	private static List<Schadule> listCodMatch;
	private static int f;
	
	public static ModelAndView registro(Request req, Response res) {
		User nUsuario = new User();
		Map nuevoUser = new HashMap();
		if (nUsuario.checkUser(req) == false) {
			nUsuario.addUser(req);
			return new ModelAndView(nuevoUser, "./html/inicio.html");
		} else {
			nuevoUser.put("Error", "Nombre de usuario invalido");
			return new ModelAndView(nuevoUser, "./html/registro.html");
		}
		
	}
	
	public static ModelAndView ingreso(Request req, Response res) {
		User userLog = new User();
		Map logUser = new HashMap(); 
		if (userLog.checkUser(req)) {
			logUser	= userLog.getUser(req);
			idUser = (int) logUser.get("user");
			if ((Boolean)logUser.get("superu") == false){
				req.session().attribute("user", logUser.get("user"));
				return new ModelAndView(logUser, "./html/logs.html");
			} else {
				req.session().attribute("user", logUser.get("user"));
				return new ModelAndView(logUser, "./html/logsu.html");
			}	
		}	
		return new ModelAndView(logUser, "./html/inicio.html");
	}

	public static ModelAndView agregarEquipo(Request req, Response res) {
		Team nTeam = new Team();
   		Map nuevoTeam = new HashMap();
   		if (!nTeam.checkTeam(req)) {
   			nTeam.addTeam(req);
			return new ModelAndView(nuevoTeam, "./html/logsu.html");
   		} else {
   			nuevoTeam.put("Error", "Equipo ya cargado");
   			return new ModelAndView(nuevoTeam, "./html/agregarequipo.html");
   		}
   	}
	
	public static ModelAndView cargarFecha(Request req, Response res) {
	   	Map nuevosteams = new HashMap();
	   	Team teams = new Team();
	   	List<Team> teamsList = teams.getTeams(); 
	   	nuevosteams.put("equipos", teamsList);
	   	return new ModelAndView (nuevosteams, "./html/cargar.mustache");
	}
	
	public static ModelAndView armarFecha(Request req, Response res) {
		Match nMatch = new Match();
		Map nuevoMatch = nMatch.addMatchs(req);
		Schadule nSchadule = new Schadule();
		ArrayList<String> aux = new ArrayList<String>();
		aux = nMatch.getCodeNoSchadule();
		nSchadule.addSchadule(aux);
		return new ModelAndView(nuevoMatch, "./html/logsu.html"); 
	}

	public static ModelAndView seleccionFecha(Request req, Response res){
		Map pronostico = new HashMap();
   		Schadule n = new Schadule();	
   		List<Schadule> lista = n.listSchadule();	
   		pronostico.put("Fechas", lista);	
   		return new ModelAndView(pronostico, "./html/pronosticar.mustache");
   		}

	public static ModelAndView pronosticarFecha(Request req, Response res){
		Map pronostico = new HashMap();
		Match pronTeams = new Match();
        List<Match> teamsForPron = pronTeams.getMatchList();
        String fech = (req.queryParams("fecha").toString());
        f = Integer.parseInt(fech);
        List<Team> eq1 = Team.findBySQL("SELECT nom_equipo FROM teams a JOIN matches b ON a.cod_equipo = b.equipo_local JOIN schadules i USING (cod_partido) WHERE (i.num_fecha = '"+f+"') order by b.cod_partido");
        List<Team> eq2 = Team.findBySQL("SELECT nom_equipo FROM teams a JOIN matches b ON a.cod_equipo = b.equipo_visitante JOIN schadules i USING (cod_partido) WHERE (i.num_fecha = '"+f+"') order by b.cod_partido");
        
        Team eqprima = eq1.get(0);
        pronostico.put("nombreEquipo1",eqprima.getString("nom_equipo"));
        eqprima = eq1.get(1);
        pronostico.put("nombreEquipo2",eqprima.getString("nom_equipo"));
        eqprima = eq1.get(2);
        pronostico.put("nombreEquipo3",eqprima.getString("nom_equipo"));
        eqprima = eq1.get(3);
        pronostico.put("nombreEquipo4",eqprima.getString("nom_equipo"));
        eqprima = eq2.get(0);
        pronostico.put("nombreEquipo5",eqprima.getString("nom_equipo"));
        eqprima = eq2.get(1);
        pronostico.put("nombreEquipo6",eqprima.getString("nom_equipo"));
        eqprima = eq2.get(2);
        pronostico.put("nombreEquipo7",eqprima.getString("nom_equipo"));
        eqprima = eq2.get(3);
        pronostico.put("nombreEquipo8",eqprima.getString("nom_equipo"));
        return new ModelAndView(pronostico, "./html/pronosticar.html");
	}

	public static ModelAndView guardarPronFecha(Request req, Response res){
		listCodMatch = Schadule.findBySQL("SELECT cod_partido FROM schadules WHERE num_fecha = '"+f+"' ");
		Prediction nPrediccion = new Prediction();
   		Map nuevaPred = nPrediccion.addPrediction(req, idUser, listCodMatch);
   		if (idUser != 1) { 
   			//Point p = new Point();
   			//p.crearPoint(idUser);
   			return new ModelAndView(nuevaPred, "./html/logs.html");
   		} else {
   		List<Prediction> resultados = nPrediccion.findBySQL("SELECT * FROM predictions WHERE id_usuario = '"+idUser+"' AND cod_partido IN (select cod_partido from schadules WHERE num_fecha = '"+f+"') ");
   		List<Prediction> predicciones = nPrediccion.findBySQL("SELECT * FROM predictions WHERE id_usuario != '"+idUser+"' AND cod_partido IN (select cod_partido from schadules WHERE num_fecha = '"+f+"') "); 	
   		nPrediccion.Guardarpuntaje(predicciones, resultados);	
   			return new ModelAndView(nuevaPred, "./html/logsu.html");
   		}	
	}
}