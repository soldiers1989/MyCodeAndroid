package com.dm.db.converter;

import java.util.Date;

import android.database.Cursor;
import android.text.TextUtils;

import com.dm.db.sqlite.ColumnDbType;

public class DateColumnConverter implements ColumnConverter<Date> {
	@Override
	public Date getFieldValue(final Cursor cursor, int index) {
		return cursor.isNull(index) ? null : new Date(cursor.getLong(index));
	}

	@Override
	public Date getFieldValue(String fieldStringValue) {
		if (TextUtils.isEmpty(fieldStringValue))
			return null;
		return new Date(Long.valueOf(fieldStringValue));
	}

	@Override
	public Object fieldValue2ColumnValue(Date fieldValue) {
		if (fieldValue == null)
			return null;
		return fieldValue.getTime();
	}

	@Override
	public ColumnDbType getColumnDbType() {
		return ColumnDbType.INTEGER;
	}
}
