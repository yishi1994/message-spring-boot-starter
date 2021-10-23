package cn.green26.web.model;

import cn.green26.web.service.IReceiver;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class MailReceiver implements IReceiver, Serializable {
    private static final long serialVersionUID = 4515994124758260873L;
    private Set<String> to;
    private Set<String> cc;
    private Set<String> bcc;
}
