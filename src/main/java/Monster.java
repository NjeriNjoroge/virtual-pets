import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

//public class Monster {

public abstract class Monster {

  public static final int MAX_FOOD_LEVEL = 3;
  public static final int MAX_SLEEP_LEVEL = 8;
  public static final int MAX_PLAY_LEVEL = 12;
  public static final int MIN_ALL_LEVELS = 0;


    public String name;
    public int personId;
    public int id;
    public int foodLevel;
    public int sleepLevel;
    public int playLevel;
    public Timestamp birthday;
    public Timestamp lastSlept;
    public Timestamp lastAte;
    public Timestamp lastPlayed;
    public Timer timer;

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
//ensure our play() method accurately updates the Monster's lastPlayed column in the database
public void play(){
  if (playLevel >= MAX_PLAY_LEVEL){
    throw new UnsupportedOperationException("You cannot play with monster anymore!");
  }
  try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE monsters SET lastplayed = now() WHERE id = :id";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  playLevel++;
}

 //retrieves the birthday value
 public Timestamp getBirthday(){
   return birthday;
 }

 //gets the time lastSlept
 public Timestamp getLastSlept(){
  return lastSlept;
}

//gets the time lastAte
public Timestamp getLastAte(){
   return lastAte;
 }

//gets the time lastPlayed
public Timestamp getLastPlayed(){
   return lastPlayed;
 }

 //putting monster to sleep
 //updated: prevents sleep levels from increasing
 //updated to insert new timestamp in the Monsters lastSlept column
 public void sleep(){
   if (sleepLevel >= MAX_SLEEP_LEVEL){
     throw new UnsupportedOperationException("You cannot make your monster sleep anymore!");
   }
   try(Connection con = DB.sql2o.open()) {
     String sql = "UPDATE monsters SET lastslept = now() WHERE id = :id";
     con.createQuery(sql)
       .addParameter("id", id)
       .executeUpdate();
     }
   sleepLevel++;
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
  if (isAlive()) {
  playLevel--;
  foodLevel--;
  sleepLevel--;
}
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
//updated to include the birthdate of every new monster
public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO monsters (name, personId, birthday) VALUES (:name, :personId, now())";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("personId", this.personId)
      .executeUpdate()
      .getKey();
  }
}





//updated the feed method
//throws the exception when a user attempts to raise a pet's foodLevel above the upper limit
//updated to record lastAte in DB
public void feed(){
  if (foodLevel >= MAX_FOOD_LEVEL){
    throw new UnsupportedOperationException("You cannot feed your monster anymore!");
  }
  try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE monsters SET lastate = now() WHERE id = :id";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  foodLevel++;
}

//startTimer method
public void startTimer(){
  Monster currentMonster = this;
  TimerTask timerTask = new TimerTask(){
    @Override
    public void run() {
      if (currentMonster.isAlive() == false){
        cancel();
      }
      depleteLevels();
    }
  };
  this.timer.schedule(timerTask, 0, 600);
}



}
