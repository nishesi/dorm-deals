package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.Dormitory;

import java.util.List;

public interface DormitoryRepository extends CrudRepository<Dormitory, Long> {
    List<Dormitory> findByIdIn(List<Long> dormitoryIds);
}
