package ru.itis.master.party.dormdeals.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.master.party.dormdeals.models.jpa.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.receiver.id = :userId and n.receiverRead = false")
    Integer getCountByReceiverId(@Param("userId") long userId);
    List<Notification> findAllByReceiverId(long userId);
}
