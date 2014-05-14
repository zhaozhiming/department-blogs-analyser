package com.github.dba.model;

public class Author {
    private final String group;
    private final String name;

    public Author(String source) {
        String[] texts = source.split("-");
        if (texts.length != 2) {
            this.group = Group.W.group();
            this.name = "赵芝明";
        } else {
            this.group = fetchGroupName(texts[0]);
            this.name = texts[1];
        }
    }

    public static Author defaultAuthor() {
        return new Author("W-赵芝明");
    }


    private String fetchGroupName(String groupShort) {
        return Group.valueOf(groupShort).group();
    }

    @Override
    public String toString() {
        return String.format("Author{name='%s', group='%s'}", name, group);
    }
}
