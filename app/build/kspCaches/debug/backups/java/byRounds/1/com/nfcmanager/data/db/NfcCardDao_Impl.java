package com.nfcmanager.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nfcmanager.data.model.NfcCardEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NfcCardDao_Impl implements NfcCardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NfcCardEntity> __insertionAdapterOfNfcCardEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCard;

  private final SharedSQLiteStatement __preparedStmtOfClearAllSelections;

  private final SharedSQLiteStatement __preparedStmtOfSetSelectedCard;

  public NfcCardDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNfcCardEntity = new EntityInsertionAdapter<NfcCardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `nfc_cards` (`id`,`uid`,`name`,`cardType`,`techListJson`,`sectorDataJson`,`pagesJson`,`createdAt`,`isSelected`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NfcCardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUid());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getCardType());
        statement.bindString(5, entity.getTechListJson());
        statement.bindString(6, entity.getSectorDataJson());
        statement.bindString(7, entity.getPagesJson());
        statement.bindLong(8, entity.getCreatedAt());
        final int _tmp = entity.isSelected() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__preparedStmtOfDeleteCard = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM nfc_cards WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllSelections = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE nfc_cards SET isSelected = 0";
        return _query;
      }
    };
    this.__preparedStmtOfSetSelectedCard = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE nfc_cards SET isSelected = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCard(final NfcCardEntity card, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfNfcCardEntity.insertAndReturnId(card);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCard(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCard.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCard.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllSelections(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllSelections.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllSelections.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setSelectedCard(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetSelectedCard.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetSelectedCard.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<NfcCardEntity>> getAllCards() {
    final String _sql = "SELECT * FROM nfc_cards ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"nfc_cards"}, new Callable<List<NfcCardEntity>>() {
      @Override
      @NonNull
      public List<NfcCardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCardType = CursorUtil.getColumnIndexOrThrow(_cursor, "cardType");
          final int _cursorIndexOfTechListJson = CursorUtil.getColumnIndexOrThrow(_cursor, "techListJson");
          final int _cursorIndexOfSectorDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sectorDataJson");
          final int _cursorIndexOfPagesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "pagesJson");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "isSelected");
          final List<NfcCardEntity> _result = new ArrayList<NfcCardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NfcCardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUid;
            _tmpUid = _cursor.getString(_cursorIndexOfUid);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCardType;
            _tmpCardType = _cursor.getString(_cursorIndexOfCardType);
            final String _tmpTechListJson;
            _tmpTechListJson = _cursor.getString(_cursorIndexOfTechListJson);
            final String _tmpSectorDataJson;
            _tmpSectorDataJson = _cursor.getString(_cursorIndexOfSectorDataJson);
            final String _tmpPagesJson;
            _tmpPagesJson = _cursor.getString(_cursorIndexOfPagesJson);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSelected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSelected);
            _tmpIsSelected = _tmp != 0;
            _item = new NfcCardEntity(_tmpId,_tmpUid,_tmpName,_tmpCardType,_tmpTechListJson,_tmpSectorDataJson,_tmpPagesJson,_tmpCreatedAt,_tmpIsSelected);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCardById(final long id, final Continuation<? super NfcCardEntity> $completion) {
    final String _sql = "SELECT * FROM nfc_cards WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<NfcCardEntity>() {
      @Override
      @Nullable
      public NfcCardEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCardType = CursorUtil.getColumnIndexOrThrow(_cursor, "cardType");
          final int _cursorIndexOfTechListJson = CursorUtil.getColumnIndexOrThrow(_cursor, "techListJson");
          final int _cursorIndexOfSectorDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sectorDataJson");
          final int _cursorIndexOfPagesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "pagesJson");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "isSelected");
          final NfcCardEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUid;
            _tmpUid = _cursor.getString(_cursorIndexOfUid);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCardType;
            _tmpCardType = _cursor.getString(_cursorIndexOfCardType);
            final String _tmpTechListJson;
            _tmpTechListJson = _cursor.getString(_cursorIndexOfTechListJson);
            final String _tmpSectorDataJson;
            _tmpSectorDataJson = _cursor.getString(_cursorIndexOfSectorDataJson);
            final String _tmpPagesJson;
            _tmpPagesJson = _cursor.getString(_cursorIndexOfPagesJson);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSelected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSelected);
            _tmpIsSelected = _tmp != 0;
            _result = new NfcCardEntity(_tmpId,_tmpUid,_tmpName,_tmpCardType,_tmpTechListJson,_tmpSectorDataJson,_tmpPagesJson,_tmpCreatedAt,_tmpIsSelected);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSelectedCard(final Continuation<? super NfcCardEntity> $completion) {
    final String _sql = "SELECT * FROM nfc_cards WHERE isSelected = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<NfcCardEntity>() {
      @Override
      @Nullable
      public NfcCardEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCardType = CursorUtil.getColumnIndexOrThrow(_cursor, "cardType");
          final int _cursorIndexOfTechListJson = CursorUtil.getColumnIndexOrThrow(_cursor, "techListJson");
          final int _cursorIndexOfSectorDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sectorDataJson");
          final int _cursorIndexOfPagesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "pagesJson");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "isSelected");
          final NfcCardEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUid;
            _tmpUid = _cursor.getString(_cursorIndexOfUid);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCardType;
            _tmpCardType = _cursor.getString(_cursorIndexOfCardType);
            final String _tmpTechListJson;
            _tmpTechListJson = _cursor.getString(_cursorIndexOfTechListJson);
            final String _tmpSectorDataJson;
            _tmpSectorDataJson = _cursor.getString(_cursorIndexOfSectorDataJson);
            final String _tmpPagesJson;
            _tmpPagesJson = _cursor.getString(_cursorIndexOfPagesJson);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSelected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSelected);
            _tmpIsSelected = _tmp != 0;
            _result = new NfcCardEntity(_tmpId,_tmpUid,_tmpName,_tmpCardType,_tmpTechListJson,_tmpSectorDataJson,_tmpPagesJson,_tmpCreatedAt,_tmpIsSelected);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
