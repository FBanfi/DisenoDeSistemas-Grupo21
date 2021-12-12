package domain.persistence;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class EntidadPersistente {

  @Id
  @GeneratedValue
  private int id;

  public int getId() { return id; }

  public void setId(int id) {
    this.id = id;
  }

}
