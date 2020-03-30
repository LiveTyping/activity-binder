package com.livetyping.activitybinder.permissions

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.kaspersky.kaspresso.annotations.RequiresAdbServer
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.core.testcontext.BaseTestContext
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.livetyping.activitybinder.BasePermissionsTest
import com.livetyping.activitybinder.MainActivity
import com.livetyping.activitybinder.MainActivityTestRule
import com.livetyping.activitybinder.PermissionExampleActivity
import com.livetyping.activitybinder.screens.DialogExplanation
import com.livetyping.activitybinder.screens.MainActivityScreen
import com.livetyping.activitybinder.screens.PermissionScreen
import junit.framework.Assert
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActiveSinglePermissionsTest : TestCase(
    Kaspresso.Builder.simple()
) {

    @get:Rule
    var activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)
    private val activePermission = CAMERA

    @RequiresAdbServer
    @Test
    fun testAllowingActivePermission() {
        before {
            before()
        }.after {
            after()
        }.run {
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("pm reset-permissions ${device.targetContext.packageName}")
            shouldOpenPermissionScreen(this)
            shouldShowPermissionDialog(this)

            step("Should grant permission") {
                device.permissions.allowViaDialog()
                assertTrue(hasGrantedPermission())
            }

            shouldNotShowPermissionDialogAgain(this)
        }
    }


    private fun <T> shouldOpenPermissionScreen(testContext: TestContext<T>) {
        testContext.step("Should open permission screen") {
            MainActivityScreen {
                permissionButton {
                    click()
                }
            }
        }
    }

    private fun <T> shouldShowPermissionDialog(testContext: TestContext<T>) {
        testContext.step("Should open permission dialog") {
            PermissionScreen {
                activePermissionButton {
                    click()
                }

                Assert.assertTrue(device.permissions.isDialogVisible())
            }
        }
    }

    private fun <T> shouldNotShowPermissionDialogAgain(testContext: TestContext<T>) {
        testContext.step("Should not show permission dialog again") {
            PermissionScreen {
                activePermissionButton {
                    click()
                    click()
                    click()
                    click()
                    click()
                }

                Assert.assertFalse(device.permissions.isDialogVisible())
            }
        }
    }


    private fun BaseTestContext.hasGrantedPermission(): Boolean =
        device.targetContext.checkSelfPermission(activePermission) ==
                PackageManager.PERMISSION_GRANTED




    private fun before() {
        clearPassivePermission()
        activityTestRule.launchActivity(null)
    }

    private fun after() {
        adbServer.disconnectServer()
    }

    private fun clearPassivePermission() {

    }
}