package homework.homework1.message;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@With
public class Message {
    private int cType; //код команди
    private int userId;
    private byte[] messageBody;
}