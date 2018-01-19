/*
 * Copyright (C) 2009-2012 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader.library;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.geometerplus.android.util.SQLiteUtil;
import org.geometerplus.android.util.UIUtil;
import org.geometerplus.fbreader.library.Author;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.fbreader.library.BookSelection;
import org.geometerplus.fbreader.library.Bookmark;
import org.geometerplus.fbreader.library.Booknote;
import org.geometerplus.fbreader.library.BooksDatabase;
import org.geometerplus.fbreader.library.FileInfo;
import org.geometerplus.fbreader.library.FileInfoSet;
import org.geometerplus.fbreader.library.SeriesInfo;
import org.geometerplus.fbreader.library.Tag;
import org.geometerplus.zlibrary.core.config.ZLConfig;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.options.ZLIntegerOption;
import org.geometerplus.zlibrary.core.options.ZLStringOption;
import org.geometerplus.zlibrary.text.view.ZLTextFixedPosition;
import org.geometerplus.zlibrary.text.view.ZLTextPosition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public final class SQLiteBooksDatabase extends BooksDatabase {
    private final String myInstanceId;
    private final SQLiteDatabase myDatabase;
    private final HashMap<Tag, Long> myIdByTag = new HashMap<Tag, Long>();
    private final HashMap<Long, Tag> myTagById = new HashMap<Long, Tag>();
    private boolean myTagCacheIsInitialized;
    private SQLiteStatement myUpdateBookInfoStatement;
    private SQLiteStatement myInsertBookInfoStatement;
    private SQLiteStatement myDeleteBookAuthorsStatement;
    private SQLiteStatement myGetAuthorIdStatement;
    private SQLiteStatement myInsertAuthorStatement;
    private SQLiteStatement myInsertBookAuthorStatement;
    private SQLiteStatement myGetTagIdStatement;
    private SQLiteStatement myCreateTagIdStatement;
    private SQLiteStatement myDeleteBookTagsStatement;
    private SQLiteStatement myInsertBookTagStatement;
    private SQLiteStatement myGetSeriesIdStatement;
    private SQLiteStatement myInsertSeriesStatement;
    private SQLiteStatement myInsertBookSeriesStatement;
    private SQLiteStatement myDeleteBookSeriesStatement;
    private SQLiteStatement myRemoveFileInfoStatement;
    private SQLiteStatement myInsertFileInfoStatement;
    private SQLiteStatement myUpdateFileInfoStatement;
    private SQLiteStatement mySaveRecentBookStatement;
    private SQLiteStatement myAddToFavoritesStatement;
    private SQLiteStatement myRemoveFromFavoritesStatement;
    private SQLiteStatement myInsertBookmarkStatement;
    private SQLiteStatement myUpdateBookmarkStatement;
    private SQLiteStatement myDeleteBookmarkStatement;
    private SQLiteStatement myDeleteBookSelectionStatement;
    private SQLiteStatement myStorePositionStatement;
    private SQLiteStatement myInsertIntoBookListStatement;
    private SQLiteStatement myDeleteFromBookListStatement;
    private SQLiteStatement myCheckBookListStatement;
    private SQLiteStatement myDeleteVisitedHyperlinksStatement;
    private SQLiteStatement myStoreVisitedHyperlinksStatement;
    private SQLiteStatement myInsertBooknoteStatement;
    private SQLiteStatement myUpdateBooknoteStatement;
    public SQLiteBooksDatabase(Context context, String instanceId) {
        myInstanceId = instanceId;
        myDatabase = context.openOrCreateDatabase("books.db",
                Context.MODE_PRIVATE, null);
        migrate(context);
    }

    protected void executeAsATransaction(Runnable actions) {
        boolean transactionStarted = false;
        try {
            myDatabase.beginTransaction();
            transactionStarted = true;
        } catch (Throwable t) {
        }
        try {
            actions.run();
            if (transactionStarted) {
                myDatabase.setTransactionSuccessful();
            }
        } finally {
            if (transactionStarted) {
                myDatabase.endTransaction();
            }
        }
    }

    private void migrate(Context context) {
        final int version = myDatabase.getVersion();
        final int currentVersion = 20;
        if (version >= currentVersion) {
            return;
        }
        UIUtil.wait((version == 0) ? "creatingBooksDatabase"
                : "updatingBooksDatabase", new Runnable() {
            public void run() {
                myDatabase.beginTransaction();

                switch (version) {
                    case 0:
                        createTables();
                    case 1:
                        updateTables1();
                    case 2:
                        updateTables2();
                    case 3:
                        updateTables3();
                    case 4:
                        updateTables4();
                    case 5:
                        updateTables5();
                    case 6:
                        updateTables6();
                    case 7:
                        updateTables7();
                    case 8:
                        updateTables8();
                    case 9:
                        updateTables9();
                    case 10:
                        updateTables10();
                    case 11:
                        updateTables11();
                    case 12:
                        updateTables12();
                    case 13:
                        updateTables13();
                    case 14:
                        updateTables14();
                    case 15:
                        updateTables15();
                    case 16:
                        updateTables16();
                    case 17:
                        updateTables17();
                    case 18:
                        updateTables18();
                    case 19:
                        updateTables19();
                }
                myDatabase.setTransactionSuccessful();
                myDatabase.endTransaction();

                myDatabase.execSQL("VACUUM");
                myDatabase.setVersion(currentVersion);
            }
        }, context);
    }

    @Override
    protected Book loadBook(long bookId) {
        Book book = null;
        final Cursor cursor = myDatabase.rawQuery(
                "SELECT file_id,title,encoding,language FROM Books WHERE book_id = "
                        + bookId, null);
        try {
            if (cursor.moveToNext()) {
                book = createBook(bookId, cursor.getLong(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
            }
        } finally {
            cursor.close();
        }
        return book;
    }

    @Override
    protected void reloadBook(Book book) {
        final Cursor cursor = myDatabase.rawQuery(
                "SELECT title,encoding,language FROM Books WHERE book_id = "
                        + book.getId(), null);
        try {
            if (cursor.moveToNext()) {
                book.setTitle(cursor.getString(0));
                book.setEncoding(cursor.getString(1));
                book.setLanguage(cursor.getString(2));
            }
        } finally {
            cursor.close();
        }
    }

    protected Book loadBookByFile(long fileId, ZLFile file) {
        if (fileId == -1) {
            return null;
        }
        Book book = null;
        final Cursor cursor = myDatabase.rawQuery(
                "SELECT book_id,title,encoding,language FROM Books WHERE file_id = "
                        + fileId, null);
        try {

            if (cursor.moveToNext()) {
                book = createBook(cursor.getLong(0), file, cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
            }
        } finally {
            cursor.close();
        }
        return book;
    }

    private void initTagCache() {
        if (myTagCacheIsInitialized) {
            return;
        }
        myTagCacheIsInitialized = true;

        Cursor cursor = myDatabase.rawQuery(
                "SELECT tag_id,parent_id,name FROM Tags ORDER BY tag_id", null);
        try {

            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                if (myTagById.get(id) == null) {
                    final Tag tag = Tag.getTag(myTagById.get(cursor.getLong(1)),
                            cursor.getString(2));
                    myIdByTag.put(tag, id);
                    myTagById.put(id, tag);
                }
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    protected Map<Long, Book> loadBooks(FileInfoSet infos, boolean existing) {
        Cursor cursor = myDatabase.rawQuery(
                "SELECT book_id,file_id,title,encoding,language FROM Books WHERE `exists` = "
                        + (existing ? 1 : 0), null);
        final HashMap<Long, Book> booksById = new HashMap<Long, Book>();
        final HashMap<Long, Book> booksByFileId = new HashMap<Long, Book>();
        try {
            while (cursor.moveToNext()) {
                final long id = cursor.getLong(0);
                final long fileId = cursor.getLong(1);
                final Book book = createBook(id, infos.getFile(fileId),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4));
                if (book != null) {
                    booksById.put(id, book);
                    booksByFileId.put(fileId, book);
                }
            }
        } finally {
            cursor.close();
        }

        initTagCache();

        cursor = myDatabase.rawQuery(
                "SELECT author_id,name,sort_key FROM Authors", null);
        final HashMap<Long, Author> authorById = new HashMap<Long, Author>();
        try {

            while (cursor.moveToNext()) {
                authorById.put(cursor.getLong(0),
                        new Author(cursor.getString(1), cursor.getString(2)));
            }
        } finally {
            cursor.close();
        }

        cursor = myDatabase
                .rawQuery(
                        "SELECT book_id,author_id FROM BookAuthor ORDER BY author_index",
                        null);
        try {

            while (cursor.moveToNext()) {
                Book book = booksById.get(cursor.getLong(0));
                if (book != null) {
                    Author author = authorById.get(cursor.getLong(1));
                    if (author != null) {
                        addAuthor(book, author);
                    }
                }
            }
        } finally {
            cursor.close();
        }

        cursor = myDatabase
                .rawQuery("SELECT book_id,tag_id FROM BookTag", null);
        try {
            while (cursor.moveToNext()) {
                Book book = booksById.get(cursor.getLong(0));
                if (book != null) {
                    addTag(book, getTagById(cursor.getLong(1)));
                }
            }
        } finally {
            cursor.close();
        }
        cursor = myDatabase.rawQuery("SELECT series_id,name FROM Series", null);
        final HashMap<Long, String> seriesById = new HashMap<Long, String>();
        try {
            while (cursor.moveToNext()) {
                seriesById.put(cursor.getLong(0), cursor.getString(1));
            }
        } finally {
            cursor.close();
        }

        cursor = myDatabase.rawQuery(
                "SELECT book_id,series_id,book_index FROM BookSeries", null);
        try {
            while (cursor.moveToNext()) {
                Book book = booksById.get(cursor.getLong(0));
                if (book != null) {
                    final String series = seriesById.get(cursor.getLong(1));
                    if (series != null) {
                        setSeriesInfo(book, series, cursor.getString(2));
                    }
                }
            }
        } finally {
            cursor.close();
        }
        return booksByFileId;
    }

    @Override
    protected void setExistingFlag(Collection<Book> books, boolean flag) {
        if (books.isEmpty()) {
            return;
        }
        final StringBuilder bookSet = new StringBuilder("(");
        boolean first = true;
        for (Book b : books) {
            if (first) {
                first = false;
            } else {
                bookSet.append(",");
            }
            bookSet.append(b.getId());
        }
        bookSet.append(")");
        myDatabase.execSQL("UPDATE Books SET `exists` = " + (flag ? 1 : 0)
                + " WHERE book_id IN " + bookSet);
    }

    @Override
    protected void updateBookInfo(long bookId, long fileId, String encoding,
                                  String language, String title) {
        if (myUpdateBookInfoStatement == null) {
            myUpdateBookInfoStatement = myDatabase
                    .compileStatement("UPDATE Books SET file_id = ?, encoding = ?, language = ?, title = ? WHERE book_id = ?");
        }
        myUpdateBookInfoStatement.bindLong(1, fileId);
        SQLiteUtil.bindString(myUpdateBookInfoStatement, 2, encoding);
        SQLiteUtil.bindString(myUpdateBookInfoStatement, 3, language);
        myUpdateBookInfoStatement.bindString(4, title);
        myUpdateBookInfoStatement.bindLong(5, bookId);
        myUpdateBookInfoStatement.execute();
    }

    @Override
    protected long insertBookInfo(ZLFile file, String encoding,
                                  String language, String title) {
        if (myInsertBookInfoStatement == null) {
            myInsertBookInfoStatement = myDatabase
                    .compileStatement("INSERT OR IGNORE INTO Books (encoding,language,title,file_id) VALUES (?,?,?,?)");
        }
        SQLiteUtil.bindString(myInsertBookInfoStatement, 1, encoding);
        SQLiteUtil.bindString(myInsertBookInfoStatement, 2, language);
        myInsertBookInfoStatement.bindString(3, title);
        final FileInfoSet infoSet = new FileInfoSet(file);
        myInsertBookInfoStatement.bindLong(4, infoSet.getId(file));
        return myInsertBookInfoStatement.executeInsert();
    }

    protected void deleteAllBookAuthors(long bookId) {
        if (myDeleteBookAuthorsStatement == null) {
            myDeleteBookAuthorsStatement = myDatabase
                    .compileStatement("DELETE FROM BookAuthor WHERE book_id = ?");
        }
        myDeleteBookAuthorsStatement.bindLong(1, bookId);
        myDeleteBookAuthorsStatement.execute();
    }

    protected void saveBookAuthorInfo(long bookId, long index, Author author) {
        if (myGetAuthorIdStatement == null) {
            myGetAuthorIdStatement = myDatabase
                    .compileStatement("SELECT author_id FROM Authors WHERE name = ? AND sort_key = ?");
            myInsertAuthorStatement = myDatabase
                    .compileStatement("INSERT OR IGNORE INTO Authors (name,sort_key) VALUES (?,?)");
            myInsertBookAuthorStatement = myDatabase
                    .compileStatement("INSERT OR REPLACE INTO BookAuthor (book_id,author_id,author_index) VALUES (?,?,?)");
        }

        long authorId;
        try {
            myGetAuthorIdStatement.bindString(1, author.DisplayName);
            myGetAuthorIdStatement.bindString(2, author.SortKey);
            authorId = myGetAuthorIdStatement.simpleQueryForLong();
        } catch (SQLException e) {
            myInsertAuthorStatement.bindString(1, author.DisplayName);
            myInsertAuthorStatement.bindString(2, author.SortKey);
            authorId = myInsertAuthorStatement.executeInsert();
        }
        myInsertBookAuthorStatement.bindLong(1, bookId);
        myInsertBookAuthorStatement.bindLong(2, authorId);
        myInsertBookAuthorStatement.bindLong(3, index);
        myInsertBookAuthorStatement.execute();
    }

    protected List<Author> loadAuthors(long bookId) {
        final Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT Authors.name,Authors.sort_key FROM BookAuthor INNER JOIN Authors ON Authors.author_id = BookAuthor.author_id WHERE BookAuthor.book_id = ?",
                        new String[]{"" + bookId});
        if (!cursor.moveToNext()) {
            cursor.close();
            return null;
        }
        final ArrayList<Author> list = new ArrayList<Author>();
        try {
            do {
                list.add(new Author(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return list;
    }

    private long getTagId(Tag tag) {
        if (myGetTagIdStatement == null) {
            myGetTagIdStatement = myDatabase
                    .compileStatement("SELECT tag_id FROM Tags WHERE parent_id = ? AND name = ?");
            myCreateTagIdStatement = myDatabase
                    .compileStatement("INSERT OR IGNORE INTO Tags (parent_id,name) VALUES (?,?)");
        }
        {
            final Long id = myIdByTag.get(tag);
            if (id != null) {
                return id;
            }
        }
        if (tag.Parent != null) {
            myGetTagIdStatement.bindLong(1, getTagId(tag.Parent));
        } else {
            myGetTagIdStatement.bindNull(1);
        }
        myGetTagIdStatement.bindString(2, tag.Name);
        long id;
        try {
            id = myGetTagIdStatement.simpleQueryForLong();
        } catch (SQLException e) {
            if (tag.Parent != null) {
                myCreateTagIdStatement.bindLong(1, getTagId(tag.Parent));
            } else {
                myCreateTagIdStatement.bindNull(1);
            }
            myCreateTagIdStatement.bindString(2, tag.Name);
            id = myCreateTagIdStatement.executeInsert();
        }
        myIdByTag.put(tag, id);
        myTagById.put(id, tag);
        return id;
    }

    protected void deleteAllBookTags(long bookId) {
        if (myDeleteBookTagsStatement == null) {
            myDeleteBookTagsStatement = myDatabase
                    .compileStatement("DELETE FROM BookTag WHERE book_id = ?");
        }
        myDeleteBookTagsStatement.bindLong(1, bookId);
        myDeleteBookTagsStatement.execute();
    }

    protected void saveBookTagInfo(long bookId, Tag tag) {
        if (myInsertBookTagStatement == null) {
            myInsertBookTagStatement = myDatabase
                    .compileStatement("INSERT OR IGNORE INTO BookTag (book_id,tag_id) VALUES (?,?)");
        }
        myInsertBookTagStatement.bindLong(1, bookId);
        myInsertBookTagStatement.bindLong(2, getTagId(tag));
        myInsertBookTagStatement.execute();
    }

    private Tag getTagById(long id) {
        Tag tag = myTagById.get(id);
        if (tag == null) {
            final Cursor cursor = myDatabase.rawQuery(
                    "SELECT parent_id,name FROM Tags WHERE tag_id = ?",
                    new String[]{"" + id});
            try {
                if (cursor.moveToNext()) {
                    final Tag parent = cursor.isNull(0) ? null
                            : getTagById(cursor.getLong(0));
                    tag = Tag.getTag(parent, cursor.getString(1));
                    myIdByTag.put(tag, id);
                    myTagById.put(id, tag);
                }
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                cursor.close();
            }
        }

        return tag;
    }

    protected List<Tag> loadTags(long bookId) {
        final Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT Tags.tag_id FROM BookTag INNER JOIN Tags ON Tags.tag_id = BookTag.tag_id WHERE BookTag.book_id = ?",
                        new String[]{"" + bookId});
        if (!cursor.moveToNext()) {
            cursor.close();
            return null;
        }
        ArrayList<Tag> list = new ArrayList<Tag>();
        try {
            do {
                list.add(getTagById(cursor.getLong(0)));
            } while (cursor.moveToNext());
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;
    }

    protected void saveBookSeriesInfo(long bookId, SeriesInfo seriesInfo) {
        if (myGetSeriesIdStatement == null) {
            myGetSeriesIdStatement = myDatabase
                    .compileStatement("SELECT series_id FROM Series WHERE name = ?");
            myInsertSeriesStatement = myDatabase
                    .compileStatement("INSERT OR IGNORE INTO Series (name) VALUES (?)");
            myInsertBookSeriesStatement = myDatabase
                    .compileStatement("INSERT OR REPLACE INTO BookSeries (book_id,series_id,book_index) VALUES (?,?,?)");
            myDeleteBookSeriesStatement = myDatabase
                    .compileStatement("DELETE FROM BookSeries WHERE book_id = ?");
        }

        if (seriesInfo == null) {
            myDeleteBookSeriesStatement.bindLong(1, bookId);
            myDeleteBookSeriesStatement.execute();
        } else {
            long seriesId;
            try {
                myGetSeriesIdStatement.bindString(1, seriesInfo.Name);
                seriesId = myGetSeriesIdStatement.simpleQueryForLong();
            } catch (SQLException e) {
                myInsertSeriesStatement.bindString(1, seriesInfo.Name);
                seriesId = myInsertSeriesStatement.executeInsert();
            }
            myInsertBookSeriesStatement.bindLong(1, bookId);
            myInsertBookSeriesStatement.bindLong(2, seriesId);
            SQLiteUtil.bindString(myInsertBookSeriesStatement, 3,
                    seriesInfo.Index != null ? seriesInfo.Index.toString()
                            : null);
            myInsertBookSeriesStatement.execute();
        }
    }

    protected SeriesInfo loadSeriesInfo(long bookId) {
        final Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT Series.name,BookSeries.book_index FROM BookSeries INNER JOIN Series ON Series.series_id = BookSeries.series_id WHERE BookSeries.book_id = ?",
                        new String[]{"" + bookId});
        SeriesInfo info = null;
        try {
            if (cursor.moveToNext()) {
                info = new SeriesInfo(cursor.getString(0),
                        SeriesInfo.createIndex(cursor.getString(1)));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return info;
    }

    protected void removeFileInfo(long fileId) {
        if (fileId == -1) {
            return;
        }
        if (myRemoveFileInfoStatement == null) {
            myRemoveFileInfoStatement = myDatabase
                    .compileStatement("DELETE FROM Files WHERE file_id = ?");
        }
        myRemoveFileInfoStatement.bindLong(1, fileId);
        myRemoveFileInfoStatement.execute();
    }

    protected void saveFileInfo(FileInfo fileInfo) {
        final long id = fileInfo.Id;
        SQLiteStatement statement;
        if (id == -1) {
            if (myInsertFileInfoStatement == null) {
                myInsertFileInfoStatement = myDatabase
                        .compileStatement("INSERT OR IGNORE INTO Files (name,parent_id,size) VALUES (?,?,?)");
            }
            statement = myInsertFileInfoStatement;
        } else {
            if (myUpdateFileInfoStatement == null) {
                myUpdateFileInfoStatement = myDatabase
                        .compileStatement("UPDATE Files SET name = ?, parent_id = ?, size = ? WHERE file_id = ?");
            }
            statement = myUpdateFileInfoStatement;
        }
        statement.bindString(1, fileInfo.Name);
        final FileInfo parent = fileInfo.Parent;
        if (parent != null) {
            statement.bindLong(2, parent.Id);
        } else {
            statement.bindNull(2);
        }
        final long size = fileInfo.FileSize;
        if (size != -1) {
            statement.bindLong(3, size);
        } else {
            statement.bindNull(3);
        }
        if (id == -1) {
            fileInfo.Id = statement.executeInsert();
        } else {
            statement.bindLong(4, id);
            statement.execute();
        }
    }

    protected Collection<FileInfo> loadFileInfos() {
        Cursor cursor = myDatabase.rawQuery(
                "SELECT file_id,name,parent_id,size FROM Files", null);
        HashMap<Long, FileInfo> infosById = new HashMap<Long, FileInfo>();
        try {
            while (cursor.moveToNext()) {
                final long id = cursor.getLong(0);
                final FileInfo info = createFileInfo(
                        id,
                        cursor.getString(1),
                        cursor.isNull(2) ? null : infosById.get(cursor
                                .getLong(2)));
                if (!cursor.isNull(3)) {
                    info.FileSize = cursor.getLong(3);
                }
                infosById.put(id, info);
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return infosById.values();
    }

    protected Collection<FileInfo> loadFileInfos(ZLFile file) {
        final LinkedList<ZLFile> fileStack = new LinkedList<ZLFile>();
        for (; file != null; file = file.getParent()) {
            fileStack.addFirst(file);
        }

        final ArrayList<FileInfo> infos = new ArrayList<FileInfo>(
                fileStack.size());
        final String[] parameters = {null};
        FileInfo current = null;
        for (ZLFile f : fileStack) {
            parameters[0] = f.getLongName();
            final Cursor cursor = myDatabase
                    .rawQuery(
                            (current == null) ? "SELECT file_id,size FROM Files WHERE name = ?"
                                    : "SELECT file_id,size FROM Files WHERE parent_id = "
                                    + current.Id + " AND name = ?",
                            parameters);
            if (cursor.moveToNext()) {
                try {
                    current = createFileInfo(cursor.getLong(0), parameters[0],
                            current);
                    if (!cursor.isNull(1)) {
                        current.FileSize = cursor.getLong(1);
                    }
                    infos.add(current);
                } catch (Exception e) {
                } finally {
                    cursor.close();
                }
            } else {
                try {

                } finally {
                    cursor.close();
                }
                break;
            }
        }

        return infos;
    }

    protected Collection<FileInfo> loadFileInfos(long fileId) {
        final ArrayList<FileInfo> infos = new ArrayList<FileInfo>();
        while (fileId != -1) {
            final Cursor cursor = myDatabase.rawQuery(
                    "SELECT name,size,parent_id FROM Files WHERE file_id = "
                            + fileId, null);
            try {
                if (cursor.moveToNext()) {
                    FileInfo info = createFileInfo(fileId, cursor.getString(0),
                            null);
                    if (!cursor.isNull(1)) {
                        info.FileSize = cursor.getLong(1);
                    }
                    infos.add(0, info);
                    fileId = cursor.isNull(2) ? -1 : cursor.getLong(2);
                } else {
                    fileId = -1;
                }
            } finally {
                cursor.close();
            }
        }
        for (int i = 1; i < infos.size(); ++i) {
            final FileInfo oldInfo = infos.get(i);
            final FileInfo newInfo = createFileInfo(oldInfo.Id, oldInfo.Name,
                    infos.get(i - 1));
            newInfo.FileSize = oldInfo.FileSize;
            infos.set(i, newInfo);
        }
        return infos;
    }

    protected void saveRecentBookIds(final List<Long> ids) {
        if (mySaveRecentBookStatement == null) {
            mySaveRecentBookStatement = myDatabase
                    .compileStatement("INSERT OR IGNORE INTO RecentBooks (book_id) VALUES (?)");
        }
        executeAsATransaction(new Runnable() {
            public void run() {
                myDatabase.delete("RecentBooks", null, null);
                for (long id : ids) {
                    mySaveRecentBookStatement.bindLong(1, id);
                    mySaveRecentBookStatement.execute();
                }
            }
        });
    }

    protected List<Long> loadRecentBookIds() {
        final Cursor cursor = myDatabase.rawQuery(
                "SELECT book_id FROM RecentBooks ORDER BY book_index", null);
        final LinkedList<Long> ids = new LinkedList<Long>();
        try {
            while (cursor.moveToNext()) {
                ids.add(cursor.getLong(0));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return ids;
    }

    protected void addToFavorites(long bookId) {
        if (myAddToFavoritesStatement == null) {
            myAddToFavoritesStatement = myDatabase
                    .compileStatement("INSERT OR IGNORE INTO Favorites(book_id) VALUES (?)");
        }
        myAddToFavoritesStatement.bindLong(1, bookId);
        myAddToFavoritesStatement.execute();
    }

    protected void removeFromFavorites(long bookId) {
        if (myRemoveFromFavoritesStatement == null) {
            myRemoveFromFavoritesStatement = myDatabase
                    .compileStatement("DELETE FROM Favorites WHERE book_id = ?");
        }
        myRemoveFromFavoritesStatement.bindLong(1, bookId);
        myRemoveFromFavoritesStatement.execute();
    }

    protected List<Long> loadFavoritesIds() {
        final Cursor cursor = myDatabase.rawQuery(
                "SELECT book_id FROM Favorites", null);
        final LinkedList<Long> ids = new LinkedList<Long>();
        try {
            while (cursor.moveToNext()) {
                ids.add(cursor.getLong(0));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return ids;
    }

    @Override
    protected List<Bookmark> loadBookmarks(long bookId, boolean visible) {
        LinkedList<Bookmark> list = new LinkedList<Bookmark>();
        Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT Bookmarks.bookmark_id,Bookmarks.book_id,Books.title,Bookmarks.bookmark_text,Bookmarks.creation_time,Bookmarks.modification_time,Bookmarks.access_time,Bookmarks.access_counter,Bookmarks.model_id,Bookmarks.paragraph,Bookmarks.word,Bookmarks.char FROM Bookmarks INNER JOIN Books ON Books.book_id = Bookmarks.book_id WHERE Bookmarks.book_id = ? AND Bookmarks.visible = ?",
                        new String[]{"" + bookId, visible ? "1" : "0"});
        try {
            while (cursor.moveToNext()) {
                list.add(createBookmark(cursor.getLong(0), cursor.getLong(1),
                        cursor.getString(2), cursor.getString(3),
                        SQLiteUtil.getDate(cursor, 4),
                        SQLiteUtil.getDate(cursor, 5),
                        SQLiteUtil.getDate(cursor, 6), (int) cursor.getLong(7),
                        cursor.getString(8), (int) cursor.getLong(9),
                        (int) cursor.getLong(10), (int) cursor.getLong(11),
                        visible));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return list;
    }

    @Override
    protected Bookmark getBookmark(long bookId, int paragraphIndex,
                                   boolean visible) {
        Bookmark bookmark = null;
        Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT Bookmarks.bookmark_id,Bookmarks.book_id,Books.title,Bookmarks.bookmark_text,Bookmarks.creation_time,Bookmarks.modification_time,Bookmarks.access_time,Bookmarks.access_counter,Bookmarks.model_id,Bookmarks.paragraph,Bookmarks.word,Bookmarks.char FROM Bookmarks INNER JOIN Books ON Books.book_id = Bookmarks.book_id WHERE Bookmarks.book_id = ? AND Bookmarks.paragraph = ? AND visible = ?",
                        new String[]{"" + bookId, "" + paragraphIndex,
                                visible ? "1" : "0"});
        try {

            if (cursor.moveToFirst()) {
                bookmark = createBookmark(cursor.getLong(0), cursor.getLong(1),
                        cursor.getString(2), cursor.getString(3),
                        SQLiteUtil.getDate(cursor, 4),
                        SQLiteUtil.getDate(cursor, 5),
                        SQLiteUtil.getDate(cursor, 6), (int) cursor.getLong(7),
                        cursor.getString(8), (int) cursor.getLong(9),
                        (int) cursor.getLong(10), (int) cursor.getLong(11),
                        visible);
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return bookmark;
    }

    /*
     * (non-Javadoc) paragraphIndex 段落 elementIndex 行起始位置 endIndex 行结束位置
     */
    @Override
    protected ArrayList<BookSelection> getBookSelection(long bookId,
                                                        int paragraphIndex, int elementIndex, int endIndex) {
        ArrayList<BookSelection> list = new ArrayList<BookSelection>();
        BookSelection bookSelection = null;

        Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT BookSelections.bookselect_id,BookSelections.book_id,BookSelections.bookselect_text,BookSelections.creation_time,BookSelections.modification_time,BookSelections.paragraph,BookSelections.word,BookSelections.char,BookSelections.end_paragraph_index,BookSelections.end_element_index,BookSelections.endIndex,BookSelections.bookselect_color FROM BookSelections INNER JOIN Books ON Books.book_id = BookSelections.book_id WHERE BookSelections.book_id = ? AND ((BookSelections.paragraph = ? AND (((BookSelections.word >= ? AND BookSelections.word <= ?) OR (BookSelections.endIndex >= ? AND BookSelections.endIndex <= ?)) OR ((BookSelections.word <= ? AND BookSelections.endIndex >= ?) OR (BookSelections.word <= ? AND BookSelections.endIndex >= ?)))) OR (BookSelections.end_paragraph_index = ? AND ((BookSelections.end_element_index >= ? OR BookSelections.end_element_index >= ?) AND BookSelections.end_paragraph_index > BookSelections.paragraph) OR (BookSelections.paragraph < ? AND BookSelections.end_paragraph_index > ? ))) ",
                        new String[]{"" + bookId, "" + paragraphIndex,
                                "" + elementIndex, "" + endIndex,
                                "" + elementIndex, "" + endIndex,
                                "" + elementIndex, "" + elementIndex,
                                "" + endIndex, "" + endIndex,
                                "" + paragraphIndex, "" + elementIndex,
                                "" + endIndex, "" + paragraphIndex,
                                "" + paragraphIndex});
        try {
            while (cursor.moveToNext()) {
                bookSelection = createBookSelection(cursor.getLong(0),
                        cursor.getLong(1), cursor.getString(2),
                        SQLiteUtil.getDate(cursor, 3),
                        SQLiteUtil.getDate(cursor, 4), (int) cursor.getLong(5),
                        (int) cursor.getLong(6), (int) cursor.getLong(7),
                        (int) cursor.getLong(8), (int) cursor.getLong(9),
                        (int) cursor.getLong(10), cursor.getString(11));
                list.add(bookSelection);
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return list;
    }

    @Override
    protected String getBookSelectionColor(long bookId) {
        Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT bookselect_color from BookSelections where book_id = ?",
                        new String[]{"" + bookId});
        String color = null;
        if (cursor.moveToLast()) {
            color = cursor.getString(0);
        }
        return color;
    }

    @Override
    protected List<Integer> getBookmarkParagraphIndexes(long bookId) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT paragraph FROM Bookmarks WHERE book_id = ? AND visible = ?",
                        new String[]{String.valueOf(bookId), "1"});
        try {
            while (cursor.moveToNext()) {
                list.add(cursor.getInt(0));
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    @Override
    protected List<Bookmark> loadAllVisibleBookmarks() {
        LinkedList<Bookmark> list = new LinkedList<Bookmark>();
        myDatabase.execSQL("DELETE FROM Bookmarks WHERE book_id = -1");
        Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT Bookmarks.bookmark_id,Bookmarks.book_id,Books.title,Bookmarks.bookmark_text,Bookmarks.creation_time,Bookmarks.modification_time,Bookmarks.access_time,Bookmarks.access_counter,Bookmarks.model_id,Bookmarks.paragraph,Bookmarks.word,Bookmarks.char FROM Bookmarks INNER JOIN Books ON Books.book_id = Bookmarks.book_id WHERE Bookmarks.visible = 1",
                        null);
        try {
            while (cursor.moveToNext()) {
                list.add(createBookmark(cursor.getLong(0), cursor.getLong(1),
                        cursor.getString(2), cursor.getString(3),
                        SQLiteUtil.getDate(cursor, 4),
                        SQLiteUtil.getDate(cursor, 5),
                        SQLiteUtil.getDate(cursor, 6), (int) cursor.getLong(7),
                        cursor.getString(8), (int) cursor.getLong(9),
                        (int) cursor.getLong(10), (int) cursor.getLong(11), true));
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    @Override
    protected long saveBookmark(Bookmark bookmark) {
        SQLiteStatement statement;
        if (bookmark.getId() == -1) {
            if (myInsertBookmarkStatement == null) {
                myInsertBookmarkStatement = myDatabase
                        .compileStatement("INSERT OR IGNORE INTO Bookmarks (book_id,bookmark_text,creation_time,modification_time,access_time,access_counter,model_id,paragraph,word,char,visible) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            }
            statement = myInsertBookmarkStatement;
        } else {
            if (myUpdateBookmarkStatement == null) {
                myUpdateBookmarkStatement = myDatabase
                        .compileStatement("UPDATE Bookmarks SET book_id = ?, bookmark_text = ?, creation_time =?, modification_time = ?,access_time = ?, access_counter = ?, model_id = ?, paragraph = ?, word = ?, char = ?, visible = ? WHERE bookmark_id = ?");
            }
            statement = myUpdateBookmarkStatement;
        }

        statement.bindLong(1, bookmark.getBookId());
        statement.bindString(2, bookmark.getText());
        SQLiteUtil.bindDate(statement, 3, bookmark.getTime(Bookmark.CREATION));
        SQLiteUtil.bindDate(statement, 4,
                bookmark.getTime(Bookmark.MODIFICATION));
        SQLiteUtil.bindDate(statement, 5, bookmark.getTime(Bookmark.ACCESS));
        statement.bindLong(6, bookmark.getAccessCount());
        SQLiteUtil.bindString(statement, 7, bookmark.ModelId);
        statement.bindLong(8, bookmark.ParagraphIndex);
        statement.bindLong(9, bookmark.ElementIndex);
        statement.bindLong(10, bookmark.CharIndex);
        statement.bindLong(11, bookmark.IsVisible ? 1 : 0);

        if (statement == myInsertBookmarkStatement) {
            return statement.executeInsert();
        } else {
            final long id = bookmark.getId();
            statement.bindLong(12, id);
            statement.execute();
            return id;
        }
    }

    /*
     * 保存选择文字
     */
    @Override
    protected long saveSelection(BookSelection bookSelection) {
        SQLiteStatement statement = myDatabase
                .compileStatement("INSERT OR IGNORE INTO BookSelections (book_id,bookselect_text,creation_time,modification_time,paragraph,word,char,end_paragraph_index,end_element_index,endIndex,bookselect_color) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
        statement.bindLong(1, bookSelection.getMyBookId());
        statement.bindString(2, bookSelection.getMyText());
        SQLiteUtil.bindDate(statement, 3, bookSelection.getMyCreationDate());
        SQLiteUtil
                .bindDate(statement, 4, bookSelection.getMyModificationDate());
        statement.bindLong(5, bookSelection.ParagraphIndex);
        statement.bindLong(6, bookSelection.ElementIndex);
        statement.bindLong(7, bookSelection.CharIndex);
        statement.bindLong(8, bookSelection.getMyEndParagraphIndex());
        statement.bindLong(9, bookSelection.getMyEndElementIndex());
        statement.bindLong(10, bookSelection.getMyIndex());
        statement.bindString(11, bookSelection.getColor());
        return statement.executeInsert();
    }

    @Override
    protected void updateSelection(BookSelection bookSelection) {
        SQLiteStatement statement = myDatabase
                .compileStatement("UPDATE BookSelections SET bookselect_color = ? WHERE book_id = ? AND paragraph = ? AND end_paragraph_index = ?");
        statement.bindString(1, bookSelection.getColor());
        statement.bindLong(2, bookSelection.getMyBookId());
        statement.bindLong(3, bookSelection.ParagraphIndex);
        statement.bindLong(4, bookSelection.getMyEndParagraphIndex());
        statement.execute();

    }

    @Override
    protected void deleteBookmark(Bookmark bookmark) {
        if (myDeleteBookmarkStatement == null) {
            myDeleteBookmarkStatement = myDatabase
                    .compileStatement("DELETE FROM Bookmarks WHERE bookmark_id = ?");
        }
        myDeleteBookmarkStatement.bindLong(1, bookmark.getId());
        myDeleteBookmarkStatement.execute();
    }

    @Override
    protected void deleteSelection(BookSelection bookSelection) {
        if (myDeleteBookSelectionStatement == null) {
            myDeleteBookSelectionStatement = myDatabase
                    .compileStatement("DELETE FROM BookSelections WHERE bookselect_id = ?");
        }
        myDeleteBookSelectionStatement.bindLong(1, bookSelection.getMyId());
        myDeleteBookSelectionStatement.execute();
    }

    protected ZLTextPosition getStoredPosition(long bookId) {
        ZLTextPosition position = null;
        Cursor cursor = myDatabase.rawQuery(
                "SELECT paragraph,word,char FROM BookState WHERE book_id = "
                        + bookId, null);
        try {
            if (cursor.moveToNext()) {
                position = new ZLTextFixedPosition((int) cursor.getLong(0),
                        (int) cursor.getLong(1), (int) cursor.getLong(2));
            }
        } finally {
            cursor.close();
        }
        return position;
    }

    protected void storePosition(long bookId, ZLTextPosition position) {
        if (myStorePositionStatement == null) {
            myStorePositionStatement = myDatabase
                    .compileStatement("INSERT OR REPLACE INTO BookState (book_id,paragraph,word,char) VALUES (?,?,?,?)");
        }
        myStorePositionStatement.bindLong(1, bookId);
        myStorePositionStatement.bindLong(2, position.getParagraphIndex());
        myStorePositionStatement.bindLong(3, position.getElementIndex());
        myStorePositionStatement.bindLong(4, position.getCharIndex());
        myStorePositionStatement.execute();
    }

    protected boolean insertIntoBookList(long bookId) {
        if (myInsertIntoBookListStatement == null) {
            myInsertIntoBookListStatement = myDatabase
                    .compileStatement("INSERT OR IGNORE INTO BookList(book_id) VALUES (?)");
        }
        myInsertIntoBookListStatement.bindLong(1, bookId);
        myInsertIntoBookListStatement.execute();
        return true;
    }

    protected boolean deleteFromBookList(long bookId) {
        if (myDeleteFromBookListStatement == null) {
            myDeleteFromBookListStatement = myDatabase
                    .compileStatement("DELETE FROM BookList WHERE book_id = ?");
        }
        myDeleteFromBookListStatement.bindLong(1, bookId);
        myDeleteFromBookListStatement.execute();
        deleteVisitedHyperlinks(bookId);
        return true;
    }

    protected boolean checkBookList(long bookId) {
        if (myCheckBookListStatement == null) {
            myCheckBookListStatement = myDatabase
                    .compileStatement("SELECT COUNT(*) FROM BookList WHERE book_id = ?");
        }
        myCheckBookListStatement.bindLong(1, bookId);
        return myCheckBookListStatement.simpleQueryForLong() > 0;
    }

    private void deleteVisitedHyperlinks(long bookId) {
        if (myDeleteVisitedHyperlinksStatement == null) {
            myDeleteVisitedHyperlinksStatement = myDatabase
                    .compileStatement("DELETE FROM VisitedHyperlinks WHERE book_id = ?");
        }

        myDeleteVisitedHyperlinksStatement.bindLong(1, bookId);
        myDeleteVisitedHyperlinksStatement.execute();
    }

    protected void addVisitedHyperlink(long bookId, String hyperlinkId) {
        if (myStoreVisitedHyperlinksStatement == null) {
            myStoreVisitedHyperlinksStatement = myDatabase
                    .compileStatement("INSERT OR IGNORE INTO VisitedHyperlinks(book_id,hyperlink_id) VALUES (?,?)");
        }

        myStoreVisitedHyperlinksStatement.bindLong(1, bookId);
        myStoreVisitedHyperlinksStatement.bindString(2, hyperlinkId);
        myStoreVisitedHyperlinksStatement.execute();
    }

    protected Collection<String> loadVisitedHyperlinks(long bookId) {
        final TreeSet<String> links = new TreeSet<String>();
        final Cursor cursor = myDatabase.rawQuery(
                "SELECT hyperlink_id FROM VisitedHyperlinks WHERE book_id = ?",
                new String[]{"" + bookId});
        try {
            while (cursor.moveToNext()) {
                links.add(cursor.getString(0));
            }
        } finally {
            cursor.close();
        }
        return links;
    }

    private void createTables() {
        myDatabase.execSQL("CREATE TABLE Books("
                + "book_id INTEGER PRIMARY KEY," + "encoding TEXT,"
                + "language TEXT," + "title TEXT NOT NULL,"
                + "file_name TEXT UNIQUE NOT NULL)");
        myDatabase.execSQL("CREATE TABLE Authors("
                + "author_id INTEGER PRIMARY KEY," + "name TEXT NOT NULL,"
                + "sort_key TEXT NOT NULL,"
                + "CONSTRAINT Authors_Unique UNIQUE (name, sort_key))");
        myDatabase
                .execSQL("CREATE TABLE BookAuthor("
                        + "author_id INTEGER NOT NULL REFERENCES Authors(author_id),"
                        + "book_id INTEGER NOT NULL REFERENCES Books(book_id),"
                        + "author_index INTEGER NOT NULL,"
                        + "CONSTRAINT BookAuthor_Unique0 UNIQUE (author_id, book_id),"
                        + "CONSTRAINT BookAuthor_Unique1 UNIQUE (book_id, author_index))");
        myDatabase.execSQL("CREATE TABLE Series("
                + "series_id INTEGER PRIMARY KEY,"
                + "name TEXT UNIQUE NOT NULL)");
        myDatabase.execSQL("CREATE TABLE BookSeries("
                + "series_id INTEGER NOT NULL REFERENCES Series(series_id),"
                + "book_id INTEGER NOT NULL UNIQUE REFERENCES Books(book_id),"
                + "book_index INTEGER)");
        myDatabase.execSQL("CREATE TABLE Tags(" + "tag_id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "parent INTEGER REFERENCES Tags(tag_id),"
                + "CONSTRAINT Tags_Unique UNIQUE (name, parent))");
        myDatabase.execSQL("CREATE TABLE BookTag("
                + "tag_id INTEGER REFERENCES Tags(tag_id),"
                + "book_id INTEGER REFERENCES Books(book_id),"
                + "CONSTRAINT BookTag_Unique UNIQUE (tag_id, book_id))");

    }

    private void updateTables1() {
        myDatabase.execSQL("ALTER TABLE Tags RENAME TO Tags_Obsolete");
        myDatabase.execSQL("CREATE TABLE Tags(" + "tag_id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "parent_id INTEGER REFERENCES Tags(tag_id),"
                + "CONSTRAINT Tags_Unique UNIQUE (name, parent_id))");
        myDatabase
                .execSQL("INSERT INTO Tags (tag_id,name,parent_id) SELECT tag_id,name,parent FROM Tags_Obsolete");
        myDatabase.execSQL("DROP TABLE Tags_Obsolete");

        myDatabase.execSQL("ALTER TABLE BookTag RENAME TO BookTag_Obsolete");
        myDatabase.execSQL("CREATE TABLE BookTag("
                + "tag_id INTEGER NOT NULL REFERENCES Tags(tag_id),"
                + "book_id INTEGER NOT NULL REFERENCES Books(book_id),"
                + "CONSTRAINT BookTag_Unique UNIQUE (tag_id, book_id))");
        myDatabase
                .execSQL("INSERT INTO BookTag (tag_id,book_id) SELECT tag_id,book_id FROM BookTag_Obsolete");
        myDatabase.execSQL("DROP TABLE BookTag_Obsolete");
    }

    private void updateTables2() {
        myDatabase
                .execSQL("CREATE INDEX BookAuthor_BookIndex ON BookAuthor (book_id)");
        myDatabase
                .execSQL("CREATE INDEX BookTag_BookIndex ON BookTag (book_id)");
        myDatabase
                .execSQL("CREATE INDEX BookSeries_BookIndex ON BookSeries (book_id)");
    }

    private void updateTables3() {
        myDatabase.execSQL("CREATE TABLE Files("
                + "file_id INTEGER PRIMARY KEY," + "name TEXT NOT NULL,"
                + "parent_id INTEGER REFERENCES Files(file_id),"
                + "size INTEGER,"
                + "CONSTRAINT Files_Unique UNIQUE (name, parent_id))");
    }

    private void updateTables4() {
        final FileInfoSet fileInfos = new FileInfoSet();
        final Cursor cursor = myDatabase.rawQuery(
                "SELECT file_name FROM Books", null);
        try {
            while (cursor.moveToNext()) {
                fileInfos.check(ZLFile.createFileByPath(cursor.getString(0))
                        .getPhysicalFile(), false);
            }
        } finally {
            cursor.close();
        }
        fileInfos.save();

        myDatabase.execSQL("CREATE TABLE RecentBooks("
                + "book_index INTEGER PRIMARY KEY,"
                + "book_id INTEGER REFERENCES Books(book_id))");
        final ArrayList<Long> ids = new ArrayList<Long>();

        final SQLiteStatement statement = myDatabase
                .compileStatement("SELECT book_id FROM Books WHERE file_name = ?");

        for (int i = 0; i < 20; ++i) {
            final ZLStringOption option = new ZLStringOption("LastOpenedBooks",
                    "Book" + i, "");
            final String fileName = option.getValue();
            option.setValue("");
            try {
                statement.bindString(1, fileName);
                final long bookId = statement.simpleQueryForLong();
                if (bookId != -1) {
                    ids.add(bookId);
                }
            } catch (SQLException e) {
            }
        }
        saveRecentBookIds(ids);
    }

    private void updateTables5() {
        myDatabase.execSQL("CREATE TABLE Bookmarks("
                + "bookmark_id INTEGER PRIMARY KEY,"
                + "book_id INTEGER NOT NULL REFERENCES Books(book_id),"
                + "bookmark_text TEXT NOT NULL,"
                + "creation_time INTEGER NOT NULL,"
                + "modification_time INTEGER," + "access_time INTEGER,"
                + "access_counter INTEGER NOT NULL,"
                + "paragraph INTEGER NOT NULL," + "word INTEGER NOT NULL,"
                + "char INTEGER NOT NULL)");
        myDatabase.execSQL("CREATE TABLE BookSelections("
                + "bookselect_id INTEGER PRIMARY KEY,"
                + "book_id INTEGER  REFERENCES Books(book_id),"
                + "bookselect_text TEXT NOT NULL,"
                + "creation_time INTEGER NOT NULL,"
                + "modification_time INTEGER," + "paragraph INTEGER NOT NULL,"
                + "word INTEGER NOT NULL," + "char INTEGER NOT NULL,"
                + "end_paragraph_index INTEGER NOT NULL,"
                + "end_element_index INTEGER NOT NULL,"
                + "endIndex INTEGER NOT NULL,"
                + "bookselect_color TEXT NOT NULL)");

        myDatabase.execSQL("CREATE TABLE BookState("
                + "book_id INTEGER UNIQUE NOT NULL REFERENCES Books(book_id),"
                + "paragraph INTEGER NOT NULL," + "word INTEGER NOT NULL,"
                + "char INTEGER NOT NULL)");
        Cursor cursor = myDatabase.rawQuery(
                "SELECT book_id,file_name FROM Books", null);
        final SQLiteStatement statement = myDatabase
                .compileStatement("INSERT INTO BookState (book_id,paragraph,word,char) VALUES (?,?,?,?)");
        try {
            while (cursor.moveToNext()) {
                final long bookId = cursor.getLong(0);
                final String fileName = cursor.getString(1);
                final int position = new ZLIntegerOption(fileName,
                        "PositionInBuffer", 0).getValue();
                final int paragraph = new ZLIntegerOption(fileName, "Paragraph_"
                        + position, 0).getValue();
                final int word = new ZLIntegerOption(fileName, "Word_" + position,
                        0).getValue();
                final int chr = new ZLIntegerOption(fileName, "Char_" + position, 0)
                        .getValue();
                if ((paragraph != 0) || (word != 0) || (chr != 0)) {
                    statement.bindLong(1, bookId);
                    statement.bindLong(2, paragraph);
                    statement.bindLong(3, word);
                    statement.bindLong(4, chr);
                    statement.execute();
                }
                ZLConfig.Instance().removeGroup(fileName);
            }
        } finally {
            cursor.close();
        }
    }

    private void updateTables6() {
        myDatabase.execSQL("ALTER TABLE Bookmarks ADD COLUMN model_id TEXT");

        myDatabase.execSQL("ALTER TABLE Books ADD COLUMN file_id INTEGER");

        myDatabase.execSQL("DELETE FROM Files");
        final FileInfoSet infoSet = new FileInfoSet();
        Cursor cursor = myDatabase
                .rawQuery("SELECT file_name FROM Books", null);
        try {
            while (cursor.moveToNext()) {
                infoSet.check(ZLFile.createFileByPath(cursor.getString(0))
                        .getPhysicalFile(), false);
            }
        } finally {
            cursor.close();
        }
        infoSet.save();

        cursor = myDatabase.rawQuery("SELECT book_id,file_name FROM Books",
                null);
        final SQLiteStatement deleteStatement = myDatabase
                .compileStatement("DELETE FROM Books WHERE book_id = ?");
        final SQLiteStatement updateStatement = myDatabase
                .compileStatement("UPDATE Books SET file_id = ? WHERE book_id = ?");
        try {
            while (cursor.moveToNext()) {
                final long bookId = cursor.getLong(0);
                final long fileId = infoSet.getId(ZLFile.createFileByPath(cursor
                        .getString(1)));

                if (fileId == -1) {
                    deleteStatement.bindLong(1, bookId);
                    deleteStatement.execute();
                } else {
                    updateStatement.bindLong(1, fileId);
                    updateStatement.bindLong(2, bookId);
                    updateStatement.execute();
                }
            }
        } finally {
            cursor.close();
        }

        myDatabase.execSQL("ALTER TABLE Books RENAME TO Books_Obsolete");
        myDatabase.execSQL("CREATE TABLE Books("
                + "book_id INTEGER PRIMARY KEY," + "encoding TEXT,"
                + "language TEXT," + "title TEXT NOT NULL,"
                + "file_id INTEGER UNIQUE NOT NULL REFERENCES Files(file_id))");
        myDatabase
                .execSQL("INSERT INTO Books (book_id,encoding,language,title,file_id) SELECT book_id,encoding,language,title,file_id FROM Books_Obsolete");
        myDatabase.execSQL("DROP TABLE Books_Obsolete");
    }

    private void updateTables7() {
        final ArrayList<Long> seriesIDs = new ArrayList<Long>();
        Cursor cursor = myDatabase.rawQuery(
                "SELECT series_id,name FROM Series", null);
        try {
            while (cursor.moveToNext()) {
                if (cursor.getString(1).length() > 200) {
                    seriesIDs.add(cursor.getLong(0));
                }
            }
        } finally {
            cursor.close();
        }
        if (seriesIDs.isEmpty()) {
            return;
        }

        final ArrayList<Long> bookIDs = new ArrayList<Long>();
        for (Long id : seriesIDs) {
            cursor = myDatabase.rawQuery(
                    "SELECT book_id FROM BookSeries WHERE series_id=" + id,
                    null);
            try {
                while (cursor.moveToNext()) {
                    bookIDs.add(cursor.getLong(0));
                }
            } finally {
                cursor.close();
            }
            myDatabase.execSQL("DELETE FROM BookSeries WHERE series_id=" + id);
            myDatabase.execSQL("DELETE FROM Series WHERE series_id=" + id);
        }

        for (Long id : bookIDs) {
            myDatabase.execSQL("DELETE FROM Books WHERE book_id=" + id);
            myDatabase.execSQL("DELETE FROM BookAuthor WHERE book_id=" + id);
            myDatabase.execSQL("DELETE FROM BookTag WHERE book_id=" + id);
        }
    }

    private void updateTables8() {
        myDatabase
                .execSQL("CREATE TABLE IF NOT EXISTS BookList ( "
                        + "book_id INTEGER UNIQUE NOT NULL REFERENCES Books (book_id))");
    }

    private void updateTables9() {
        myDatabase
                .execSQL("CREATE INDEX BookList_BookIndex ON BookList (book_id)");
    }

    private void updateTables10() {
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Favorites("
                + "book_id INTEGER UNIQUE NOT NULL REFERENCES Books(book_id))");
    }

    private void updateTables11() {
        myDatabase.execSQL("UPDATE Files SET size = size + 1");
    }

    private void updateTables12() {
        myDatabase
                .execSQL("DELETE FROM Files WHERE parent_id IN (SELECT file_id FROM Files WHERE name LIKE '%.epub')");
    }

    private void updateTables13() {
        myDatabase
                .execSQL("ALTER TABLE Bookmarks ADD COLUMN visible INTEGER DEFAULT 1");
    }

    private void updateTables14() {
        myDatabase
                .execSQL("ALTER TABLE BookSeries RENAME TO BookSeries_Obsolete");
        myDatabase.execSQL("CREATE TABLE BookSeries("
                + "series_id INTEGER NOT NULL REFERENCES Series(series_id),"
                + "book_id INTEGER NOT NULL UNIQUE REFERENCES Books(book_id),"
                + "book_index REAL)");
        myDatabase
                .execSQL("INSERT INTO BookSeries (series_id,book_id,book_index) SELECT series_id,book_id,book_index FROM BookSeries_Obsolete");
        myDatabase.execSQL("DROP TABLE BookSeries_Obsolete");
    }

    private void updateTables15() {
        myDatabase
                .execSQL("CREATE TABLE IF NOT EXISTS VisitedHyperlinks("
                        + "book_id INTEGER NOT NULL REFERENCES Books(book_id),"
                        + "hyperlink_id TEXT NOT NULL,"
                        + "CONSTRAINT VisitedHyperlinks_Unique UNIQUE (book_id, hyperlink_id))");
    }

    private void updateTables16() {
        myDatabase
                .execSQL("ALTER TABLE Books ADD COLUMN `exists` INTEGER DEFAULT 1");
    }

    private void updateTables17() {
        myDatabase
                .execSQL("CREATE TABLE IF NOT EXISTS BookStatus("
                        + "book_id INTEGER NOT NULL REFERENCES Books(book_id) PRIMARY KEY,"
                        + "access_time INTEGER NOT NULL,"
                        + "pages_full INTEGER NOT NULL,"
                        + "page_current INTEGER NOT NULL)");
    }

    private void updateTables18() {
        myDatabase
                .execSQL("ALTER TABLE BookSeries RENAME TO BookSeries_Obsolete");
        myDatabase.execSQL("CREATE TABLE BookSeries("
                + "series_id INTEGER NOT NULL REFERENCES Series(series_id),"
                + "book_id INTEGER NOT NULL UNIQUE REFERENCES Books(book_id),"
                + "book_index TEXT)");
        final SQLiteStatement insert = myDatabase
                .compileStatement("INSERT INTO BookSeries (series_id,book_id,book_index) VALUES (?,?,?)");
        final Cursor cursor = myDatabase.rawQuery(
                "SELECT series_id,book_id,book_index FROM BookSeries_Obsolete",
                null);
        try {
            while (cursor.moveToNext()) {
                insert.bindLong(1, cursor.getLong(0));
                insert.bindLong(2, cursor.getLong(1));
                final float index = cursor.getFloat(2);
                final String stringIndex;
                if (index == 0.0f) {
                    stringIndex = null;
                } else {
                    if (Math.abs(index - Math.round(index)) < 0.01) {
                        stringIndex = String.valueOf(Math.round(index));
                    } else {
                        stringIndex = String.format("%.1f", index);
                    }
                }
                final BigDecimal bdIndex = SeriesInfo.createIndex(stringIndex);
                SQLiteUtil.bindString(insert, 3,
                        bdIndex != null ? bdIndex.toString() : null);
                insert.executeInsert();
            }
        } finally {
            cursor.close();
        }
        myDatabase.execSQL("DROP TABLE BookSeries_Obsolete");
    }

    /**
     * 增加笔记相关的数据库表
     */
    private void updateTables19() {

        myDatabase.execSQL("CREATE TABLE Booknotes("
                + "bookmark_id INTEGER PRIMARY KEY,"
                + "book_id INTEGER NOT NULL REFERENCES Books(book_id),"
                + "bookmark_text TEXT NOT NULL,"
                + "creation_time INTEGER NOT NULL,"
                + "modification_time INTEGER," + "access_time INTEGER,"
                + "access_counter INTEGER NOT NULL,"
                + "paragraph INTEGER NOT NULL," + "word INTEGER NOT NULL,"
                + "char INTEGER NOT NULL)");
        myDatabase.execSQL("ALTER TABLE Booknotes ADD COLUMN model_id TEXT");
        myDatabase.execSQL("ALTER TABLE Booknotes ADD COLUMN visible INTEGER DEFAULT 1");
    }

    @Override
    protected List<Booknote> loadBooknotes(long bookId, boolean visible) {
        LinkedList<Booknote> list = new LinkedList<Booknote>();
        Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT Bookmarks.bookmark_id,Bookmarks.book_id,Books.title,Bookmarks.bookmark_text,Bookmarks.creation_time,Bookmarks.modification_time,Bookmarks.access_time,Bookmarks.access_counter,Bookmarks.model_id,Bookmarks.paragraph,Bookmarks.word,Bookmarks.char FROM Booknotes as Bookmarks INNER JOIN Books ON Books.book_id = Bookmarks.book_id WHERE Bookmarks.book_id = ? AND Bookmarks.visible = ?",
                        new String[]{"" + bookId, visible ? "1" : "0"});
        try {
            while (cursor.moveToNext()) {
                list.add(createBooknote(cursor.getLong(0), cursor.getLong(1),
                        cursor.getString(2), cursor.getString(3),
                        SQLiteUtil.getDate(cursor, 4),
                        SQLiteUtil.getDate(cursor, 5),
                        SQLiteUtil.getDate(cursor, 6), (int) cursor.getLong(7),
                        cursor.getString(8), (int) cursor.getLong(9),
                        (int) cursor.getLong(10), (int) cursor.getLong(11),
                        visible));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return list;
    }

    @Override
    protected List<Booknote> loadAllVisibleBooknotes() {
        LinkedList<Booknote> list = new LinkedList<Booknote>();
        myDatabase.execSQL("DELETE FROM Booknotes WHERE book_id = -1");
        Cursor cursor = myDatabase
                .rawQuery(
                        "SELECT Bookmarks.bookmark_id,Bookmarks.book_id,Books.title,Bookmarks.bookmark_text,Bookmarks.creation_time,Bookmarks.modification_time,Bookmarks.access_time,Bookmarks.access_counter,Bookmarks.model_id,Bookmarks.paragraph,Bookmarks.word,Bookmarks.char FROM Booknotes as Bookmarks INNER JOIN Books ON Books.book_id = Bookmarks.book_id ",
                        null);
        try {
            while (cursor.moveToNext()) {
                list.add(createBooknote(cursor.getLong(0), cursor.getLong(1),
                        cursor.getString(2), cursor.getString(3),
                        SQLiteUtil.getDate(cursor, 4),
                        SQLiteUtil.getDate(cursor, 5),
                        SQLiteUtil.getDate(cursor, 6), (int) cursor.getLong(7),
                        cursor.getString(8), (int) cursor.getLong(9),
                        (int) cursor.getLong(10), (int) cursor.getLong(11), true));
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    @Override
    protected long saveBooknote(Booknote bookmark) {
        SQLiteStatement statement;
        if (bookmark.getId() == -1) {
            if (myInsertBooknoteStatement == null) {
                myInsertBooknoteStatement = myDatabase
                        .compileStatement("INSERT   INTO Booknotes (book_id,bookmark_text,creation_time,modification_time,access_time,access_counter,model_id,paragraph,word,char,visible) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            }
            statement = myInsertBooknoteStatement;
        } else {
            if (myUpdateBooknoteStatement == null) {
                myUpdateBooknoteStatement = myDatabase
                        .compileStatement("UPDATE Booknotes SET book_id = ?, bookmark_text = ?, creation_time =?, modification_time = ?,access_time = ?, access_counter = ?, model_id = ?, paragraph = ?, word = ?, char = ?, visible = ? WHERE bookmark_id = ?");
            }
            statement = myUpdateBooknoteStatement;
        }

        statement.bindLong(1, bookmark.getBookId());
        statement.bindString(2, bookmark.getText());
        SQLiteUtil.bindDate(statement, 3, bookmark.getTime(Bookmark.CREATION));
        SQLiteUtil.bindDate(statement, 4,
                bookmark.getTime(Bookmark.MODIFICATION));
        SQLiteUtil.bindDate(statement, 5, bookmark.getTime(Bookmark.ACCESS));
        statement.bindLong(6, bookmark.getAccessCount());
        SQLiteUtil.bindString(statement, 7, bookmark.ModelId);
        statement.bindLong(8, bookmark.ParagraphIndex);
        statement.bindLong(9, bookmark.ElementIndex);
        statement.bindLong(10, bookmark.CharIndex);
        statement.bindLong(11, bookmark.IsVisible ? 1 : 0);

        if (statement == myInsertBooknoteStatement) {
            long result = statement.executeInsert();

            List<Booknote> list = loadAllVisibleBooknotes();
            Log.d("zhaoq", "executeInsert result size is " + list.size());
            return result;
        } else {
            final long id = bookmark.getId();
            statement.bindLong(12, id);
            statement.execute();
            return id;
        }
    }

    @Override
    protected void deleteBooknote(Booknote bookmark) {
        SQLiteStatement myDeleteBookmarkStatement = myDatabase
                .compileStatement("DELETE FROM Booknotes WHERE bookmark_id = ?");
        myDeleteBookmarkStatement.bindLong(1, bookmark.getId());
        myDeleteBookmarkStatement.execute();
        List<Booknote> list = loadAllVisibleBooknotes();
        Log.d("zhaoq", "delete result size is " + list.size());
    }
}
