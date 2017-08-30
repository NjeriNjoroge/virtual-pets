import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Person {
  private String name;
  private String email;
  private int id;

  public Person(String name, String email) {
    this.name = name;
    this.email = email;
  }

//gets name property
  public String getName() {
    return name;
  }

//gets email property
  public String getEmail() {
    return email;
  }

//gets Id
public int getId() {
   return id;
 }

//overrides equals
@Override
public boolean equals(Object otherPerson){
if (!(otherPerson instanceof Person)) {
  return false;
} else {
  Person newPerson = (Person) otherPerson;
  return this.getName().equals(newPerson.getName()) &&
         this.getEmail().equals(newPerson.getEmail());
}
}

//saving to DB.
//altered the save method to gather database-assigned ID
public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO persons (name, email) VALUES (:name, :email)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("email", this.email)
      .executeUpdate()
      .getKey();
  }
}

//returns all DB entries
public static List<Person> all() {
  String sql = "SELECT * FROM persons";
  try(Connection con = DB.sql2o.open()) {
   return con.createQuery(sql).executeAndFetch(Person.class);
  }
}

//finds Person based on their ID
public static Person find(int id) {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM persons where id=:id";
    Person person = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Person.class);
    return person;
  }
}

//returns all monster objects belonging to a person
//updated so that it returns all types of virtual pets
public List<Object> getMonsters() {
  List<Object> allMonsters = new ArrayList<Object>();

  try(Connection con = DB.sql2o.open()) {
    String sqlFire = "SELECT * FROM monsters WHERE personId=:id AND type='fire';";
    List<FireMonster> fireMonsters = con.createQuery(sqlFire)
      .addParameter("id", this.id)
      .throwOnMappingFailure(false) //instructs sql2o not to throw errors if an object database contains an empty column for which it doesn't have a corresponding property
      .executeAndFetch(FireMonster.class);
    allMonsters.addAll(fireMonsters);

    String sqlWater = "SELECT * FROM monsters WHERE personId=:id AND type='water';";
    List<WaterMonster> waterMonsters = con.createQuery(sqlWater)
      .addParameter("id", this.id)
      .throwOnMappingFailure(false) //instructs sql2o not to throw errors if an object database contains an empty column for which it doesn't have a corresponding property
      .executeAndFetch(WaterMonster.class);
    allMonsters.addAll(waterMonsters);
    }

    return allMonsters;
}

}
