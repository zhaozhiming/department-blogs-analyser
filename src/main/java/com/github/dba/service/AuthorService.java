package com.github.dba.service;

import com.github.dba.model.Author;
import com.github.dba.repo.DepGroupRepository;
import com.github.dba.repo.DepMemberRepository;
import com.google.common.base.Strings;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private DepGroupRepository depGroupRepository;

    @Autowired
    private DepMemberRepository depMemberRepository;

    public Author fetchAuthor(Elements tags) {
        if (tags.size() == 0) return Author.defaultAuthor();

        String tag = tags.get(tags.size() - 1).text();
        if (Strings.isNullOrEmpty(tag)) return Author.defaultAuthor();

        String[] texts = tag.split("-");
        if (texts.length != 2) return Author.defaultAuthor();

        String groupName = depGroupRepository.findGroupFullNameByShort(texts[0]);
        String memberName = depMemberRepository.findMemberFullNameByShort(texts[1]);

        return new Author(groupName, memberName);
    }
}
