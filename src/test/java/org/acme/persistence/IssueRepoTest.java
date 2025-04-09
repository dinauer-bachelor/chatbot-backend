package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.persistence.entity.SimilarIssue;
import org.acme.persistence.repo.IssueRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class IssueRepoTest
{
    @Inject
    IssueRepo issueRepo;

    @Test
    void testFindSimilarIssues()
    {
        List<SimilarIssue> similar = issueRepo.findSimilarByKey("FMN-4");

        System.out.println(similar);
    }
}
