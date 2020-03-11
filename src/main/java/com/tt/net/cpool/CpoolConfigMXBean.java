package com.tt.net.cpool;

public interface CpoolConfigMXBean
{
    long getConnectionTimeout();

    void setConnectionTimeout(long connectoinTimeoutMs);

    long getValidationTimeout();

    long getIdleTimeout();

    void setIdleTimeout(long idleTimeoutMs);

    long getLeakDetectionThreshold();

    void setLeakDetectionThreshold();

    long getMaxLifetime();

    void setMaxLifetime(long maxLifetimeMs);

    int getMinimumIdle();

    void setMinimumIdel(int minIdel);

    int getMaximumPoolSize();

    void setPassword(String password);

    void setUsername(String username);

    String getPoolname();

    String getCatalog();

    void setCatalog(String catalog);
}
