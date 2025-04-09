package dev.dinauer.persistence.entity;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Project
{
    private String key;
    private String name;
    private String description;
    private ZonedDateTime insertedAt;

    public String getKey()
    {
        return key;
    }

    public Project setKey(String key)
    {
        this.key = key;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public Project setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getDescription()
    {
        if (description == null)
        {
            return "";
        }
        return description;
    }

    public Project setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public ZonedDateTime getInsertedAt()
    {
        return insertedAt;
    }

    public Project setInsertedAt(ZonedDateTime insertedAt)
    {
        this.insertedAt = insertedAt;
        return this;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Project that = (Project) object;
        return Objects.equals(key, that.key) && Objects.equals(name, that.name) && Objects.equals(getDescription(), that.getDescription());
    }
}
