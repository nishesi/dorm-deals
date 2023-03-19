package ru.itis.master.party.dormdeals.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.services.UserService;

@Component
@RequiredArgsConstructor
public class ScheduledDeleteUsers {

    private final UserService userService;

    @Scheduled(cron = "0 0 0 * * ?")
    private void deleteUnconfirmedUsers() {
        userService.deleteUnconfirmedUsers();
    }
}
