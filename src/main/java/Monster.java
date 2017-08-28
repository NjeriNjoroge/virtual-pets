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

}
