package com.tt.net.cpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class CpoolConfig
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CpoolConfig.class);
    private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final long CONNECTION_TIMEOUT = SECONDS.toMillis(30);
    private static final long VALIDATION_TIMEOUT = SECONDS.toMillis(5);
    private static final long IDLE_TIMEOUT = MINUTES.toMillis(10);
    private static final long MAX_LIFETIME = MINUTES.toMillis(30);
    private static final int DEFAULT_POOL_SIZE = 10;

    private static boolean unitTest = false;

    private volatile String catalog;
    private volatile long connectionTimeout;
    private volatile long validationTimeout;
    private volatile long idleTimeout;
    private volatile long leakDetectionThreshold;
    private volatile long maxLifetime;
    private volatile int maxPoolSize;
    private volatile int minIdle;
    private volatile String username;
    private volatile String password;

    private long initializationFailTimeout;
    private String connectionInitSql;
    private String connectionTestQuery;
    private String dataSourceClassname;
    private String dataSourceJdinName;
    private String driverClassName;
    private String exceptionOverrideClassName;
    private String jdbcUrl;
    private String poolName;
    private String schema;
    private String transactionIsolationName;
    private boolean isAutoCommit;
    private boolean isReadOnly;
    private boolean isIsolateInternalQueries;
    private boolean isRegisterMbeans;
    private boolean isAllowPoolSuspension;
    private DataSource dataSource;
    private Properties dataSourceProperties;
    private ThreadFactory threadFactory;
    private ScheduledExecutorService scheduledExecutor;

    private Object metriRegistry;
    private Object healthCheckRegistry;
    private Properties healthCheckProperties;

    public CpoolConfig()
    {
        dataSourceProperties = new Properties();
        healthCheckProperties = new Properties();

        minIdle = -1;
        maxPoolSize = -1;
        maxLifetime = MAX_LIFETIME;
        connectionTimeout = CONNECTION_TIMEOUT;
        validationTimeout = VALIDATION_TIMEOUT;
        idleTimeout = IDLE_TIMEOUT;
        initializationFailTimeout = 1;
        isAutoCommit = true;

        String systemProp = System.getProperty("cpool.configurationFile");
        if ( systemProp != null ) {
            loadProperties(systemProp);
        }
    }

    private void loadProperties(String propertyFileName) {
        final File propFile = new File(propertyFileName);
        try ( final InputStream is = propFile.isFile() ? new FileInputStream(propFile) : this.getClass().getResourceAsStream(propertyFileName) ) {
            if ( is != null ) {
                Properties props = new Properties();
                props.load(is);

            }
            else {
                throw new IllegalArgumentException("Cannot find property file: " + propertyFileName);
            }
        }
        catch ( IOException io ) {
            throw new RuntimeException("Failed to read property file", io);
        }
    }

}
