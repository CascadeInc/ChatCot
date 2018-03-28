package by.cascade.chatcot.storage.databaseprocessing.util;

import by.cascade.chatcot.storage.ConnectorException;
import by.cascade.chatcot.storage.ConnectorInfoException;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ProxyConnection implements Connection {
    private static int number = 0;
    private String name;
    private Connection connection;

    ProxyConnection(Connection connection) {
        this.connection = connection;
        this.name = "Proxy Connection number " + number;
        number++;
    }

    @Override
    public void close() throws ConnectorException {
        ConnectionPool.getInstance().releaseConnection(this);
    }

    void closeConnection() throws ConnectorException {
        try {
            connection.close();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't close connection", e);
        }
    }

    @Override
    public Statement createStatement() throws ConnectorException {
        try {
            return connection.createStatement();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't create statement", e);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws ConnectorException {
        try {
            return connection.prepareStatement(sql);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't prepare statement", e);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql) throws ConnectorException {
        try {
            return connection.prepareCall(sql);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't prepare call", e);
        }
    }

    @Override
    public String nativeSQL(String sql) throws ConnectorException {
        try {
            return connection.nativeSQL(sql);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't transform to native SQL", e);
        }
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws ConnectorException {
        try {
            connection.setAutoCommit(autoCommit);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't setting auto commit", e);
        }
    }

    @Override
    public boolean getAutoCommit() throws ConnectorException {
        try {
            return connection.getAutoCommit();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting auto commit", e);
        }
    }

    @Override
    public void commit() throws ConnectorException {
        try {
            connection.commit();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't commit", e);
        }
    }

    @Override
    public void rollback() throws ConnectorException {
        try {
            connection.rollback();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't rollback", e);
        }
    }

    @Override
    public boolean isClosed() throws ConnectorException {
        try {
            return connection.isClosed();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting is closed", e);
        }
    }

    @Override
    public DatabaseMetaData getMetaData() throws ConnectorException {
        try {
            return connection.getMetaData();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting meta data", e);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) throws ConnectorException {
        try {
            connection.setReadOnly(readOnly);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't set is read only", e);
        }
    }

    @Override
    public boolean isReadOnly() throws ConnectorException {
        try {
            return connection.isReadOnly();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting is read only", e);
        }
    }

    @Override
    public void setCatalog(String catalog) throws ConnectorException {
        try {
            connection.setCatalog(catalog);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't set catalog", e);
        }
    }

    @Override
    public String getCatalog() throws ConnectorException {
        try {
            return connection.getCatalog();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't get catalog", e);
        }
    }

    @Override
    public void setTransactionIsolation(int level) throws ConnectorException {
        try {
            connection.setTransactionIsolation(level);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't set transaction isolation", e);
        }
    }

    @Override
    public int getTransactionIsolation() throws ConnectorException {
        try {
            return connection.getTransactionIsolation();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't get transaction isolation", e);
        }
    }

    @Override
    public SQLWarning getWarnings() throws ConnectorException {
        try {
            return connection.getWarnings();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting warnings", e);
        }
    }

    @Override
    public void clearWarnings() throws ConnectorException {
        try {
            connection.clearWarnings();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't clear warnings", e);
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws ConnectorException {
        try {
            return connection.createStatement(resultSetType, resultSetConcurrency);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't create statement", e);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws ConnectorException {
        try {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't prepare statement", e);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws ConnectorException {
        try {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't prepare call", e);
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws ConnectorException {
        try {
            return connection.getTypeMap();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting type map", e);
        }
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws ConnectorException {
        try {
            connection.setTypeMap(map);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't setting type map", e);
        }
    }

    @Override
    public void setHoldability(int holdability) throws ConnectorException {
        try {
            connection.setHoldability(holdability);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't setting holdability", e);
        }
    }

    @Override
    public int getHoldability() throws ConnectorException {
        try {
            return connection.getHoldability();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting holdability", e);
        }
    }

    @Override
    public Savepoint setSavepoint() throws ConnectorException {
        try {
            return connection.setSavepoint();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't setting savepoint", e);
        }
    }

    @Override
    public Savepoint setSavepoint(String name) throws ConnectorException {
        try {
            return connection.setSavepoint(name);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't setting savepoint", e);
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws ConnectorException {
        try {
            connection.rollback();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't rollback", e);
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws ConnectorException {
        try {
            connection.releaseSavepoint(savepoint);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't release savepoint", e);
        }

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws ConnectorException {
        try {
            return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't creating statement", e);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws ConnectorException {
        try {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't prepare statement", e);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws ConnectorException {
        try {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't prepare call", e);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws ConnectorException {
        try {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't prepare statement", e);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws ConnectorException {
        try {
            return connection.prepareStatement(sql, columnIndexes);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't prepare statement", e);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws ConnectorException {
        try {
            return connection.prepareStatement(sql, columnNames);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't prepare statement", e);
        }
    }

    @Override
    public Clob createClob() throws ConnectorException {
        try {
            return connection.createClob();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't create clob", e);
        }
    }

    @Override
    public Blob createBlob() throws ConnectorException {
        try {
            return connection.createBlob();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't create blob", e);
        }
    }

    @Override
    public NClob createNClob() throws ConnectorException {
        try {
            return connection.createNClob();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't create nclob", e);
        }
    }

    @Override
    public SQLXML createSQLXML() throws ConnectorException {
        try {
            return connection.createSQLXML();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't create sql xml", e);
        }
    }

    @Override
    public boolean isValid(int timeout) throws ConnectorException {
        try {
            return connection.isValid(timeout);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting is valid", e);
        }
    }

    @Override
    public void setClientInfo(String name, String value) throws ConnectorInfoException {
        try {
            connection.setClientInfo(name, value);
        }
        catch (SQLClientInfoException e) {
            throw new ConnectorInfoException("can't set client info", e);
        }
    }

    @Override
    public void setClientInfo(Properties properties) throws ConnectorInfoException {
        try {
            connection.setClientInfo(properties);
        }
        catch (SQLClientInfoException e) {
            throw new ConnectorInfoException("can't set client info", e);
        }
    }

    @Override
    public String getClientInfo(String name) throws ConnectorException {
        try {
            return connection.getClientInfo(name);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting client info", e);
        }
    }

    @Override
    public Properties getClientInfo() throws ConnectorException {
        try {
            return connection.getClientInfo();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting client info", e);
        }
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws ConnectorException {
        try {
            return connection.createArrayOf(typeName, elements);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't create array", e);
        }
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws ConnectorException {
        try {
            return connection.createStruct(typeName, attributes);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't create struct", e);
        }
    }

    @Override
    public void setSchema(String schema) throws ConnectorException {
        try {
            connection.setSchema(schema);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't setting schema", e);
        }

    }

    @Override
    public String getSchema() throws ConnectorException {
        try {
            return connection.getSchema();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting schema", e);
        }
    }

    @Override
    public void abort(Executor executor) throws ConnectorException {
        try {
            connection.abort(executor);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't abort", e);
        }
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws ConnectorException {
        try {
            connection.setNetworkTimeout(executor, milliseconds);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't setting network timeout", e);
        }

    }

    @Override
    public int getNetworkTimeout() throws ConnectorException {
        try {
            return connection.getNetworkTimeout();
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting network timeout", e);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws ConnectorException {
        try {
            return connection.unwrap(iface);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't unwrap", e);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws ConnectorException {
        try {
            return connection.isWrapperFor(iface);
        }
        catch (SQLException e) {
            throw new ConnectorException("can't getting is wrapper for", e);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
