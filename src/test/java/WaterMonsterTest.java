import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

public class WaterMonsterTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

//creates instance of WaterMonster
  @Test
  public void WaterMonster_instantiatesCorrectly_true() {
    WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
    assertEquals(true, testWaterMonster instanceof WaterMonster);
  }

//instantiates WaterMonster with a name
  @Test
public void WaterMonster_instantiatesWithName_String() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  assertEquals("Bubbles", testWaterMonster.getName());
}

//instantiates WaterMonster with person id
@Test
public void WaterMonster_instantiatesWithPersonId_int() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  assertEquals(1, testWaterMonster.getPersonId());
}

//overriding equals()
@Test
public void equals_returnsTrueIfNameAndPersonIdAreSame_true() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  WaterMonster anotherWaterMonster = new WaterMonster("Bubbles", 1);
  assertTrue(testWaterMonster.equals(anotherWaterMonster));
}

//saves WaterMonsters to the DB
@Test
public void save_returnsTrueIfDescriptionsAretheSame() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.save();
  assertTrue(WaterMonster.all().get(0).equals(testWaterMonster));
}

//save also sets the WaterMonster's id attribute
@Test
public void save_assignsIdToWaterMonster() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.save();
  WaterMonster savedWaterMonster = WaterMonster.all().get(0);
  assertEquals(savedWaterMonster.getId(), testWaterMonster.getId());
}

//returns all database entries
@Test
public void all_returnsAllInstancesOfWaterMonster_true() {
  WaterMonster firstWaterMonster = new WaterMonster("Bubbles", 1);
  firstWaterMonster.save();
  WaterMonster secondWaterMonster = new WaterMonster("Spud", 1);
  secondWaterMonster.save();
  assertEquals(true, WaterMonster.all().get(0).equals(firstWaterMonster));
  assertEquals(true, WaterMonster.all().get(1).equals(secondWaterMonster));
}

//finding WaterMonsters based on id
@Test
public void find_returnsWaterMonsterWithSameId_secondWaterMonster() {
  WaterMonster firstWaterMonster = new WaterMonster("Bubbles", 1);
  firstWaterMonster.save();
  WaterMonster secondWaterMonster = new WaterMonster("Spud", 3);
  secondWaterMonster.save();
  assertEquals(WaterMonster.find(secondWaterMonster.getId()), secondWaterMonster);
}

//associating a person with many WaterMonsters
@Test
public void save_savesPersonIdIntoDB_true() {
  Person testPerson = new Person("Henry", "henry@henry.com");
  testPerson.save();
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", testPerson.getId());
  testWaterMonster.save();
  WaterMonster savedWaterMonster = WaterMonster.find(testWaterMonster.getId());
  assertEquals(savedWaterMonster.getPersonId(), testPerson.getId());
}

//instantiating WaterMonsters with half full levels
@Test
public void WaterMonster_instantiatesWithHalfFullPlayLevel(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  assertEquals(testWaterMonster.getPlayLevel(), (WaterMonster.MAX_PLAY_LEVEL / 2));
}

//instantiates with half-full sleepLevel
@Test
public void WaterMonster_instantiatesWithHalfFullSleepLevel(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  assertEquals(testWaterMonster.getSleepLevel(), (WaterMonster.MAX_SLEEP_LEVEL / 2));
}

//instantiates with half-full food level
@Test
public void WaterMonster_instantiatesWithHalfFullFoodLevel(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  assertEquals(testWaterMonster.getFoodLevel(), (WaterMonster.MAX_FOOD_LEVEL / 2));
}

//checks whether the WaterMonster has died
@Test
public void isAlive_confirmsWaterMonsterIsAliveIfAllLevelsAboveMinimum_true(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  assertEquals(testWaterMonster.isAlive(), true);
}

//confirms the isAlive() method can determine when WaterMonster is dead
@Test
public void depleteLevels_reducesAllLevels(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.depleteLevels();
  assertEquals(testWaterMonster.getFoodLevel(), (WaterMonster.MAX_FOOD_LEVEL / 2) - 1);
  assertEquals(testWaterMonster.getSleepLevel(), (WaterMonster.MAX_SLEEP_LEVEL / 2) - 1);
  assertEquals(testWaterMonster.getPlayLevel(), (WaterMonster.MAX_PLAY_LEVEL / 2) - 1);
}

//confirms the isAlive() method can determine when WaterMonster is dead
@Test
public void isAlive_recognizesWaterMonsterIsDeadWhenLevelsReachMinimum_false(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  for(int i = WaterMonster.MIN_ALL_LEVELS; i <= WaterMonster.MAX_FOOD_LEVEL; i++){
    testWaterMonster.depleteLevels();
  }
  assertEquals(testWaterMonster.isAlive(), false);
}

//allows users to interact with their pet to increase their levels
@Test
public void play_increasesWaterMonsterPlayLevel(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.play();
  assertTrue(testWaterMonster.getPlayLevel() > (WaterMonster.MAX_PLAY_LEVEL / 2));
}

//putting WaterMonsters to sleep
@Test
public void sleep_increasesWaterMonsterSleepLevel(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.sleep();
  assertTrue(testWaterMonster.getSleepLevel() > (WaterMonster.MAX_SLEEP_LEVEL / 2));
}

//feeding the WaterMonsters
@Test
public void feed_increasesWaterMonsterFoodLevel(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.feed();
  assertTrue(testWaterMonster.getFoodLevel() > (WaterMonster.MAX_FOOD_LEVEL / 2));
}

//when user tries to feed the WaterMonster to much
//updated to catch the exception
@Test
public void WaterMonster_foodLevelCannotGoBeyondMaxValue(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  for(int i = WaterMonster.MIN_ALL_LEVELS; i <= (WaterMonster.MAX_FOOD_LEVEL); i++){
    try {
      testWaterMonster.feed();
    } catch (UnsupportedOperationException exception){ }
  }
  assertTrue(testWaterMonster.getFoodLevel() <= WaterMonster.MAX_FOOD_LEVEL);
}

//confirms we receive an exception if we attempt to raise our WaterMonsters foodLevel
@Test (expected = UnsupportedOperationException.class)
public void feed_throwsExceptionIfFoodLevelIsAtMaxValue(){
 WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
 for(int i = WaterMonster.MIN_ALL_LEVELS; i <= (WaterMonster.MAX_FOOD_LEVEL); i++){
   testWaterMonster.feed();
 }
}

//confirms we receive an exception if we attempt to raise our WaterMonsters sleepLevel
@Test(expected = UnsupportedOperationException.class)
public void sleep_throwsExceptionIfSleepLevelIsAtMaxValue(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  for(int i = WaterMonster.MIN_ALL_LEVELS; i <= (WaterMonster.MAX_SLEEP_LEVEL); i++){
    testWaterMonster.sleep();
  }
}

//tests that throwing the above exception prevents sleepLevel from increasing beyond its constant
@Test
public void WaterMonster_sleepLevelCannotGoBeyondMaxValue(){
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  for(int i = WaterMonster.MIN_ALL_LEVELS; i <= (WaterMonster.MAX_SLEEP_LEVEL); i++){
    try {
      testWaterMonster.sleep();
    } catch (UnsupportedOperationException exception){ }
  }
  assertTrue(testWaterMonster.getSleepLevel() <= WaterMonster.MAX_SLEEP_LEVEL);
}

//saves WaterMonster birthdate into database
@Test
public void save_recordsTimeOfCreationInDatabase() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.save();
  Timestamp savedWaterMonsterBirthday = WaterMonster.find(testWaterMonster.getId()).getBirthday();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(rightNow.getDay(), savedWaterMonsterBirthday.getDay());
}

//asserts the sleep() method accurately updates the lastSlept value in DB
@Test
public void sleep_recordsTimeLastSleptInDatabase() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.save();
  testWaterMonster.sleep();
  Timestamp savedWaterMonsterLastSlept = WaterMonster.find(testWaterMonster.getId()).getLastSlept();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedWaterMonsterLastSlept));
}

//asserts the lastAte() method accurately updates value in DB
@Test
public void feed_recordsTimeLastAteInDatabase() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.save();
  testWaterMonster.feed();
  Timestamp savedWaterMonsterLastAte = WaterMonster.find(testWaterMonster.getId()).getLastAte();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedWaterMonsterLastAte));
}

//ensure our play() method accurately updates the WaterMonster's lastPlayed column in the database
@Test
public void play_recordsTimeLastPlayedInDatabase() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.save();
  testWaterMonster.play();
  Timestamp savedWaterMonsterLastPlayed = WaterMonster.find(testWaterMonster.getId()).getLastPlayed();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedWaterMonsterLastPlayed));
}

//integrating a basic Timer
@Test
public void timer_executesDepleteLevelsMethod() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  int firstPlayLevel = testWaterMonster.getPlayLevel();
  testWaterMonster.startTimer();
  try {
    Thread.sleep(6000);
  } catch (InterruptedException exception){}
  int secondPlayLevel = testWaterMonster.getPlayLevel();
  assertTrue(firstPlayLevel > secondPlayLevel);
}

//tests that the timer halts when one of the WaterMonster's levels reaches 0
@Test
public void timer_haltsAfterWaterMonsterDies() {
  WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
  testWaterMonster.startTimer();
  try {
    Thread.sleep(6000);
  } catch (InterruptedException exception){}
  assertFalse(testWaterMonster.isAlive());
  assertTrue(testWaterMonster.getFoodLevel() >= 0);
}


}
