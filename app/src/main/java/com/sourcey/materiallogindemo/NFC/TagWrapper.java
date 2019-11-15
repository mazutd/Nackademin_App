package com.sourcey.materiallogindemo.NFC;

public class TagWrapper {
    private String id;
    public TagTechList techList = new TagTechList();

    public TagWrapper(final String tagId) {
        id = tagId;
    }

    public final String getId() {
        return id;
    }
}