package com.dm.db.table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.database.Cursor;

import com.dm.db.converter.ColumnConverter;
import com.dm.db.converter.ColumnConverterFactory;
import com.dm.db.sqlite.ColumnDbType;
import com.dm.utils.DMLog;

@SuppressWarnings("rawtypes")
public class Column {

	private Table table;

	private int index = -1;

	protected final String columnName;
	private final Object defaultValue;

	protected final Method getMethod;
	protected final Method setMethod;

    protected final Field columnField;

    protected final ColumnConverter columnConverter;

	/* package */Column(Class<?> entityType, Field field) {
		this.columnField = field;
		this.columnConverter = ColumnConverterFactory.getColumnConverter(field
				.getType());
		this.columnName = ColumnUtils.getColumnNameByField(field);
		if (this.columnConverter != null) {
			this.defaultValue = this.columnConverter.getFieldValue(ColumnUtils
					.getColumnDefaultValue(field));
		} else {
			this.defaultValue = null;
		}
		this.getMethod = ColumnUtils.getColumnGetMethod(entityType, field);
		this.setMethod = ColumnUtils.getColumnSetMethod(entityType, field);
	}

	public void setValue2Entity(Object entity, Cursor cursor, int index) {
		this.index = index;
		Object value = columnConverter.getFieldValue(cursor, index);
		if (value == null && defaultValue == null)
			return;

		if (setMethod != null) {
			try {
				setMethod.invoke(entity, value == null ? defaultValue : value);
			} catch (Throwable e) {
				DMLog.e("setValue2Entity", e.getMessage());
			}
		} else {
			try {
				this.columnField.setAccessible(true);
				this.columnField.set(entity, value == null ? defaultValue
						: value);
			} catch (Throwable e) {
				DMLog.e("setValue2Entity", e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Object getColumnValue(Object entity) {
		Object fieldValue = getFieldValue(entity);
		return columnConverter.fieldValue2ColumnValue(fieldValue);
	}

	public Object getFieldValue(Object entity) {
		Object fieldValue = null;
		if (entity != null) {
			if (getMethod != null) {
				try {
					fieldValue = getMethod.invoke(entity);
				} catch (Throwable e) {
					DMLog.e("getFieldValue", e.getMessage());
				}
			} else {
				try {
					this.columnField.setAccessible(true);
					fieldValue = this.columnField.get(entity);
				} catch (Throwable e) {
					DMLog.e("getFieldValue", e.getMessage());
				}
			}
		}
		return fieldValue;
	}

	public Table getTable() {
		return table;
	}

	/* package */void setTable(Table table) {
		this.table = table;
	}

	/**
	 * The value set in setValue2Entity(...)
	 *
	 * @return -1 or the index of this column.
	 */
	public int getIndex() {
		return index;
	}

	public String getColumnName() {
		return columnName;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public Field getColumnField() {
		return columnField;
	}

	public ColumnConverter getColumnConverter() {
		return columnConverter;
	}

	public ColumnDbType getColumnDbType() {
		return columnConverter.getColumnDbType();
	}
}
