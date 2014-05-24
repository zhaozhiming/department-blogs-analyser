package com.github.dba.service;

import com.github.dba.model.Author;
import com.github.dba.model.DepGroup;
import com.github.dba.model.DepMember;
import com.github.dba.repo.read.DepGroupReadRepository;
import com.github.dba.repo.read.DepMemberReadRepository;
import com.google.common.base.Strings;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private DepGroupReadRepository depGroupReadRepository;

    @Autowired
    private DepMemberReadRepository depMemberReadRepository;

    public Author fetchAuthor(Elements tags) {
        if (tags.size() == 0) return Author.defaultAuthor();

        String tag = tags.get(tags.size() - 1).text();
        if (Strings.isNullOrEmpty(tag)) return Author.defaultAuthor();

        String[] texts = tag.toUpperCase().split("-");
        if (texts.length != 2) return Author.defaultAuthor();

        DepGroup group = depGroupReadRepository.findByGroupShort(texts[0]);
        String groupName = group != null ? group.getName() : "unknown";

        DepMember member = depMemberReadRepository.findByMemberShort(texts[1], texts[0]);
        String memberName = member != null ? member.getName() : "unknown";

        return new Author(groupName, memberName);
    }
}
