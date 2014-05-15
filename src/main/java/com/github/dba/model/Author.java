package com.github.dba.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Author {
    private static final Log log = LogFactory.getLog(Author.class);
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
        try {
            return Group.valueOf(groupShort).group();
        }catch (Exception e) {
            log.debug("not group match the name");
            return Group.W.group();
        }
    }

    @Override
    public String toString() {
        return String.format("Author{name='%s', group='%s'}", name, group);
    }
}
