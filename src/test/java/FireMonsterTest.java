import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

public class FireMonsterTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

//creates instance of FireMonster
  @Test
  public void FireMonster_instantiatesCorrectly_true() {
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    assertEquals(true, testFireMonster instanceof FireMonster);
  }

//instantiates FireMonster with a name
  @Test
public void FireMonster_instantiatesWithName_String() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  assertEquals("Bubbles", testFireMonster.getName());
}

//instantiates FireMonster with person id
@Test
public void FireMonster_instantiatesWithPersonId_int() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  assertEquals(1, testFireMonster.getPersonId());
}

//overriding equals()
@Test
public void equals_returnsTrueIfNameAndPersonIdAreSame_true() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  FireMonster anotherFireMonster = new FireMonster("Bubbles", 1);
  assertTrue(testFireMonster.equals(anotherFireMonster));
}

//saves FireMonsters to the DB
@Test
public void save_returnsTrueIfDescriptionsAretheSame() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.save();
  assertTrue(FireMonster.all().get(0).equals(testFireMonster));
}

//save also sets the FireMonster's id attribute
@Test
public void save_assignsIdToFireMonster() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.save();
  FireMonster savedFireMonster = FireMonster.all().get(0);
  assertEquals(savedFireMonster.getId(), testFireMonster.getId());
}

//returns all database entries
@Test
public void all_returnsAllInstancesOfFireMonster_true() {
  FireMonster firstFireMonster = new FireMonster("Bubbles", 1);
  firstFireMonster.save();
  FireMonster secondFireMonster = new FireMonster("Spud", 1);
  secondFireMonster.save();
  assertEquals(true, FireMonster.all().get(0).equals(firstFireMonster));
  assertEquals(true, FireMonster.all().get(1).equals(secondFireMonster));
}

//finding FireMonsters based on id
@Test
public void find_returnsFireMonsterWithSameId_secondFireMonster() {
  FireMonster firstFireMonster = new FireMonster("Bubbles", 1);
  firstFireMonster.save();
  FireMonster secondFireMonster = new FireMonster("Spud", 3);
  secondFireMonster.save();
  assertEquals(FireMonster.find(secondFireMonster.getId()), secondFireMonster);
}

//associating a person with many FireMonsters
@Test
public void save_savesPersonIdIntoDB_true() {
  Person testPerson = new Person("Henry", "henry@henry.com");
  testPerson.save();
  FireMonster testFireMonster = new FireMonster("Bubbles", testPerson.getId());
  testFireMonster.save();
  FireMonster savedFireMonster = FireMonster.find(testFireMonster.getId());
  assertEquals(savedFireMonster.getPersonId(), testPerson.getId());
}

//instantiating FireMonsters with half full levels
@Test
public void FireMonster_instantiatesWithHalfFullPlayLevel(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  assertEquals(testFireMonster.getPlayLevel(), (FireMonster.MAX_PLAY_LEVEL / 2));
}

//instantiates with half-full sleepLevel
@Test
public void FireMonster_instantiatesWithHalfFullSleepLevel(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  assertEquals(testFireMonster.getSleepLevel(), (FireMonster.MAX_SLEEP_LEVEL / 2));
}

//instantiates with half-full food level
@Test
public void FireMonster_instantiatesWithHalfFullFoodLevel(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  assertEquals(testFireMonster.getFoodLevel(), (FireMonster.MAX_FOOD_LEVEL / 2));
}

//checks whether the FireMonster has died
@Test
public void isAlive_confirmsFireMonsterIsAliveIfAllLevelsAboveMinimum_true(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  assertEquals(testFireMonster.isAlive(), true);
}

//confirms the isAlive() method can determine when FireMonster is dead
@Test
public void depleteLevels_reducesAllLevels(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.depleteLevels();
  assertEquals(testFireMonster.getFoodLevel(), (FireMonster.MAX_FOOD_LEVEL / 2) - 1);
  assertEquals(testFireMonster.getSleepLevel(), (FireMonster.MAX_SLEEP_LEVEL / 2) - 1);
  assertEquals(testFireMonster.getPlayLevel(), (FireMonster.MAX_PLAY_LEVEL / 2) - 1);
}

//confirms the isAlive() method can determine when FireMonster is dead
@Test
public void isAlive_recognizesFireMonsterIsDeadWhenLevelsReachMinimum_false(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  for(int i = FireMonster.MIN_ALL_LEVELS; i <= FireMonster.MAX_FOOD_LEVEL; i++){
    testFireMonster.depleteLevels();
  }
  assertEquals(testFireMonster.isAlive(), false);
}

//allows users to interact with their pet to increase their levels
@Test
public void play_increasesFireMonsterPlayLevel(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.play();
  assertTrue(testFireMonster.getPlayLevel() > (FireMonster.MAX_PLAY_LEVEL / 2));
}

//putting FireMonsters to sleep
@Test
public void sleep_increasesFireMonsterSleepLevel(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.sleep();
  assertTrue(testFireMonster.getSleepLevel() > (FireMonster.MAX_SLEEP_LEVEL / 2));
}

//feeding the FireMonsters
@Test
public void feed_increasesFireMonsterFoodLevel(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.feed();
  assertTrue(testFireMonster.getFoodLevel() > (FireMonster.MAX_FOOD_LEVEL / 2));
}

//when user tries to feed the FireMonster to much
//updated to catch the exception
@Test
public void FireMonster_foodLevelCannotGoBeyondMaxValue(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_FOOD_LEVEL); i++){
    try {
      testFireMonster.feed();
    } catch (UnsupportedOperationException exception){ }
  }
  assertTrue(testFireMonster.getFoodLevel() <= FireMonster.MAX_FOOD_LEVEL);
}

//confirms we receive an exception if we attempt to raise our FireMonsters foodLevel
@Test (expected = UnsupportedOperationException.class)
public void feed_throwsExceptionIfFoodLevelIsAtMaxValue(){
 FireMonster testFireMonster = new FireMonster("Bubbles", 1);
 for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_FOOD_LEVEL); i++){
   testFireMonster.feed();
 }
}

//confirms we receive an exception if we attempt to raise our FireMonsters sleepLevel
@Test(expected = UnsupportedOperationException.class)
public void sleep_throwsExceptionIfSleepLevelIsAtMaxValue(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_SLEEP_LEVEL); i++){
    testFireMonster.sleep();
  }
}

//tests that throwing the above exception prevents sleepLevel from increasing beyond its constant
@Test
public void FireMonster_sleepLevelCannotGoBeyondMaxValue(){
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_SLEEP_LEVEL); i++){
    try {
      testFireMonster.sleep();
    } catch (UnsupportedOperationException exception){ }
  }
  assertTrue(testFireMonster.getSleepLevel() <= FireMonster.MAX_SLEEP_LEVEL);
}

//saves FireMonster birthdate into database
@Test
public void save_recordsTimeOfCreationInDatabase() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.save();
  Timestamp savedFireMonsterBirthday = FireMonster.find(testFireMonster.getId()).getBirthday();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(rightNow.getDay(), savedFireMonsterBirthday.getDay());
}

//asserts the sleep() method accurately updates the lastSlept value in DB
@Test
public void sleep_recordsTimeLastSleptInDatabase() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.save();
  testFireMonster.sleep();
  Timestamp savedFireMonsterLastSlept = FireMonster.find(testFireMonster.getId()).getLastSlept();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedFireMonsterLastSlept));
}

//asserts the lastAte() method accurately updates value in DB
@Test
public void feed_recordsTimeLastAteInDatabase() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.save();
  testFireMonster.feed();
  Timestamp savedFireMonsterLastAte = FireMonster.find(testFireMonster.getId()).getLastAte();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedFireMonsterLastAte));
}

//ensure our play() method accurately updates the FireMonster's lastPlayed column in the database
@Test
public void play_recordsTimeLastPlayedInDatabase() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.save();
  testFireMonster.play();
  Timestamp savedFireMonsterLastPlayed = FireMonster.find(testFireMonster.getId()).getLastPlayed();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedFireMonsterLastPlayed));
}

//integrating a basic Timer
@Test
public void timer_executesDepleteLevelsMethod() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  int firstPlayLevel = testFireMonster.getPlayLevel();
  testFireMonster.startTimer();
  try {
    Thread.sleep(6000);
  } catch (InterruptedException exception){}
  int secondPlayLevel = testFireMonster.getPlayLevel();
  assertTrue(firstPlayLevel > secondPlayLevel);
}

//tests that the timer halts when one of the FireMonster's levels reaches 0
@Test
public void timer_haltsAfterFireMonsterDies() {
  FireMonster testFireMonster = new FireMonster("Bubbles", 1);
  testFireMonster.startTimer();
  try {
    Thread.sleep(6000);
  } catch (InterruptedException exception){}
  assertFalse(testFireMonster.isAlive());
  assertTrue(testFireMonster.getFoodLevel() >= 0);
}


}
