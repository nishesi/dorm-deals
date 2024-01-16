package ru.itis.master.party.dormdeals.repositories.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.jpa.Dormitory;

import java.util.List;

public interface DormitoryRepository extends CrudRepository<Dormitory, Long> {
    List<Dormitory> findByIdIn(List<Long> dormitoryIds);
}
