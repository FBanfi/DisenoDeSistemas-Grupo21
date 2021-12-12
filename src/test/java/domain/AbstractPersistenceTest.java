package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public abstract class AbstractPersistenceTest implements WithGlobalEntityManager, TransactionalOps {

  @BeforeEach
  public void antes() {
    beginTransaction();
  }

  @AfterEach
  public void despues() {
    rollbackTransaction();
  }
}
