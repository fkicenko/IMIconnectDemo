package com.imimobile.connectdemo.datas.roomdb;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MessageDao_Impl implements MessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MessageEntity> __insertionAdapterOfMessageEntity;

  private final EntityDeletionOrUpdateAdapter<MessageEntity> __deletionAdapterOfMessageEntity;

  private final EntityDeletionOrUpdateAdapter<MessageEntity> __updateAdapterOfMessageEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteNotifications;

  private final SharedSQLiteStatement __preparedStmtOfDeleteNotification;

  public MessageDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessageEntity = new EntityInsertionAdapter<MessageEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `messages_tbl` (`id`,`message_id`,`message_text`,`created_time`,`channel`,`userId`,`read_status`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MessageEntity value) {
        stmt.bindLong(1, value.id);
        if (value.messageId == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.messageId);
        }
        if (value.messageText == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.messageText);
        }
        final Long _tmp = DateConverter.toTimestamp(value.createdTime);
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.channel == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.channel);
        }
        if (value.userId == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.userId);
        }
        stmt.bindLong(7, value.messageReadStatus);
      }
    };
    this.__deletionAdapterOfMessageEntity = new EntityDeletionOrUpdateAdapter<MessageEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `messages_tbl` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MessageEntity value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfMessageEntity = new EntityDeletionOrUpdateAdapter<MessageEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `messages_tbl` SET `id` = ?,`message_id` = ?,`message_text` = ?,`created_time` = ?,`channel` = ?,`userId` = ?,`read_status` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MessageEntity value) {
        stmt.bindLong(1, value.id);
        if (value.messageId == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.messageId);
        }
        if (value.messageText == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.messageText);
        }
        final Long _tmp = DateConverter.toTimestamp(value.createdTime);
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.channel == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.channel);
        }
        if (value.userId == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.userId);
        }
        stmt.bindLong(7, value.messageReadStatus);
        stmt.bindLong(8, value.id);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM messages_tbl";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteNotifications = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM messages_tbl WHERE userId IN(?) AND channel IN(?)";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteNotification = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM messages_tbl WHERE userId IN(?) AND channel IN(?) AND message_id IN(?)";
        return _query;
      }
    };
  }

  @Override
  public void insert(final MessageEntity message) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMessageEntity.insert(message);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final MessageEntity message) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMessageEntity.handle(message);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateMessage(final MessageEntity message) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfMessageEntity.handle(message);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public void deleteNotifications(final String userId, final String channel) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteNotifications.acquire();
    int _argIndex = 1;
    if (userId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    if (channel == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, channel);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteNotifications.release(_stmt);
    }
  }

  @Override
  public void deleteNotification(final String userId, final String channel,
      final String messageId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteNotification.acquire();
    int _argIndex = 1;
    if (userId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    if (channel == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, channel);
    }
    _argIndex = 3;
    if (messageId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, messageId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteNotification.release(_stmt);
    }
  }

  @Override
  public List<MessageEntity> getMessages(final String userId, final String channel) {
    final String _sql = "SELECT * from messages_tbl WHERE userId IN(?) AND channel IN(?) ORDER BY created_time DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    if (channel == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, channel);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
      final int _cursorIndexOfMessageText = CursorUtil.getColumnIndexOrThrow(_cursor, "message_text");
      final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "created_time");
      final int _cursorIndexOfChannel = CursorUtil.getColumnIndexOrThrow(_cursor, "channel");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMessageReadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "read_status");
      final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MessageEntity _item;
        _item = new MessageEntity();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfMessageId)) {
          _item.messageId = null;
        } else {
          _item.messageId = _cursor.getString(_cursorIndexOfMessageId);
        }
        if (_cursor.isNull(_cursorIndexOfMessageText)) {
          _item.messageText = null;
        } else {
          _item.messageText = _cursor.getString(_cursorIndexOfMessageText);
        }
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfCreatedTime)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfCreatedTime);
        }
        _item.createdTime = DateConverter.toDate(_tmp);
        if (_cursor.isNull(_cursorIndexOfChannel)) {
          _item.channel = null;
        } else {
          _item.channel = _cursor.getString(_cursorIndexOfChannel);
        }
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _item.userId = null;
        } else {
          _item.userId = _cursor.getString(_cursorIndexOfUserId);
        }
        _item.messageReadStatus = _cursor.getInt(_cursorIndexOfMessageReadStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<MessageEntity> getMessagesByIds(final String... msgIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * from messages_tbl WHERE message_id IN(");
    final int _inputSize = msgIds.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : msgIds) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
      final int _cursorIndexOfMessageText = CursorUtil.getColumnIndexOrThrow(_cursor, "message_text");
      final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "created_time");
      final int _cursorIndexOfChannel = CursorUtil.getColumnIndexOrThrow(_cursor, "channel");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMessageReadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "read_status");
      final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MessageEntity _item_1;
        _item_1 = new MessageEntity();
        _item_1.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfMessageId)) {
          _item_1.messageId = null;
        } else {
          _item_1.messageId = _cursor.getString(_cursorIndexOfMessageId);
        }
        if (_cursor.isNull(_cursorIndexOfMessageText)) {
          _item_1.messageText = null;
        } else {
          _item_1.messageText = _cursor.getString(_cursorIndexOfMessageText);
        }
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfCreatedTime)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfCreatedTime);
        }
        _item_1.createdTime = DateConverter.toDate(_tmp);
        if (_cursor.isNull(_cursorIndexOfChannel)) {
          _item_1.channel = null;
        } else {
          _item_1.channel = _cursor.getString(_cursorIndexOfChannel);
        }
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _item_1.userId = null;
        } else {
          _item_1.userId = _cursor.getString(_cursorIndexOfUserId);
        }
        _item_1.messageReadStatus = _cursor.getInt(_cursorIndexOfMessageReadStatus);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
