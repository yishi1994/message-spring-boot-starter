package cn.green26.web.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlibabaPushMessage implements Serializable {
    private static final long serialVersionUID = -4289977932004745633L;
    private String title;
    private String body;
    /**
     * json类型的map
     */
    private String map;
}
