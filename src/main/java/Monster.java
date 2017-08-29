import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Monster {
  private String name;
  private int personId;
  private int id;
  private int foodLevel;
  private int sleepLevel;
  private int playLevel;

  public static final int MAX_FOOD_LEVEL = 3;
  public static final int MAX_SLEEP_LEVEL = 8;
  public static final int MAX_PLAY_LEVEL = 12;
  public static final int MIN_ALL_LEVELS = 0;

  public Monster(String name, int personId) {
    this.name = name;
    this.personId = personId;
    playLevel = MAX_PLAY_LEVEL / 2;
    sleepLevel = MAX_SLEEP_LEVEL / 2;
    foodLevel = MAX_FOOD_LEVEL / 2;

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

//gets playLevel
public int getPlayLevel() {
  return playLevel;
}

//gets sleepLevel
public int getSleepLevel(){
  return sleepLevel;
}

//gets foodLevel
public int getFoodLevel(){
   return foodLevel;
 }

//checking playLevels
public void play(){
   playLevel++;
 }

 //putting monster to sleep
 public void sleep(){
  sleepLevel++;
}

//feeding the monsters
public void feed(){
   foodLevel++;
 }

//checks whether the monster has died
public boolean isAlive() {
  if (foodLevel <= MIN_ALL_LEVELS ||
  playLevel <= MIN_ALL_LEVELS ||
  sleepLevel <= MIN_ALL_LEVELS) {
    return false;
  }
  return true;
}

//confirms the isAlive() method can determine when monster is dead
public void depleteLevels(){
  playLevel--;
  foodLevel--;
  sleepLevel--;
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

//throws the exception when a user attempts to raise a pet's foodLevel above the upper limit
public void feed(){
  if (foodLevel >= MAX_FOOD_LEVEL){
    throw new UnsupportedOperationException("You cannot feed your monster anymore!");
  }
  foodLevel++;
}



}
