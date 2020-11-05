package ru.otus.messagesystem.message.model;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
public class MessageClientId {
    private String name;
    private final String host = "http://localhost";
    @Setter
    private String port;

    public MessageClientId(String clientName) {
        this.name = clientName;
    }

    public String getSocket() {
        return host + ":" + port;
    }
}
