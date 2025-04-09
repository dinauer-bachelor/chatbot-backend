package dev.dinauer.persistence.entity;

import java.time.ZonedDateTime;
import java.util.List;

public class Issue
{
    private String key;
    private String summary;
    private String description;
    private ZonedDateTime createdAt;
    private String issuetype;
    private String status;
    private List<String> components;
    private String projectKey;
    private String assignee;
    private String reporter;

    public String getKey()
    {
        return key;
    }

    public Issue setKey(String key)
    {
        this.key = key;
        return this;
    }

    public String getSummary()
    {
        return summary;
    }

    public Issue setSummary(String summary)
    {
        this.summary = summary;
        return this;
    }

    public String getDescription()
    {
        return description;
    }

    public Issue setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public ZonedDateTime getCreatedAt()
    {
        return createdAt;
    }

    public Issue setCreatedAt(ZonedDateTime createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }

    public String getIssuetype()
    {
        return issuetype;
    }

    public Issue setIssuetype(String issuetype)
    {
        this.issuetype = issuetype;
        return this;
    }

    public String getStatus()
    {
        return status;
    }

    public Issue setStatus(String status)
    {
        this.status = status;
        return this;
    }

    public List<String> getComponents()
    {
        return components;
    }

    public Issue setComponents(List<String> components)
    {
        this.components = components;
        return this;
    }

    public String getProjectKey()
    {
        return projectKey;
    }

    public Issue setProjectKey(String projectKey)
    {
        this.projectKey = projectKey;
        return this;
    }

    public String getAssignee()
    {
        return assignee;
    }

    public Issue setAssignee(String assignee)
    {
        this.assignee = assignee;
        return this;
    }

    public String getReporter()
    {
        return reporter;
    }

    public Issue setReporter(String reporter)
    {
        this.reporter = reporter;
        return this;
    }
}
