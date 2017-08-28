import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;

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


}
