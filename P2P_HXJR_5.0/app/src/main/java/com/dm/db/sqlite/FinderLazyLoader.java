package com.dm.db.sqlite;

import java.util.List;

import com.dm.db.exception.DbException;
import com.dm.db.table.ColumnUtils;
import com.dm.db.table.Finder;
import com.dm.db.table.Table;

public class FinderLazyLoader<T> {
	private final Finder finderColumn;
	private final Object finderValue;

	public FinderLazyLoader(Finder finderColumn, Object value) {
		this.finderColumn = finderColumn;
		this.finderValue = ColumnUtils.convert2DbColumnValueIfNeeded(value);
	}

	public List<T> getAllFromDb() throws DbException {
		List<T> entities = null;
		Table table = finderColumn.getTable();
		if (table != null) {
			entities = table.db.findAll(Selector.from(
					finderColumn.getTargetEntityType()).where(
					finderColumn.getTargetColumnName(), "=", finderValue));
		}
		return entities;
	}

	public T getFirstFromDb() throws DbException {
		T entity = null;
		Table table = finderColumn.getTable();
		if (table != null) {
			entity = table.db.findFirst(Selector.from(
					finderColumn.getTargetEntityType()).where(
					finderColumn.getTargetColumnName(), "=", finderValue));
		}
		return entity;
	}
}
