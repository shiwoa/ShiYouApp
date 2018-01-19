package org.geometerplus.fbreader.library;

import org.geometerplus.zlibrary.text.view.ZLTextFixedPosition;
import org.geometerplus.zlibrary.text.view.ZLTextPosition;

import java.util.Date;

public class BookSelection extends ZLTextFixedPosition {
    private final long myBookId;
    private final Date myCreationDate;
    private long myId;
    private String myText;
    private int endIndex;
    private int myEndParagraphIndex;
    private int myEndElementIndex;
    private String color;
    private Date myModificationDate;

    BookSelection(long id, long bookId, String text, Date creationDate,
                  Date modificationDate, int paragraphIndex,
                  int elementIndex, int charIndex, int endParagraphIndex, int endElementIndex, int end, String lineOption) {
        super(paragraphIndex, elementIndex, charIndex);
        myId = id;
        myBookId = bookId;
        myText = text;
        myCreationDate = creationDate;
        myModificationDate = modificationDate;
        endIndex = end;
        myEndParagraphIndex = endParagraphIndex;
        myEndElementIndex = endElementIndex;
        color = lineOption;
    }

    public BookSelection(Book book, String modelId, ZLTextPosition position, int endParagraphIndex, int endElementIndex, String text, String lineOption) {
        super(position);

        myId = -1;
        myBookId = book.getId();
        myText = text;
        myCreationDate = new Date();
        String replaceAll = text.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
        endIndex = position.getElementIndex() + replaceAll.length();
        myEndParagraphIndex = endParagraphIndex;
        myEndElementIndex = endElementIndex;
        color = lineOption;
    }

    public int getMyEndParagraphIndex() {
        return myEndParagraphIndex;
    }

    public void setMyEndParagraphIndex(int myEndParagraphIndex) {
        this.myEndParagraphIndex = myEndParagraphIndex;
    }

    public int getMyEndElementIndex() {
        return myEndElementIndex;
    }

    public void setMyEndElementIndex(int myEndElementIndex) {
        this.myEndElementIndex = myEndElementIndex;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getMyIndex() {
        return endIndex;
    }

    public void setMyIndex(int myIndex) {
        this.endIndex = myIndex;
    }

    public long getMyId() {
        return myId;
    }

    public void setMyId(long myId) {
        this.myId = myId;
    }

    public String getMyText() {
        return myText;
    }

    public void setMyText(String myText) {
        this.myText = myText;
    }

    public Date getMyModificationDate() {
        return myModificationDate;
    }

    public void setMyModificationDate(Date myModificationDate) {
        this.myModificationDate = myModificationDate;
    }

    public long getMyBookId() {
        return myBookId;
    }

    public Date getMyCreationDate() {
        return myCreationDate;
    }

    public void save() {
        myId = BooksDatabase.Instance().saveSelection(this);
    }

    public void update() {
        BooksDatabase.Instance().updateSelection(this);
    }

    public void delete() {
        if (myId != -1) {
            BooksDatabase.Instance().deleteSelection(this);
        }
    }
}
