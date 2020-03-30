package com.livetyping.core

import android.app.Activity
import com.livetyping.core.holder.activity.ActivityHolder
import com.livetyping.core.holder.activity.ActivityHolderImpl
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ActivityHolderImplTest {
    private lateinit var activityHolder: ActivityHolder
    private lateinit var activity: Activity

    @Before
    fun setUp() {
        activityHolder = ActivityHolderImpl()
        activity = mockk(relaxed = true)
    }

    @Test
    fun `Should save activity when onActivityStarted() is called`() {
        //Given

        //When
        activityHolder.add(activity)

        //Then
        val result = activityHolder.getCurrentActivity()
        assertEquals(activity, result)
    }

    @Test
    fun `Should not add activity if null was passed`(){
        //When
        activityHolder.add(null)

        //Then
        val result = activityHolder.getCurrentActivity()
        assertNull(result)
    }

    @Test
    fun `Should remove activity`() {

        //When
        activityHolder.add(activity)
        activityHolder.remove(activity)

        //Then
        val result = activityHolder.getCurrentActivity()
        assertNull(result)
    }

    @Test
    fun `Should should remove activity when onActivityStopped() is called`() {
        //Given
        val activity2: Activity = mockk(relaxed = true)
        //When
        activityHolder.add(activity)
        activityHolder.add(activity2)

        //Then
        val result = activityHolder.getCurrentActivity()
        assertEquals(activity2, result)
    }

    @Test
    fun `Should return previous activity if previous was removed`(){
        //Given
        val activity2: Activity = mockk(relaxed = true)

        //When
        activityHolder.add(activity)
        activityHolder.add(activity2)
        activityHolder.remove(activity2)

        //Then
        val result = activityHolder.getCurrentActivity()
        assertEquals(activity, result)
    }

}