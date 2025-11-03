package fr.maxlego08.text.api;

public enum FontType {

    ITEMSADDER(":%format%:", ":offset_%pixels%:"),
    NEXO("<glyph_%format%>", "<shift:%pixels%>"),
    ORAXEN("<glyph_%format%>", "<shift:%pixels%>"),

    ;

    private final String format;
    private final String offset;

    FontType(String format, String offset) {
        this.format = format;
        this.offset = offset;
    }

    public String getFormat() {
        return this.format;
    }

    public String getFormat(String format) {
        return this.format.replace("%format%", format);
    }

    public String getOffset() {
        return this.offset;
    }

    public String getOffset(int pixels) {
        return this.offset.replace("%pixels%", String.valueOf(pixels));
    }
}
