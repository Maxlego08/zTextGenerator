package fr.maxlego08.text.api.utils;

public enum Permission {

    ZTEXTGENERATOR_USE,
    ZTEXTGENERATOR_RELOAD,

    ZTEXTGENERATOR_BOOK,
    ZTEXTGENERATOR_OPEN, ZTEXTGENERATOR_TEST_ALPHABET;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String asPermission() {
        return permission;
    }

}