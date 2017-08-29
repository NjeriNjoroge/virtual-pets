import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

public class MonsterTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

//creates instance of Monster
  @Test
  public void monster_instantiatesCorrectly_true() {
    Monster testMonster = new Monster("Bubbles", 1);
    assertEquals(true, testMonster instanceof Monster);
  }

//instantiates Monster with a name
  @Test
public void Monster_instantiatesWithName_String() {
  Monster testMonster = new Monster("Bubbles", 1);
  assertEquals("Bubbles", testMonster.getName());
}

//instantiates Monster with person id
@Test
public void Monster_instantiatesWithPersonId_int() {
  Monster testMonster = new Monster("Bubbles", 1);
  assertEquals(1, testMonster.getPersonId());
}

//overriding equals()
@Test
public void equals_returnsTrueIfNameAndPersonIdAreSame_true() {
  Monster testMonster = new Monster("Bubbles", 1);
  Monster anotherMonster = new Monster("Bubbles", 1);
  assertTrue(testMonster.equals(anotherMonster));
}

//saves Monsters to the DB
@Test
public void save_returnsTrueIfDescriptionsAretheSame() {
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.save();
  assertTrue(Monster.all().get(0).equals(testMonster));
}

//save also sets the Monster's id attribute
@Test
public void save_assignsIdToMonster() {
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.save();
  Monster savedMonster = Monster.all().get(0);
  assertEquals(savedMonster.getId(), testMonster.getId());
}

//returns all database entries
@Test
public void all_returnsAllInstancesOfMonster_true() {
  Monster firstMonster = new Monster("Bubbles", 1);
  firstMonster.save();
  Monster secondMonster = new Monster("Spud", 1);
  secondMonster.save();
  assertEquals(true, Monster.all().get(0).equals(firstMonster));
  assertEquals(true, Monster.all().get(1).equals(secondMonster));
}

//finding monsters based on id
@Test
public void find_returnsMonsterWithSameId_secondMonster() {
  Monster firstMonster = new Monster("Bubbles", 1);
  firstMonster.save();
  Monster secondMonster = new Monster("Spud", 3);
  secondMonster.save();
  assertEquals(Monster.find(secondMonster.getId()), secondMonster);
}

//associating a person with many monsters
@Test
public void save_savesPersonIdIntoDB_true() {
  Person testPerson = new Person("Henry", "henry@henry.com");
  testPerson.save();
  Monster testMonster = new Monster("Bubbles", testPerson.getId());
  testMonster.save();
  Monster savedMonster = Monster.find(testMonster.getId());
  assertEquals(savedMonster.getPersonId(), testPerson.getId());
}

//instantiating monsters with half full levels
@Test
public void monster_instantiatesWithHalfFullPlayLevel(){
  Monster testMonster = new Monster("Bubbles", 1);
  assertEquals(testMonster.getPlayLevel(), (Monster.MAX_PLAY_LEVEL / 2));
}

//instantiates with half-full sleepLevel
@Test
public void monster_instantiatesWithHalfFullSleepLevel(){
  Monster testMonster = new Monster("Bubbles", 1);
  assertEquals(testMonster.getSleepLevel(), (Monster.MAX_SLEEP_LEVEL / 2));
}

//instantiates with half-full food level
@Test
public void monster_instantiatesWithHalfFullFoodLevel(){
  Monster testMonster = new Monster("Bubbles", 1);
  assertEquals(testMonster.getFoodLevel(), (Monster.MAX_FOOD_LEVEL / 2));
}

//checks whether the monster has died
@Test
public void isAlive_confirmsMonsterIsAliveIfAllLevelsAboveMinimum_true(){
  Monster testMonster = new Monster("Bubbles", 1);
  assertEquals(testMonster.isAlive(), true);
}

//confirms the isAlive() method can determine when monster is dead
@Test
public void depleteLevels_reducesAllLevels(){
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.depleteLevels();
  assertEquals(testMonster.getFoodLevel(), (Monster.MAX_FOOD_LEVEL / 2) - 1);
  assertEquals(testMonster.getSleepLevel(), (Monster.MAX_SLEEP_LEVEL / 2) - 1);
  assertEquals(testMonster.getPlayLevel(), (Monster.MAX_PLAY_LEVEL / 2) - 1);
}

//confirms the isAlive() method can determine when monster is dead
@Test
public void isAlive_recognizesMonsterIsDeadWhenLevelsReachMinimum_false(){
  Monster testMonster = new Monster("Bubbles", 1);
  for(int i = Monster.MIN_ALL_LEVELS; i <= Monster.MAX_FOOD_LEVEL; i++){
    testMonster.depleteLevels();
  }
  assertEquals(testMonster.isAlive(), false);
}

//allows users to interact with their pet to increase their levels
@Test
public void play_increasesMonsterPlayLevel(){
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.play();
  assertTrue(testMonster.getPlayLevel() > (Monster.MAX_PLAY_LEVEL / 2));
}

//putting Monsters to sleep
@Test
public void sleep_increasesMonsterSleepLevel(){
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.sleep();
  assertTrue(testMonster.getSleepLevel() > (Monster.MAX_SLEEP_LEVEL / 2));
}

//feeding the monsters
@Test
public void feed_increasesMonsterFoodLevel(){
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.feed();
  assertTrue(testMonster.getFoodLevel() > (Monster.MAX_FOOD_LEVEL / 2));
}

//when user tries to feed the monster to much
//updated to catch the exception
@Test
public void monster_foodLevelCannotGoBeyondMaxValue(){
  Monster testMonster = new Monster("Bubbles", 1);
  for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_FOOD_LEVEL); i++){
    try {
      testMonster.feed();
    } catch (UnsupportedOperationException exception){ }
  }
  assertTrue(testMonster.getFoodLevel() <= Monster.MAX_FOOD_LEVEL);
}

//confirms we receive an exception if we attempt to raise our Monsters foodLevel
@Test (expected = UnsupportedOperationException.class)
public void feed_throwsExceptionIfFoodLevelIsAtMaxValue(){
 Monster testMonster = new Monster("Bubbles", 1);
 for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_FOOD_LEVEL); i++){
   testMonster.feed();
 }
}

//confirms we receive an exception if we attempt to raise our Monsters sleepLevel
@Test(expected = UnsupportedOperationException.class)
public void sleep_throwsExceptionIfSleepLevelIsAtMaxValue(){
  Monster testMonster = new Monster("Bubbles", 1);
  for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_SLEEP_LEVEL); i++){
    testMonster.sleep();
  }
}

//tests that throwing the above exception prevents sleepLevel from increasing beyond its constant
@Test
public void monster_sleepLevelCannotGoBeyondMaxValue(){
  Monster testMonster = new Monster("Bubbles", 1);
  for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_SLEEP_LEVEL); i++){
    try {
      testMonster.sleep();
    } catch (UnsupportedOperationException exception){ }
  }
  assertTrue(testMonster.getSleepLevel() <= Monster.MAX_SLEEP_LEVEL);
}

//saves Monster birthdate into database
@Test
public void save_recordsTimeOfCreationInDatabase() {
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.save();
  Timestamp savedMonsterBirthday = Monster.find(testMonster.getId()).getBirthday();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(rightNow.getDay(), savedMonsterBirthday.getDay());
}

//asserts the sleep() method accurately updates the lastSlept value in DB
@Test
public void sleep_recordsTimeLastSleptInDatabase() {
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.save();
  testMonster.sleep();
  Timestamp savedMonsterLastSlept = Monster.find(testMonster.getId()).getLastSlept();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedMonsterLastSlept));
}

//asserts the lastAte() method accurately updates value in DB
@Test
public void feed_recordsTimeLastAteInDatabase() {
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.save();
  testMonster.feed();
  Timestamp savedMonsterLastAte = Monster.find(testMonster.getId()).getLastAte();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedMonsterLastAte));
}

//ensure our play() method accurately updates the Monster's lastPlayed column in the database
@Test
public void play_recordsTimeLastPlayedInDatabase() {
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.save();
  testMonster.play();
  Timestamp savedMonsterLastPlayed = Monster.find(testMonster.getId()).getLastPlayed();
  Timestamp rightNow = new Timestamp(new Date().getTime());
  assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedMonsterLastPlayed));
}

//integrating a basic Timer
@Test
public void timer_executesDepleteLevelsMethod() {
  Monster testMonster = new Monster("Bubbles", 1);
  int firstPlayLevel = testMonster.getPlayLevel();
  testMonster.startTimer();
  try {
    Thread.sleep(6000);
  } catch (InterruptedException exception){}
  int secondPlayLevel = testMonster.getPlayLevel();
  assertTrue(firstPlayLevel > secondPlayLevel);
}

//tests that the timer halts when one of the Monster's levels reaches 0
@Test
public void timer_haltsAfterMonsterDies() {
  Monster testMonster = new Monster("Bubbles", 1);
  testMonster.startTimer();
  try {
    Thread.sleep(6000);
  } catch (InterruptedException exception){}
  assertFalse(testMonster.isAlive());
  assertTrue(testMonster.getFoodLevel() >= 0);
}


}
