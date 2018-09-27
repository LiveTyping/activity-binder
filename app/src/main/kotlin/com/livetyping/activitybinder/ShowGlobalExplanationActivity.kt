package com.livetyping.activitybinder

import com.livetyping.permission.PreSettingsActivity


class ShowGlobalExplanationActivity : PreSettingsActivity() {

    override fun layoutResId(): Int = R.layout.activity_permission_explanation

    override fun settingsButtonId(): Int = R.id.permission_settings
}