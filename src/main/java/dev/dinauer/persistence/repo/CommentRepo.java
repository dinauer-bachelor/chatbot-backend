package dev.dinauer.persistence.repo;

import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import dev.dinauer.persistence.entity.Comment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CommentRepo
{
    private final static String VERTEX_NAME = "comment";

    private final static String LATEST_STATE = "latest_comment_state";

    private final static String HAS_STATE = "has_comment_state";

    @Inject
    Database database;

    public List<Comment> getLatestCommentsByIssueKey(String issueKey)
    {
        String sql = String.format("SELECT FROM (TRAVERSE out('%s') FROM (TRAVERSE out() FROM (SELECT FROM issue_id WHERE key=:key))) WHERE @type='%s'", LATEST_STATE, VERTEX_NAME);
        try (RemoteDatabase remoteDB = database.get())
        {
            ResultSet rs = remoteDB.command(Database.SQL, sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("key", issueKey)));
            List<Comment> comments = new LinkedList<>();
            while (rs.hasNext())
            {
                Result result = rs.next();
                Comment comment = new Comment();
                comment.setId(result.getProperty("id"));
                comment.setText(result.getProperty("text"));
                comment.setIssueKey(result.getProperty("issue_key"));
                comment.setAuthor(result.getProperty("author"));
                comments.add(comment);
            }
            return comments;
        }
    }
}
