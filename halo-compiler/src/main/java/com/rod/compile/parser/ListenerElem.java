package com.rod.compile.parser;

import javax.lang.model.element.ExecutableElement;

/**
 * @author Rod
 * @date 2019-07-09
 */
public class ListenerElem {

    public final int mViewId;
    public final ExecutableElement mElem;
    public final Parser mParser;

    public ListenerElem(int viewId, ExecutableElement elem, Parser parser) {
        mViewId = viewId;
        mElem = elem;
        mParser = parser;
    }
}
