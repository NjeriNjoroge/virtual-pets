import java.util.Timer;
import org.sql2o.*;
import java.util.List;

public class WaterMonster extends Monster {
  private int waterLevel;
  public static final int MAX_WATER_LEVEL = 8;
  public static final String DATABASE_TYPE = "water";

  public WaterMonster(String name, int personId) {
    this.name = name;
    this.personId = personId;
    type = DATABASE_TYPE;
    playLevel = MAX_PLAY_LEVEL / 2;
    sleepLevel = MAX_SLEEP_LEVEL / 2;
    foodLevel = MAX_FOOD_LEVEL / 2;
    waterLevel = MAX_WATER_LEVEL / 2;
    timer = new Timer();
  }

//instantiates with water half full
public int getWaterLevel(){
   return waterLevel;
 }

//test to check waterLevel
@Override
public void depleteLevels(){
  if (isAlive()){
    playLevel--;
    foodLevel--;
    sleepLevel--;
    waterLevel--;
  }
}

//overriding isAlive()
@Override
public boolean isAlive() {
  if (foodLevel <= MIN_ALL_LEVELS ||
  playLevel <= MIN_ALL_LEVELS ||
  waterLevel <= MIN_ALL_LEVELS ||
  sleepLevel <= MIN_ALL_LEVELS) {
    return false;
  }
  return true;
}


//increases water level
//updated throws an exception if the waterLevel in maximum
public void water(){
  if (waterLevel >= MAX_WATER_LEVEL){
    throw new UnsupportedOperationException("You cannot water your pet any more!");
  }
  waterLevel++;
}

  //returns all DB entries
  //updated all() to return only the monster specific type
  public static List<WaterMonster> all() {
    String sql = "SELECT * FROM monsters WHERE type='water';";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .throwOnMappingFailure(false) //instructs sql2o not to throw errors if an object database contains an empty column for which it doesn't have a corresponding property
      .executeAndFetch(WaterMonster.class);
}
  }

  //finding monsters based on id
  public static WaterMonster find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM monsters where id=:id";
       WaterMonster monster = con.createQuery(sql)
         .addParameter("id", id)
         .throwOnMappingFailure(false) //instructs sql2o not to throw errors if an object database contains an empty column for which it doesn't have a corresponding property
         .executeAndFetchFirst(WaterMonster.class);
      return monster;
    }
  }

}
