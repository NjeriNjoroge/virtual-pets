import java.util.Timer;
import org.sql2o.*;
import java.util.List;
import java.sql.Timestamp;

public class FireMonster extends Monster {
  private int fireLevel;
  public static final int MAX_FIRE_LEVEL = 10;
  public static final String DATABASE_TYPE = "fire";
  public Timestamp lastKindling;

  public FireMonster(String name, int personId) {
    this.name = name;
    this.personId = personId;
    type = DATABASE_TYPE;
    playLevel = MAX_PLAY_LEVEL / 2;
    sleepLevel = MAX_SLEEP_LEVEL / 2;
    foodLevel = MAX_FOOD_LEVEL / 2;
    fireLevel = MAX_FIRE_LEVEL / 2;
    timer = new Timer();
  }

//overrides depleteLevels
@Override
public void depleteLevels(){
  if (isAlive()){
    playLevel--;
    foodLevel--;
    sleepLevel--;
    fireLevel--;
  }
}

//checks the status of the fire level
@Override
public boolean isAlive() {
  if (foodLevel <= MIN_ALL_LEVELS ||
  playLevel <= MIN_ALL_LEVELS ||
  fireLevel <= MIN_ALL_LEVELS ||
  sleepLevel <= MIN_ALL_LEVELS) {
    return false;
  }
  return true;
}


//instantiates with fire level half-full
  public int getFireLevel(){
    return fireLevel;
  }

//increases fire monsters fire level
//update: throws exception when maxed out
//update:asserts that the kindling method can update the lastKindling timestamp in the DB
public void kindling(){
  if (fireLevel >= MAX_PLAY_LEVEL){
    throw new UnsupportedOperationException("You cannot give any more kindling!");
  }
  try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE monsters SET lastkindling = now() WHERE id = :id";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  fireLevel++;
}

//asserts that the kindling method can update the lastKindling timestamp in the DB
public Timestamp getLastKindling(){
  return lastKindling;
}

  //returns all DB entries
  //updated all() to return only the monster specific type
  public static List<FireMonster> all() {
    String sql = "SELECT * FROM monsters WHERE type='fire';";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .throwOnMappingFailure(false) //instructs sql2o not to throw errors if an object database contains an empty column for which it doesn't have a corresponding property
      .executeAndFetch(FireMonster.class);
    }
  }

  //finding monsters based on id
  public static FireMonster find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM monsters where id=:id";
      FireMonster monster = con.createQuery(sql)
        .addParameter("id", id)
        .throwOnMappingFailure(false) //instructs sql2o not to throw errors if an object database contains an empty column for which it doesn't have a corresponding property
        .executeAndFetchFirst(FireMonster.class);
    return monster;
    }
  }

}
