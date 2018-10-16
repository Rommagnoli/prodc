package prode;

import java.util.*;
import org.javalite.activejdbc.Base;
import static spark.Spark.*;
import spark.*;
import org.javalite.activejdbc.Model;

public class User extends Model {

	public User(){	}

	public Map addUser(Request req){
		String userlog = req.queryParams("user");
		String passlog = req.queryParams("password");

		Map resultUser = new HashMap();
		List<User> busqueda = User.where("username = ?", userlog);
		Boolean esta = (busqueda.size() == 0);
		if (esta){
			User u = new User();
       		u.set("username", userlog);
        	u.set("password", passlog);
        	u.set("superu", false);
        	u.saveIt();
		} else {
			resultUser.put("Error","Usuario en uso, intente con otro");
			return resultUser;
		}
		return resultUser;
    }

    public Map getUser(Request req){
    	String userlog = req.queryParams("user");
    	String passlog = req.queryParams("password");

    	Map resultUser = new HashMap();

    	List<User> busqueda = User.where("username = ? and password = ?", userlog, passlog);
    	Boolean esta = (busqueda.size() != 0);

    	if (esta){
    		resultUser.put("user", busqueda.get(0).get("id"));
    		resultUser.put("superu", busqueda.get(0).get("superu"));
    		return resultUser;
    	}
    	resultUser.put("user", null);
    	return resultUser;
    }

    
}