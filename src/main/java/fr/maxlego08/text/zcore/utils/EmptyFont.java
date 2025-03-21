package fr.maxlego08.text.zcore.utils;

import fr.maxlego08.text.api.font.FontImage;

public class EmptyFont implements FontImage {
    @Override
    public String replace(String string) {
        return string;
    }
}
