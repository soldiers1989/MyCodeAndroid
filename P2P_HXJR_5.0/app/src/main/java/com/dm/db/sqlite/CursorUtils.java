package com.dm.db.sqlite;

import java.util.concurrent.ConcurrentHashMap;

import android.database.Cursor;

import com.dm.db.DbUtils;
import com.dm.db.table.Column;
import com.dm.db.table.DbModel;
import com.dm.db.table.Finder;
import com.dm.db.table.Id;
import com.dm.db.table.Table;
import com.dm.utils.DMLog;

public class CursorUtils {

	public static <T> T getEntity(final DbUtils db, final Cursor cursor,
			Class<T> entityType, long findCacheSequence) {
		if (db == null || cursor == null)
			return null;

		EntityTempCache.setSeq(findCacheSequence);
		try {
			Table table = Table.get(db, entityType);
			Id id = table.id;
			String idColumnName = id.getColumnName();
			int idIndex = id.getIndex();
			if (idIndex < 0) {
				idIndex = cursor.getColumnIndex(idColumnName);
			}
			Object idValue = id.getColumnConverter().getFieldValue(cursor,
					idIndex);
			T entity = EntityTempCache.get(entityType, idValue);
			if (entity == null) {
				entity = entityType.newInstance();
				id.setValue2Entity(entity, cursor, idIndex);
				EntityTempCache.put(entityType, idValue, entity);
			} else {
				return entity;
			}
			int columnCount = cursor.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				String columnName = cursor.getColumnName(i);
				Column column = table.columnMap.get(columnName);
				if (column != null) {
					column.setValue2Entity(entity, cursor, i);
				}
			}

			// init finder
			for (Finder finder : table.finderMap.values()) {
				finder.setValue2Entity(entity, null, 0);
			}
			return entity;
		} catch (Throwable e) {
			DMLog.e("getEntity erro", e.getMessage());
		}

		return null;
	}

	public static DbModel getDbModel(final Cursor cursor) {
		DbModel result = null;
		if (cursor != null) {
			result = new DbModel();
			int columnCount = cursor.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				result.add(cursor.getColumnName(i), cursor.getString(i));
			}
		}
		return result;
	}

	public static class FindCacheSequence {
		private FindCacheSequence() {
		}

		private static long seq = 0;
		private static final String FOREIGN_LAZY_LOADER_CLASS_NAME = ForeignLazyLoader.class
				.getName();
		private static final String FINDER_LAZY_LOADER_CLASS_NAME = FinderLazyLoader.class
				.getName();

		public static long getSeq() {
			String findMethodCaller = Thread.currentThread().getStackTrace()[4]
					.getClassName();
			if (!findMethodCaller.equals(FOREIGN_LAZY_LOADER_CLASS_NAME)
					&& !findMethodCaller.equals(FINDER_LAZY_LOADER_CLASS_NAME)) {
				++seq;
			}
			return seq;
		}
	}

	private static class EntityTempCache {
		private EntityTempCache() {
		}

		private static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

		private static long seq = 0;

		public static <T> void put(Class<T> entityType, Object idValue,
				Object entity) {
			cache.put(entityType.getName() + "#" + idValue, entity);
		}

		@SuppressWarnings("unchecked")
		public static <T> T get(Class<T> entityType, Object idValue) {
			return (T) cache.get(entityType.getName() + "#" + idValue);
		}

		public static void setSeq(long seq) {
			if (EntityTempCache.seq != seq) {
				cache.clear();
				EntityTempCache.seq = seq;
			}
		}
	}
}
