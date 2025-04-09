package dev.dinauer.persistence.entity;

public class SimilarIssue
{
    String similarity;
    Issue issue;

    public String getSimilarity()
    {
        return similarity;
    }

    public SimilarIssue setSimilarity(double similarity)
    {
        if (similarity < 0.1)
        {
            this.similarity = "Somewhat similar";
        }
        else if (similarity < 0.2)
        {
            this.similarity = "Rather similar";
        }
        else if (similarity < 0.3)
        {
            this.similarity = "Very similar. Maybe same task/bug/issue";
        }
        else if (similarity < 0.4)
        {
            this.similarity = "Super similar. Probably same task/bug/issue";
        }
        else if (similarity < 0.5)
        {
            this.similarity = "Extremely similar. Could be a duplicate";
        }
        else if (similarity >= 0.5)
        {
            this.similarity = "This is most likely a duplicate";
        }
        return this;
    }

    public Issue getIssue()
    {
        return issue;
    }

    public SimilarIssue setIssue(Issue issue)
    {
        this.issue = issue;
        return this;
    }
}
