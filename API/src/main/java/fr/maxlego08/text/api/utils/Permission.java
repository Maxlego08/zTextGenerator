package fr.maxlego08.text.api.utils;

public enum Permission {

    ZTEXTGENERATOR_USE,
    ZTEXTGENERATOR_RELOAD,

    ZTEXTGENERATOR_BOOK,
    ZTEXTGENERATOR_TEXT;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String asPermission() {
        return permission;
    }

}