package org.acme.persistence.repo;

import com.arcadedb.remote.RemoteDatabase;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Startup
@ApplicationScoped
public class Database
{
    private static final Logger LOG = LoggerFactory.getLogger(Database.class);

    public static final String SQL_SCRIPT = "sqlscript";
    public static final String SQL = "sqlscript";

    @ConfigProperty(name = "arcadedb.host")
    String host;

    @ConfigProperty(name = "arcadedb.port")
    Integer port;

    @ConfigProperty(name = "arcadedb.database")
    String database;

    @ConfigProperty(name = "arcadedb.user")
    String user;

    @ConfigProperty(name = "arcadedb.password")
    String password;

    public RemoteDatabase get()
    {
        return new RemoteDatabase(host, port, database, user, password);
    }

    @PostConstruct
    public void logConnection()
    {
        LOG.info("Using data source {}:{} and database {}", host, port, database);
    }
}
