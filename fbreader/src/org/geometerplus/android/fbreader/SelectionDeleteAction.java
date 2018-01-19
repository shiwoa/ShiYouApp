package org.geometerplus.android.fbreader;

import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.fbreader.FBView;
import org.geometerplus.fbreader.library.BookSelection;
import org.geometerplus.fbreader.library.Library;
import org.geometerplus.zlibrary.text.view.ZLTextPosition;

import java.util.ArrayList;

public class SelectionDeleteAction extends FBAndroidAction {
    SelectionDeleteAction(FBReader baseActivity, FBReaderApp fbreader) {
        super(baseActivity, fbreader);
    }

    @Override
    protected void run(Object... params) {
        FBView fbView = Reader.getTextView();
        String selectedText = fbView.getSelectedText();
        ZLTextPosition selectionStartPosition = fbView.getSelectionStartPosition();
        ZLTextPosition selectionEndPosition = fbView.getSelectionEndPosition();
        int paragraphIndex = selectionStartPosition.getParagraphIndex();
        int elementIndex = selectionStartPosition.getElementIndex();
        int endIndex = selectionEndPosition.getElementIndex();
        long bookId = Reader.Model.Book.getId();
        String replaceAll = selectedText.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
        ArrayList<BookSelection> bookSelectionList = Library.Instance().getBookSelection(bookId, paragraphIndex, elementIndex, endIndex);
        if (bookSelectionList != null && bookSelectionList.size() != 0) {
            for (int i = 0; i < bookSelectionList.size(); i++) {
                BookSelection bookSelection = bookSelectionList.get(i);
                bookSelection.delete();
            }
        }
        fbView.clearSelection();
    }
}