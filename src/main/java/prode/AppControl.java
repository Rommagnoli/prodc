package prode;

import java.util.*;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import prode.Point;

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
				Schadule f = new Schadule();
				List<Point> points = Point.listaPuntosPorFecha(f.ultimaFecha());
				List<User> u = User.listaUserpuntosActuales(f.ultimaFecha());
				logUser.put("puntos", points);
				logUser.put("usuarios", u);
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
			return new ModelAndView(nuevoTeam, "./html/agregarequipo.html");
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
	
	
	public static ModelAndView seleccionarResultado(Request req, Response res){
		Map pronostico = new HashMap();
   		Schadule n = new Schadule();
   		List <Schadule> fecha = Schadule.findBySQL("SELECT DISTINCT num_fecha FROM schadules a  WHERE a.cod_partido IN (SELECT cod_partido FROM predictions b WHERE b.id_usuario = 1)");     
   		if (fecha.size() > 0) {
			pronostico.put("Fechas", fecha);
		}	
   		return new ModelAndView(pronostico, "./html/elegirfecharesultado.html");
   	}
	
	public static ModelAndView verResultado(Request req, Response res) {
		Map pronostico = new HashMap();
		Match pronTeams = new Match();
        List<Match> teamsForPron = pronTeams.getMatchList();
        String fech = (req.queryParams("fecha2"));
        System.out.println(fech);
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
        	List<Prediction> p = Prediction.findBySQL("SELECT * FROM predictions WHERE id_usuario = '"+idUser+"' AND cod_partido IN (select cod_partido from schadules WHERE num_fecha = '"+f+"') ");
        	pronostico.put("golEquipo1", p.get(0).get("equipoL"));
        	pronostico.put("golEquipo2", p.get(0).get("equipoV"));
        	pronostico.put("golEquipo3", p.get(1).get("equipoL"));
        	pronostico.put("golEquipo4", p.get(1).get("equipoV"));
        	pronostico.put("golEquipo5", p.get(2).get("equipoL"));
        	pronostico.put("golEquipo6", p.get(2).get("equipoV"));
        	pronostico.put("golEquipo7", p.get(3).get("equipoL"));
        	pronostico.put("golEquipo8", p.get(3).get("equipoV"));
        	pronostico.put("fecha", f);
        	List<Prediction> q = Prediction.findBySQL("SELECT * FROM predictions WHERE id_usuario = 1 AND cod_partido IN (select cod_partido from schadules WHERE num_fecha = '"+f+"') ");
        	pronostico.put("golEquipoR1",  q.get(0).get("equipoL"));
        	pronostico.put("golEquipoR2", q.get(0).get("equipoV"));
        	pronostico.put("golEquipoR3", q.get(1).get("equipoL"));
        	pronostico.put("golEquipoR4", q.get(1).get("equipoV"));
        	pronostico.put("golEquipoR5", q.get(2).get("equipoL"));
        	pronostico.put("golEquipoR6", q.get(2).get("equipoV"));
        	pronostico.put("golEquipoR7", q.get(3).get("equipoL"));
        	pronostico.put("golEquipoR8", q.get(3).get("equipoV"));
        	
        	return new ModelAndView(pronostico, "./html/resultado.html");
	}
	
	public static ModelAndView guardarPronFecha(Request req, Response res){
		listCodMatch = Schadule.findBySQL("SELECT cod_partido FROM schadules WHERE num_fecha = '"+f+"' ");
		Prediction nPrediccion = new Prediction();
   		Map nuevaPred = nPrediccion.addPrediction(req, idUser, listCodMatch);
   		if (idUser != 1) { 
   			Point p = new Point();
   			p.crearPoint(idUser, f);
   			return new ModelAndView(nuevaPred, "./html/logs.html");
   		} else {
   		List<Prediction> resultados = Prediction.findBySQL("SELECT * FROM predictions WHERE id_usuario = '"+idUser+"' AND cod_partido IN (select cod_partido from schadules WHERE num_fecha = '"+f+"') ");
   		List<Prediction> predicciones = Prediction.findBySQL("SELECT * FROM predictions WHERE id_usuario != '"+idUser+"' AND cod_partido IN (select cod_partido from schadules WHERE num_fecha = '"+f+"') "); 	
   		nPrediccion.guardarPuntaje(predicciones, resultados, f);	
   			return new ModelAndView(nuevaPred, "./html/logsu.html");
   		}	
	}
	
	public static ModelAndView seleccionFechaModif(Request req, Response res){
		Map pronostico = new HashMap();
   		Schadule n = new Schadule();	
   		List<Schadule> lista = n.listSchadule();	
   		pronostico.put("Fechas", lista);	
   		return new ModelAndView(pronostico, "./html/modifpred.mustache");
   		}
	
	public static ModelAndView modPronFecha(Request req, Response res){
		Map modPronostico = new HashMap();
		
		Match pronTeams = new Match();
        List<Match> teamsForPron = pronTeams.getMatchList();
        String fech = (req.queryParams("fecha"));
        f = Integer.parseInt(fech);
        List<Team> eq1 = Team.findBySQL("SELECT nom_equipo FROM teams a JOIN matches b ON a.cod_equipo = b.equipo_local JOIN schadules i USING (cod_partido) WHERE (i.num_fecha = '"+f+"') order by b.cod_partido");
        List<Team> eq2 = Team.findBySQL("SELECT nom_equipo FROM teams a JOIN matches b ON a.cod_equipo = b.equipo_visitante JOIN schadules i USING (cod_partido) WHERE (i.num_fecha = '"+f+"') order by b.cod_partido");
        
        Team eqprima = eq1.get(0);
        modPronostico.put("nombreEquipo1",eqprima.getString("nom_equipo"));
        eqprima = eq1.get(1);
        modPronostico.put("nombreEquipo2",eqprima.getString("nom_equipo"));
        eqprima = eq1.get(2);
        modPronostico.put("nombreEquipo3",eqprima.getString("nom_equipo"));
        eqprima = eq1.get(3);
        modPronostico.put("nombreEquipo4",eqprima.getString("nom_equipo"));
        eqprima = eq2.get(0);
        modPronostico.put("nombreEquipo5",eqprima.getString("nom_equipo"));
        eqprima = eq2.get(1);
        modPronostico.put("nombreEquipo6",eqprima.getString("nom_equipo"));
        eqprima = eq2.get(2);
        modPronostico.put("nombreEquipo7",eqprima.getString("nom_equipo"));
        eqprima = eq2.get(3);
        modPronostico.put("nombreEquipo8",eqprima.getString("nom_equipo"));
        
        return new ModelAndView(modPronostico, "./html/modifpred.html");	
	}
	
	public static ModelAndView guardarModPronFecha(Request req, Response res){
		listCodMatch = Schadule.findBySQL("SELECT cod_partido FROM schadules WHERE num_fecha = '"+f+"' ");
		Prediction nPrediccion = new Prediction();
   		Map nuevaPred = nPrediccion.modifPrediction(req, idUser, listCodMatch);
   		return new ModelAndView(nuevaPred, "./html/logs.html");
   	}
	
	public static ModelAndView observar(Request req, Response res) {
	   	Map usuarios = new HashMap(); 
	   	LazyList<User> userList = User.findAll();
	   	usuarios.put("usuarios", userList);
	   	return new ModelAndView (usuarios, "./html/verUsuarios.html");
	}
	
	public static ModelAndView verRank(Request req, Response res) {
	   	Map ranking = new HashMap();
		List<Point> puntos = Point.listaPuntosTotal();
		List<User> usuarios = User.listaUserPuntosTotales();
		ranking.put("puntos", puntos);
		ranking.put("usuarios", usuarios);
	   	return new ModelAndView (ranking, "./html/rank.html");
	}
}