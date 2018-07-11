package com.dm.db.sqlite;

import java.util.List;

import com.dm.db.exception.DbException;
import com.dm.db.table.ColumnUtils;
import com.dm.db.table.Foreign;
import com.dm.db.table.Table;

public class ForeignLazyLoader<T> {
	private final Foreign foreignColumn;
	private Object columnValue;

	public ForeignLazyLoader(Foreign foreignColumn, Object value) {
		this.foreignColumn = foreignColumn;
		this.columnValue = ColumnUtils.convert2DbColumnValueIfNeeded(value);
	}

	public List<T> getAllFromDb() throws DbException {
		List<T> entities = null;
		Table table = foreignColumn.getTable();
		if (table != null) {
			entities = table.db.findAll(Selector.from(
					foreignColumn.getForeignEntityType()).where(
					foreignColumn.getForeignColumnName(), "=", columnValue));
		}
		return entities;
	}

	public T getFirstFromDb() throws DbException {
		T entity = null;
		Table table = foreignColumn.getTable();
		if (table != null) {
			entity = table.db.findFirst(Selector.from(
					foreignColumn.getForeignEntityType()).where(
					foreignColumn.getForeignColumnName(), "=", columnValue));
		}
		return entity;
	}

	public void setColumnValue(Object value) {
		this.columnValue = ColumnUtils.convert2DbColumnValueIfNeeded(value);
	}

	public Object getColumnValue() {
		return columnValue;
	}
}
