package org.acme.persistence.repo;

import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Database
{
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
}
