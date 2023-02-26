package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.Test;

public interface TestRepo extends CrudRepository<Test, Long> {
}
