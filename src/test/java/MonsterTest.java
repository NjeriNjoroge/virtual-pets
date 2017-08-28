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

}
