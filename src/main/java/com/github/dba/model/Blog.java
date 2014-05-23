package com.github.dba.model;

import com.github.dba.util.DbaUtil;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;

import static java.lang.String.format;

@Entity(name = "blogs")
@Table(name = "blogs", uniqueConstraints = @UniqueConstraint(columnNames = {"blogId", "website"}))
public class Blog {
    private static final String PAGE_DATE_FORMAT = "yyyy-MM-dd";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Basic
    private String title;
    @Basic
    private String link;
    @Basic
    private int view;
    @Basic
    private int comment;
    @Basic
    private Long time;
    @Embedded
    private Author author;
    @Basic
    private String blogId;
    @Basic
    private String website;

    public Blog() {
    }

    public Blog(String title, String link, int view,
                int comment, Long time, Author author, String blogId, String website) {
        this.title = title;
        this.link = link;
        this.view = view;
        this.comment = comment;
        this.time = time;
        this.author = author;
        this.blogId = blogId;
        this.website = website;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return String.format("Blog{title='%s', link='%s', view=%d, comment=%d, " +
                "time=%d, author=%s, blogId='%s', website='%s'}",
                title, link, view, comment, time, author, blogId, website);
    }

    public static Specification<Blog> querySpecification(final String depGroup, final String website,
                                                         final String startDate, final String endDate) {
        return Specifications.where(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();

                if (!Strings.isNullOrEmpty(depGroup) && !"所有分组".equals(depGroup)) {
                    predicate.getExpressions().add(
                            cb.equal(root.<Author>get("author").<String>get("groupName"), depGroup));
                }

                if (!Strings.isNullOrEmpty(website) && !"所有".equals(website)) {
                    predicate.getExpressions().add(
                            cb.equal(root.<String>get("website"), website));
                }

                if (!Strings.isNullOrEmpty(startDate)) {
                    try {
                        long time = DbaUtil.parseTimeStringToLong(startDate, PAGE_DATE_FORMAT);
                        predicate.getExpressions().add(cb.ge(root.<Long>get("time"), time));
                    } catch (ParseException e) {
                        throw new RuntimeException(format("%s parse to date error:", startDate));
                    }
                }

                if (!Strings.isNullOrEmpty(endDate)) {
                    try {
                        long time = DbaUtil.parseTimeStringToLong(endDate, PAGE_DATE_FORMAT);
                        predicate.getExpressions().add(cb.le(root.<Long>get("time"), time));
                    } catch (ParseException e) {
                        throw new RuntimeException(format("%s parse to date error:", endDate));
                    }
                }

                return predicate;
            }
        });
    }
}
