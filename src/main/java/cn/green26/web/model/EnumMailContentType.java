package cn.green26.web.model;

public enum EnumMailContentType {
    TEXT("text/plain;charset=utf-8"), HTML("text/html;charset=utf-8");
    private String contentType;

    EnumMailContentType(String contentType) {
        this.contentType = contentType;
    }

    public static String getEncodingByContentType(EnumMailContentType ct) {
        return ct.name();
    }
}
