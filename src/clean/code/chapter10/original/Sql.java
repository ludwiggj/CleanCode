package clean.code.chapter10.original;

import clean.code.added.to.make.code.build.Column;
import clean.code.added.to.make.code.build.Criteria;

public abstract class Sql {
  //   public Sql(String table, Column[] columns);
  public abstract String create();

  public abstract String insert(Object[] fields);

  public abstract String selectAll();

  public abstract String findByKey(String keyColumn, String keyValue);

  public abstract String select(Column column, String pattern);

  public abstract String select(Criteria criteria);

  public abstract String preparedInsert();

  private String columnList(Column[] columns) {
    return "";
  }

  private String valuesList(Object[] fields, final Column[] columns) {
    return "";
  }

  private String selectWithCriteria(String criteria) {
    return "";
  }

  private String placeholderList(Column[] columns) {
    return "";
  }
}