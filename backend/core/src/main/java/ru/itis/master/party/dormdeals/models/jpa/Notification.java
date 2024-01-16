package ru.itis.master.party.dormdeals.models.jpa;


import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.AbstractEntity;

import java.time.ZonedDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @ToString.Exclude
    private Shop sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @ToString.Exclude
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


