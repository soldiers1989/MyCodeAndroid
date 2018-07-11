package com.dm.db.table;

import java.lang.reflect.Field;
import java.util.List;

import android.database.Cursor;

import com.dm.db.exception.DbException;
import com.dm.db.sqlite.ColumnDbType;
import com.dm.db.sqlite.FinderLazyLoader;
import com.dm.utils.DMLog;

public class Finder extends Column {

	private final String valueColumnName;
	private final String targetColumnName;

	/* package */Finder(Class<?> entityType, Field field) {
		super(entityType, field);

		com.dm.db.annotation.Finder finder = field
				.getAnnotation(com.dm.db.annotation.Finder.class);
		this.valueColumnName = finder.valueColumn();
		this.targetColumnName = finder.targetColumn();
	}

	public Class<?> getTargetEntityType() {
		return ColumnUtils.getFinderTargetEntityType(this);
	}

	public String getTargetColumnName() {
		return targetColumnName;
	}

    @SuppressWarnings("rawtypes")
    @Override
	public void setValue2Entity(Object entity, Cursor cursor, int index) {
		Object value = null;
		Class<?> columnType = columnField.getType();
		Object finderValue = TableUtils.getColumnOrId(entity.getClass(),
				this.valueColumnName).getColumnValue(entity);
		if (columnType.equals(FinderLazyLoader.class)) {
			value = new FinderLazyLoader(this, finderValue);
		} else if (columnType.equals(List.class)) {
			try {
				value = new FinderLazyLoader(this, finderValue).getAllFromDb();
			} catch (DbException e) {
				DMLog.e("Throwable", e.getMessage());
			}
		} else {
			try {
				value = new FinderLazyLoader(this, finderValue)
						.getFirstFromDb();
			} catch (DbException e) {
				DMLog.e("Throwable", e.getMessage());
			}
		}

		if (setMethod != null) {
			try {
				setMethod.invoke(entity, value);
			} catch (Throwable e) {
				DMLog.e("Throwable", e.getMessage());
			}
		} else {
			try {
				this.columnField.setAccessible(true);
				this.columnField.set(entity, value);
			} catch (Throwable e) {
				DMLog.e("Throwable", e.getMessage());
			}
		}
	}

	@Override
	public Object getColumnValue(Object entity) {
		return null;
	}

	@Override
	public Object getDefaultValue() {
		return null;
	}

	@Override
	public ColumnDbType getColumnDbType() {
		return ColumnDbType.TEXT;
	}
}
