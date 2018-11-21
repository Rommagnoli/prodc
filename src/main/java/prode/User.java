package prode;

import java.util.*;
import org.javalite.activejdbc.Base;
import static spark.Spark.*;
import spark.*;
import org.javalite.activejdbc.Model;

public class User extends Model {


	public User(){	}
	
	public void addSuperUser() {
		List<User> busqueda;
		if ((busqueda = User.where("superu = ?", true)).size() == 0 ) {
			String userlog = "superusuario";
			String passlog = "superusuario";
			User u = new User();
			u.set("username", userlog);
			u.set("password", passlog);
			u.set("superu", true);
			u.saveIt();
		}	
	}
	
	public boolean checkUsername(User user) {
		String username = (String) user.get("username");
		return !(username == "" || username.length() < 5);
	}
	
	public boolean checkPassword(User user) {
		String password = (String) user.get("password");
		return !(password == "" || password.length() < 5);
	}
		
	public boolean checkUser(Request req) throws IllegalArgumentException {
		String userlog = req.queryParams("user");
		String passlog = req.queryParams("password");
		List<User> busqueda = User.where("username = ? and password = ?", userlog, passlog);
		Boolean esta = (busqueda.size() != 0);
		return esta;
	}
		
	

	public void addUser(Request req) throws IllegalArgumentException {
		String userlog = req.queryParams("user");
		String passlog = req.queryParams("password");
		User u = new User();
		u.set("username", userlog);
        u.set("password", passlog);
        if (!(u.checkPassword(u) || u.checkUsername(u)))
        	throw new IllegalArgumentException("Nombre de usuario o contraseña demasiado cortos");
        u.set("superu", false);
        u.saveIt();
    }

    public Map getUser(Request req){
    	String userlog = req.queryParams("user");
    	String passlog = req.queryParams("password");
    	Map resultUser = new HashMap();
    	List<User> busqueda = User.where("username = ? and password = ?", userlog, passlog);
    	resultUser.put("user", busqueda.get(0).get("id"));
    	resultUser.put("superu", busqueda.get(0).get("superu"));
    	return resultUser;
    }
    

    public static List<User> listaUserpuntosActuales(int idFecha) {
    	List<User> Users = User.findBySQL("SELECT username FROM users a JOIN points b ON a.id = b.idUser where b.idFecha = '"+idFecha+"' ORDER BY b.puntajeActual DESC");
    	return Users;
	}
    public int id() {
    	return this.getInteger("id");//IMPORTANTE
    }
    
    public String username() {
    	return this.getString("username");
    }

}