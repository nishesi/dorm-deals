package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.master.party.dormdeals.dto.NotificationDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.NotificationService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDto>> getNotifications(@AuthenticationPrincipal
                                                                UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }

    @PutMapping("/notifications/{id}")
    public ResponseEntity<?> readNotification(@PathVariable("id") Long notificationId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        long userId = userDetails.getUser().getId();
        notificationService.readNotification(notificationId, userId);
        return ResponseEntity.accepted().build();
    }
}
