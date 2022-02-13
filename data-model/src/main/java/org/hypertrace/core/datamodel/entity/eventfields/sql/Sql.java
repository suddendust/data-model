package org.hypertrace.core.datamodel.entity.eventfields.sql;

public class Sql {
  private String query;
  private String db_type;
  private String url;
  private String params;
  private String sqlstate;

  public String getQuery() {
    return query;
  }

  public String getDbType() {
    return db_type;
  }

  public String getUrl() {
    return url;
  }

  public String getParams() {
    return params;
  }

  public String getSqlstate() {
    return sqlstate;
  }
}
