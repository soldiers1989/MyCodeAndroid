package com.dm.db.table;

import java.lang.reflect.Field;
import java.util.List;

import android.database.Cursor;

import com.dm.db.converter.ColumnConverter;
import com.dm.db.converter.ColumnConverterFactory;
import com.dm.db.exception.DbException;
import com.dm.db.sqlite.ColumnDbType;
import com.dm.db.sqlite.ForeignLazyLoader;
import com.dm.utils.DMLog;

@SuppressWarnings("rawtypes")
public class Foreign extends Column {

	private final String foreignColumnName;
    
    private final ColumnConverter foreignColumnConverter;

	/* package */Foreign(Class<?> entityType, Field field) {
		super(entityType, field);

		foreignColumnName = ColumnUtils.getForeignColumnNameByField(field);
		Class<?> foreignColumnType = TableUtils.getColumnOrId(
				getForeignEntityType(), foreignColumnName).columnField
				.getType();
		foreignColumnConverter = ColumnConverterFactory
				.getColumnConverter(foreignColumnType);
	}

	public String getForeignColumnName() {
		return foreignColumnName;
	}

	public Class<?> getForeignEntityType() {
		return ColumnUtils.getForeignEntityType(this);
	}

	@Override
	public void setValue2Entity(Object entity, Cursor cursor, int index) {
		Object fieldValue = foreignColumnConverter.getFieldValue(cursor, index);
		if (fieldValue == null)
			return;

		Object value = null;
		Class<?> columnType = columnField.getType();
		if (columnType.equals(ForeignLazyLoader.class)) {
			value = new ForeignLazyLoader(this, fieldValue);
		} else if (columnType.equals(List.class)) {
			try {
				value = new ForeignLazyLoader(this, fieldValue).getAllFromDb();
			} catch (DbException e) {
				DMLog.e("Throwable", e.getMessage());
			}
		} else {
			try {
				value = new ForeignLazyLoader(this, fieldValue)
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
		Object fieldValue = getFieldValue(entity);
		Object columnValue = null;

		if (fieldValue != null) {
			Class<?> columnType = columnField.getType();
			if (columnType.equals(ForeignLazyLoader.class)) {
				columnValue = ((ForeignLazyLoader) fieldValue).getColumnValue();
			} else if (columnType.equals(List.class)) {
				try {
					List<?> foreignEntities = (List<?>) fieldValue;
					if (foreignEntities.size() > 0) {

						Class<?> foreignEntityType = ColumnUtils
								.getForeignEntityType(this);
						Column column = TableUtils.getColumnOrId(
								foreignEntityType, foreignColumnName);
						columnValue = column.getColumnValue(foreignEntities
								.get(0));

						// 仅自动关联外键
						Table table = this.getTable();
						if (table != null && column instanceof Id) {
							for (Object foreignObj : foreignEntities) {
								Object idValue = column
										.getColumnValue(foreignObj);
								if (idValue == null) {
									table.db.saveOrUpdate(foreignObj);
								}
							}
						}

						columnValue = column.getColumnValue(foreignEntities
								.get(0));
					}
				} catch (Throwable e) {
					DMLog.e("Throwable", e.getMessage());
				}
			} else {
				try {
					Column column = TableUtils.getColumnOrId(columnType,
							foreignColumnName);
					columnValue = column.getColumnValue(fieldValue);

					Table table = this.getTable();
					if (table != null && columnValue == null
							&& column instanceof Id) {
						table.db.saveOrUpdate(fieldValue);
					}

					columnValue = column.getColumnValue(fieldValue);
				} catch (Throwable e) {
					DMLog.e("Throwable", e.getMessage());
				}
			}
		}

		return columnValue;
	}

	@Override
	public ColumnDbType getColumnDbType() {
		return foreignColumnConverter.getColumnDbType();
	}

	/**
	 * It always return null.
	 *
	 * @return null
	 */
	@Override
	public Object getDefaultValue() {
		return null;
	}
}
