package cn.green26.web.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailMessage implements Serializable {
    private static final long serialVersionUID = 5718760092224561076L;
    private String subject;
    private String body;
    private EnumMailContentType contentType = EnumMailContentType.HTML;
}
