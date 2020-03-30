package com.livetyping.activitybinder.screens

import com.agoda.kakao.text.KTextView
import com.livetyping.activitybinder.R

object DialogExplanation: KScreen<DialogExplanation>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val dialogTitle = KTextView{
        withText(R.string.active_permission_rationale_text)
    }
}