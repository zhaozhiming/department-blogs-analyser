package com.github.dba.service;

import com.github.dba.model.Author;
import com.github.dba.model.DepGroup;
import com.github.dba.model.DepMember;
import com.github.dba.repo.DepGroupRepository;
import com.github.dba.repo.DepMemberRepository1;
import com.google.common.base.Strings;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private DepGroupRepository depGroupRepository;

    @Autowired
    private DepMemberRepository1 depMemberRepository;

    public Author fetchAuthor(Elements tags) {
        if (tags.size() == 0) return Author.defaultAuthor();

        String tag = tags.get(tags.size() - 1).text();
        if (Strings.isNullOrEmpty(tag)) return Author.defaultAuthor();

        String[] texts = tag.split("-");
        if (texts.length != 2) return Author.defaultAuthor();

        DepGroup group = depGroupRepository.findByGroupShort(texts[0]);
        String groupName = group != null ? group.getName() : "unknown";


        DepMember member = depMemberRepository.findByMemberShort(texts[1]);
        String memberName = member != null ? member.getName() : "unknown";

        return new Author(groupName, memberName);
    }
}
