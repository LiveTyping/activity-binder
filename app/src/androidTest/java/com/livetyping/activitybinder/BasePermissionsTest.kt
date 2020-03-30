package com.livetyping.activitybinder

import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.core.testcontext.BaseTestContext
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.livetyping.activitybinder.screens.MainActivityScreen
import kotlinx.coroutines.awaitAll
import org.junit.AfterClass
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BasePermissionsTest : TestCase(Kaspresso.Builder.simple()) {
    @get:Rule
    var activityTestRule = MainActivityTestRule()


    protected fun before() {

    }

    protected fun after() {
        activityTestRule.finishActivity()
        adbServer.disconnectServer()
    }


}