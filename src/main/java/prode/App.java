package prode;

import java.util.*;
import com.codahale.metrics.*;
import java.util.concurrent.TimeUnit;
import prode.User;
import org.javalite.activejdbc.Base;
import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App{
	
	static final MetricRegistry metrics = new MetricRegistry();
	private final static  Meter requests = metrics.meter("requests");
	
    public static void main( String[] args ) throws InterruptedException{ 
    	
    	staticFileLocation("/public");
    	/**
    	 * Apertura de la base de datos
    	 */
		before((request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true", "root", "root");
		});

		after((request, response) -> {
    	Base.close();
   		});	

		/**
		 * Carga de la pagina de inicio
		 */
		Map inicio = new HashMap();
   		get("/inicio", (req, res) -> {
   		    requests.mark();
   			User v = new User();
   	    	v.addSuperUser();
			return new ModelAndView(inicio, "./html/inicio.html");
        }, new MustacheTemplateEngine()
    	);
   		

   		/**
		 * Carga de la pagina de de logueo
		 */
   		Map logs = new HashMap();
   		get("/logs", AppControl::ingreso, new MustacheTemplateEngine());

   		/**
		 * Carga de la pagina de registro
		 */
   		Map reg = new HashMap();
    	get("/registro", (req, res) -> {
    	    requests.mark();
 		return new ModelAndView(reg, "./html/registro.html");
  		}, new MustacheTemplateEngine()
   		);
    	
    	/**
		 * 
		 */
    	post("/registro",AppControl::registro, new MustacheTemplateEngine());

    	/**
		 * Carga de la pagina para agregar equipos
		 */
	   	Map agrega = new HashMap();
	   	get("/agregar", (req, res) -> {
    	    requests.mark();
	   	return new ModelAndView(agrega, "./html/agregarequipo.html");	
	   	}, new MustacheTemplateEngine()
	   	);
	   
	   	post("/agregar",AppControl::agregarEquipo, new MustacheTemplateEngine());
	   	
	   	/**
		 * Carga de la pagina para cargar fechas
		 */
	   	get("/cargar",AppControl::cargarFecha, new MustacheTemplateEngine());

	   	post("/cargar",AppControl::armarFecha, new MustacheTemplateEngine());
	   	
	   	get("/observar", AppControl::observar, new MustacheTemplateEngine());
	 
	   	/*
	   	metrics.name(Queue.class, "requests", "size");
    	System.out.println(metrics.toString());
    	
    	startReport();
        Meter requests = metrics.meter("requests");
        requests.mark();
        wait5Seconds();    	
	   	*/
	   	get("/pronosticar", AppControl::seleccionFecha, new MustacheTemplateEngine());

   		get("/pronosticado", AppControl::pronosticarFecha, new MustacheTemplateEngine());
   		
   		post("/pronosticado", AppControl::guardarPronFecha, new MustacheTemplateEngine());
   		
   		get("/resultado", AppControl::seleccionarResultado, new MustacheTemplateEngine());
   		
   		get("/resultadocargado", AppControl::verResultado, new MustacheTemplateEngine());
	   	
	   	get("/modifpred", AppControl::seleccionFechaModif, new MustacheTemplateEngine());

	   	get("/modifpredcargada", AppControl::modPronFecha, new MustacheTemplateEngine());

	   	post("/modifpredcargada", AppControl::guardarModPronFecha, new MustacheTemplateEngine());
	   	
	   	}   
    
    //Metricas
    
    /*static void startReport() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    static void wait5Seconds() {
        try {
            Thread.sleep(5*1000);
        }
        catch(InterruptedException e) {}*/
}

