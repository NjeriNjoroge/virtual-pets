public class Monster {
  private String name;

  public Monster(String name, int personId) {
    this.name = name;
  }

//gets Monster name
  public String getName(){
    return name;
  }

//instantiates monster with person id
  public int getPersonId(){
   return personId;
 }

//overrides equals
//updated so that it empties the monster table after each spec
@Override
 protected void after() {
   try(Connection con = DB.sql2o.open()) {
     String deletePersonsQuery = "DELETE FROM persons *;";
     String deleteMonstersQuery = "DELETE FROM monsters *;";
     con.createQuery(deletePersonsQuery).executeUpdate();
     con.createQuery(deleteMonstersQuery).executeUpdate();
   }
 }

}
