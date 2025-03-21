package org.acme.persistence.repo;

import com.arcadedb.graph.Vertex;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.persistence.entity.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class IssueRepo
{
    private static final Logger LOG = LoggerFactory.getLogger(IssueRepo.class);

    private final static String LATEST_STATE = "latest_issue_state";

    private final static String HAS_STATE = "has_issue_state";

    @Inject
    Database database;

    public Optional<Vertex> exists(String key) {
        String sql = "SELECT FROM issue_id WHERE key = :key;";
        try(RemoteDatabase remoteDB = database.get()) {
            ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("key", key)));
            if(rs.hasNext())
            {
                return rs.next().getVertex();
            } else
            {
                return Optional.empty();
            }
        }
    }

    public Optional<Issue> findById(String key)
    {
        Optional<Vertex> x = exists(key);
        if(x.isPresent()) {
            String sql = String.format("SELECT expand(@in) FROM %s WHERE @out = :out;", LATEST_STATE);
            try(RemoteDatabase remoteDB = database.get())
            {
                ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("out", x.get().getIdentity())));
                return resultSetToOptionalIssue(rs);
            }
        }
        return Optional.empty();
    }

    public Optional<Map<ZonedDateTime, Issue>> findHistoryById(String key)
    {
        Optional<Vertex> x = exists(key);
        if(x.isPresent()) {
            String sql = String.format("SELECT expand(@in) FROM %s WHERE @out = :out;", HAS_STATE);
            try(RemoteDatabase remoteDB = database.get())
            {
                ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("out", x.get().getIdentity())));
                Map<ZonedDateTime, Issue> history = new HashMap<>();
                while(rs.hasNext())
                {
                    Result result = rs.next();
                    history.put(ZonedDateTime.parse(result.getProperty("inserted_at"), DateTimeFormatter.ISO_DATE_TIME), resultToIssue(result));
                }
                return Optional.of(history);
            }
        }
        return Optional.empty();
    }

    public List<Issue> findByWhere(String where)
    {
        String sql = String.format("SELECT FROM (TRAVERSE out() FROM (TRAVERSE out() FROM project_id) MAXDEPTH 5) WHERE @type = 'issue' AND (%s)", where);
        try(RemoteDatabase remoteDB = database.get())
        {
            LOG.info("Executing SQL {}", sql);
            ResultSet rs = remoteDB.command("sql", sql);
            List<Issue> issues = rs.stream().map(this::resultToIssue).toList();
            LOG.info("Found {} result(s).", issues.size());
            return issues;
        }
    }

    private Optional<Issue> resultSetToOptionalIssue(ResultSet resultSet)
    {
        if(resultSet.hasNext())
        {
            Result result = resultSet.next();
            if(result.getIdentity().isPresent())
            {
                return Optional.of(resultToIssue(result));
            }
        }
        return Optional.empty();
    }

    private Issue resultToIssue(Result result)
    {
        Issue issue =  new Issue();
        issue.setKey(result.getProperty("key"));
        issue.setProjectKey(result.getProperty("project_key"));
        issue.setSummary(result.getProperty("summary"));
        issue.setDescription(result.getProperty("description"));
        issue.setStatus(result.getProperty("status"));
        issue.setIssuetype(result.getProperty("issuetype"));
        issue.setAssignee(result.getProperty("assignee"));
        issue.setReporter(result.getProperty("reporter"));
        return issue;
    }
}
