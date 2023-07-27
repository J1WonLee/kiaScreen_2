package com.copy.kiascreen.util.textlink

import android.text.util.Linkify
import android.widget.TextView
import java.util.regex.Matcher
import java.util.regex.Pattern

class TextLink(private val textView : TextView) {
    private val mTransForm = Linkify.TransformFilter { _, _ ->
        ""
    }

    fun setLinkify(txt : String) {
        val pattern = Pattern.compile(txt)
        Linkify.addLinks(textView, pattern, "http://www.kncap.org/indexNew.jsp", null, mTransForm)
    }
}