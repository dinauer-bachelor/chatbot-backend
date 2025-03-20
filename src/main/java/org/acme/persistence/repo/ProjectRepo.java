package org.acme.persistence.repo;

import com.arcadedb.graph.MutableVertex;
import com.arcadedb.graph.Vertex;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.persistence.entity.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ProjectRepo
{
    private final static Logger LOG = LoggerFactory.getLogger(ProjectRepo.class);

    private final static String LATEST_STATE = "latest_project_state";

    @Inject
    Database database;

    public Optional<Vertex> exists(String key) {
        String sql = "SELECT FROM project_id WHERE key = :key;";
        try(RemoteDatabase remoteDB = database.get()) {
            ResultSet rs = remoteDB.command(Database.SQL, sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("key", key)));
            if(rs.hasNext())
            {
                return rs.next().getVertex();
            }
            return Optional.empty();
        }
    }

    public Optional<Project> findById(String key)
    {
        Optional<Vertex> x = exists(key);
        if(x.isPresent()) {
            String sql = String.format("SELECT expand(@in) FROM %s WHERE @out = :out;", LATEST_STATE);
            try(RemoteDatabase remoteDB = database.get())
            {
                ResultSet rs = remoteDB.command(Database.SQL, sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("out", x.get().getIdentity())));
                return resultSetToOptionalProject(rs);
            }
        }
        return Optional.empty();
    }

    public List<Project> findAll()
    {
        String sql = String.format("SELECT FROM (TRAVERSE out('%s') FROM project_id)", LATEST_STATE);
        try(RemoteDatabase remoteDB = database.get())
        {
            ResultSet rs = remoteDB.command(Database.SQL, sql);
            return rs.stream().map(result -> new Project().setKey(result.getProperty("key")).setName(result.getProperty("name")).setDescription(result.getProperty("description"))).toList();
        }
    }

    private Optional<Project> resultSetToOptionalProject(ResultSet resultSet)
    {
        if(resultSet.hasNext())
        {
            Result result = resultSet.next();
            if(result.getIdentity().isPresent())
            {
                Project project =  new Project();
                project.setKey(result.getProperty("key"));
                project.setName(result.getProperty("name"));
                project.setDescription(result.getProperty("description"));
                project.setInsertedAt(ZonedDateTime.parse(result.getProperty("inserted_at")));
                return Optional.of(project);
            }
        }
        return Optional.empty();
    }
}
