package org.acme.persistence.entity;

public class Comment
{
    private String id;
    private String issueKey;
    private String projectKey;
    private String text;
    private String author;

    public String getId()
    {
        return id;
    }

    public Comment setId(String id)
    {
        this.id = id;
        return this;
    }

    public String getIssueKey()
    {
        return issueKey;
    }

    public Comment setIssueKey(String issueKey)
    {
        this.issueKey = issueKey;
        return this;
    }

    public String getProjectKey()
    {
        return projectKey;
    }

    public Comment setProjectKey(String projectKey)
    {
        this.projectKey = projectKey;
        return this;
    }

    public String getText()
    {
        return text;
    }

    public Comment setText(String text)
    {
        this.text = text;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Comment setAuthor(String author) {
        this.author = author;
        return this;
    }
}
