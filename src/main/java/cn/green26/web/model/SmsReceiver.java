package cn.green26.web.model;

import cn.green26.web.service.IReceiver;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class SmsReceiver implements IReceiver, Serializable {
    private static final long serialVersionUID = -6334340283319360364L;
    private Set<Long> mobiles;
}