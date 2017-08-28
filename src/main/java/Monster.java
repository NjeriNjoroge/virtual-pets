import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Monster {
  private String name;
  private int id;
  private int personId;

  public Monster(String name, int personId) {
    this.name = name;
    this.personId = personId;
  }

//gets Monster name
  public String getName(){
    return name;
  }

//instantiates monster with person id
  public int getPersonId(){
   return personId;
 }

 //gets monster id
 public int getId() {
   return id;
 }


//overrides equals
@Override
public boolean equals(Object otherMonster){
  if (!(otherMonster instanceof Monster)) {
    return false;
  } else {
    Monster newMonster = (Monster) otherMonster;
    return this.getName().equals(newMonster.getName()) &&
           this.getPersonId() == newMonster.getPersonId();
  }
}

//saving to DB
 public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO monsters (name, personid) VALUES (:name, :personId)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("personId", this.personId)
      .executeUpdate()
      .getKey();
  }
}

//returns all DB entries
public static List<Monster> all() {
  String sql = "SELECT * FROM monsters";
  try(Connection con = DB.sql2o.open()) {
    return con.createQuery(sql).executeAndFetch(Monster.class);
  }
}

//finding monsters based on id
public static Monster find(int id) {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM monsters where id=:id";
    Monster monster = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Monster.class);
    return monster;
  }
}

}
