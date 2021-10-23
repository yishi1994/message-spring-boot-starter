package cn.green26.web.model;

import cn.green26.web.service.IReceiver;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class InternalMessageReceiver implements IReceiver, Serializable {
    private static final long serialVersionUID = -7066826155769212155L;
    private Set<Long> userId;
}