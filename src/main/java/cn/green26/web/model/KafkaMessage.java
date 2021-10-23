package cn.green26.web.model;

import lombok.Data;

@Data
public class KafkaMessage {
    private String message;
    private long datetime;
}
