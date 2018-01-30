package org.testcontainers.containers;

import org.rnorth.ducttape.ratelimits.RateLimiter;
import org.rnorth.ducttape.ratelimits.RateLimiterBuilder;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author gusohal
 */
public class OracleContainer<SELF extends JdbcDatabaseContainer<SELF>> extends JdbcDatabaseContainer<SELF> {

    public static final String NAME = "oracle";
    public static final String IMAGE = TestcontainersConfiguration.getInstance()
            .getProperties().getProperty("oracle.container.image","wnameless/oracle-xe-11g");
    private static final int ORACLE_PORT = 1521;
    private static final int APEX_HTTP_PORT = 8080;

    private static final RateLimiter DB_CONNECT_RATE_LIMIT = RateLimiterBuilder.newBuilder()
            .withRate(10, TimeUnit.SECONDS)
            .withConstantThroughput()
            .build();

    public OracleContainer() {
        super(IMAGE + ":latest");
    }

    public OracleContainer(String dockerImageName) {
        super(dockerImageName);
    }

    @Override
    protected Integer getLivenessCheckPort() {
        return getMappedPort(ORACLE_PORT);
    }

    @Override
    protected void configure() {
        addExposedPorts(ORACLE_PORT, APEX_HTTP_PORT);
    }

    @Override
    public String getDriverClassName() {
        return "oracle.jdbc.OracleDriver";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:oracle:thin:" + getUsername() + "/" + getPassword() + "@//" + getContainerIpAddress() + ":" + getOraclePort() + "/" + getSid();
    }

    @Override
    public String getUsername() {
        return "system";
    }

    @Override
    public String getPassword() {
        return "oracle";
    }

    @SuppressWarnings("SameReturnValue")
    public String getSid() {
        return "xe";
    }

    public Integer getOraclePort() {
        return getMappedPort(ORACLE_PORT);
    }

    @SuppressWarnings("unused")
    public Integer getWebPort() {
        return getMappedPort(APEX_HTTP_PORT);
    }

    @Override
    public String getTestQueryString() {
        return "SELECT 1 FROM DUAL";
    }

    // TODO: Replace with permanent solution to https://github.com/testcontainers/testcontainers-java/issues/568
    @Override
    public Connection createConnection(String queryString) throws SQLException {
        final Properties info = new Properties();
        info.put("user", this.getUsername());
        info.put("password", this.getPassword());
        final String url = this.getJdbcUrl() + queryString;

        final Driver jdbcDriverInstance = getJdbcDriverInstance();

        try {
            return Unreliables.retryUntilSuccess(120, TimeUnit.SECONDS, () -> jdbcDriverInstance.connect(url, info));
        } catch (Exception e) {
            throw new SQLException("Could not create new connection", e);
        }
    }
}