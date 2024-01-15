package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Shop sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @Enumerated(EnumType.STRING)
    private Type notificationType;
    private String message;
    private Boolean receiverRead;
    private ZonedDateTime createdDateTime;

    public enum Type {
        ORDER_NOTIFICATION,
        CUSTOM_NOTIFICATION
    }
}


