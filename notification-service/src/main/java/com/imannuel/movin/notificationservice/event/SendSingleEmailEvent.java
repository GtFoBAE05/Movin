package com.imannuel.movin.notificationservice.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendSingleEmailEvent {
    private String target;

    private String subject;

    private String content;
}
