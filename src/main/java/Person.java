public class Person {
  private String name;
  private String email;

  public Person(String name, String email) {
    this.name = name;
    this.email = email;
  }

//gets name property
  public String getName() {
    return name;
  }

//gets email property
  public String getEmail() {
    return email;
  }

//overrides equals
@Override
public boolean equals(Object otherPerson){
if (!(otherPerson instanceof Person)) {
  return false;
} else {
  Person newPerson = (Person) otherPerson;
  return this.getName().equals(newPerson.getName()) &&
         this.getEmail().equals(newPerson.getEmail());
}
}

}
