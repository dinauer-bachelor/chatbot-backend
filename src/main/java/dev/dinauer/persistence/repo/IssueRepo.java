package dev.dinauer.persistence.repo;

import com.arcadedb.graph.Edge;
import com.arcadedb.graph.Vertex;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import dev.dinauer.persistence.entity.Issue;
import dev.dinauer.persistence.entity.SimilarIssue;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class IssueRepo
{
    private static final Logger LOG = LoggerFactory.getLogger(IssueRepo.class);

    private final static String VERTEX_TYPE = "issue";

    private final static String LATEST_STATE = "latest_issue_state";

    private final static String HAS_STATE = "has_issue_state";

    @Inject
    Database database;

    public Optional<Vertex> exists(String key)
    {
        String sql = "SELECT FROM issue_id WHERE key = :key;";
        try (RemoteDatabase remoteDB = database.get())
        {
            ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("key", key)));
            if (rs.hasNext())
            {
                return rs.next().getVertex();
            }
            else
            {
                return Optional.empty();
            }
        }
    }

    public Optional<Issue> findById(String key)
    {
        Optional<Vertex> x = exists(key);
        if (x.isPresent())
        {
            String sql = String.format("SELECT expand(@in) FROM %s WHERE @out = :out;", LATEST_STATE);
            try (RemoteDatabase remoteDB = database.get())
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
        if (x.isPresent())
        {
            String sql = String.format("SELECT expand(@in) FROM %s WHERE @out = :out;", HAS_STATE);
            try (RemoteDatabase remoteDB = database.get())
            {
                ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("out", x.get().getIdentity())));
                Map<ZonedDateTime, Issue> history = new HashMap<>();
                while (rs.hasNext())
                {
                    Result result = rs.next();
                    history.put(ZonedDateTime.parse(result.getProperty("inserted_at"), DateTimeFormatter.ISO_DATE_TIME), resultToIssue(result));
                }
                return Optional.of(history);
            }
        }
        return Optional.empty();
    }

    public List<Issue> findByWhere(String where, Integer limit)
    {
        String sql = String.format("SELECT FROM (TRAVERSE out() FROM (TRAVERSE out() FROM project_id) MAXDEPTH 5) WHERE @type = 'issue' AND (%s)", where);
        try (RemoteDatabase remoteDB = database.get())
        {
            LOG.info("Executing SQL {}", sql);
            ResultSet rs = remoteDB.command(Database.SQL, sql);
            List<Issue> issues = rs.stream().map(this::resultToIssue).toList();
            if (limit != null)
            {
                return issues.stream().limit(limit).toList();
            }
            return issues;
        }
    }

    public List<SimilarIssue> findSimilarByKey(String key)
    {
        Optional<Vertex> optionalSimilarId = exists(key);
        if (optionalSimilarId.isPresent())
        {
            Vertex similarId = optionalSimilarId.get();
            try (RemoteDatabase remoteDB = database.get())
            {
                List<SimilarIssue> similarities = new ArrayList<>();
                Vertex similarIdActive = remoteDB.lookupByRID(similarId.getIdentity()).asVertex();
                Iterator<Edge> similarityEdges = similarIdActive.getEdges(Vertex.DIRECTION.OUT, "is_similar_to").iterator();
                while (similarityEdges.hasNext())
                {
                    Edge edge = similarityEdges.next();
                    Iterator<Edge> edgesToLatestOther = edge.getInVertex().getEdges(Vertex.DIRECTION.OUT, LATEST_STATE).iterator();
                    if (edgesToLatestOther.hasNext())
                    {
                        Edge edge1 = edgesToLatestOther.next();
                        Vertex otherIssue = edge1.getInVertex();
                        SimilarIssue similarIssue = new SimilarIssue();
                        similarIssue.setSimilarity(edge.getDouble("similarity"));
                        similarIssue.setIssue(vertexToIssue(otherIssue).orElse(null));
                        similarities.add(similarIssue);
                    }
                }
                return similarities;
            }
        }
        throw new NoSuchElementException();
    }

    private Optional<Issue> resultSetToOptionalIssue(ResultSet resultSet)
    {
        if (resultSet.hasNext())
        {
            Result result = resultSet.next();
            if (result.getIdentity().isPresent())
            {
                return Optional.of(resultToIssue(result));
            }
        }
        return Optional.empty();
    }

    private Optional<Issue> vertexToIssue(Vertex vertex)
    {
        if (VERTEX_TYPE.equals(vertex.getTypeName()))
        {
            Issue issue = new Issue();
            issue.setKey(vertex.getString("key"));
            issue.setProjectKey(vertex.getString("project_key"));
            issue.setSummary(vertex.getString("summary"));
            issue.setDescription(vertex.getString("description"));
            issue.setStatus(vertex.getString("status"));
            issue.setIssuetype(vertex.getString("issuetype"));
            issue.setAssignee(vertex.getString("assignee"));
            issue.setReporter(vertex.getString("reporter"));
            return Optional.of(issue);
        }
        return Optional.empty();
    }

    private Issue resultToIssue(Result result)
    {
        Issue issue = new Issue();
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
