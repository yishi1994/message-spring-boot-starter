package cn.green26.web.model;

import cn.green26.web.service.IReceiver;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class KafkaMessageReceiver implements IReceiver, Serializable {
    private static final long serialVersionUID = -9050387729967375544L;
    private Set<Long> id;
}