package cn.green26.web.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternalMessage implements Serializable {
    private static final long serialVersionUID = -7424145544728227111L;
    private String title;
    private String body;
    private long datetime;
}
