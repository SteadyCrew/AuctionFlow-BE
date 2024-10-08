package org.example.auctionflowbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 메세지를 보낸 사용자

    private String content;
    private LocalDateTime sendAt; // 전송한 시간
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;  // 메세지가 속한 채팅방


}
