package cn.green26.web.model;

public enum EnumMailContentType {
    TEXT("text/plain;charset=utf-8"), HTML("text/html;charset=utf-8");
    private final String contentType;

    public String getContentType() {
        return contentType;
    }

    EnumMailContentType(String contentType) {
        this.contentType = contentType;
    }

    public static String getValueByCode(String name) {
        for (EnumMailContentType obj : EnumMailContentType.values()) {
            if (name.equals(obj.name())) {
                return obj.getContentType();
            }
        }
        return null;
    }
}
