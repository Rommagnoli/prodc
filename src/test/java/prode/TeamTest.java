package prode;

import prode.Team;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TeamTest {
	
	@Before
  	public void before(){
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
    	System.out.println("UserTest setup");
    	Base.openTransaction();
  	}

  	@After
  	public void after(){
    	System.out.println("UserTest tearDown");
    	Base.rollbackTransaction();
    	Base.close();
  	}

  	@Test
	public void testTeamNombre() {
  		Team t = new Team();
  		t.set("nom_equipo","");
  		String s  = (String)t.get("nom_equipo");
		assertEquals(t.validTeam(s), false);
		
	}

}
