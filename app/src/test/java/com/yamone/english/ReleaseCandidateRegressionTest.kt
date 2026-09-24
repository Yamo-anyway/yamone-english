package com.yamone.english

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReleaseCandidateRegressionTest {
    @Test
    fun invalidRestoredTabAlwaysFallsBackToToday() {
        val tabCount = 5
        assertEquals(0, AppNavigationState.sanitizeTabIndex(-1, tabCount))
        assertEquals(0, AppNavigationState.sanitizeTabIndex(tabCount, tabCount))
        assertEquals(0, AppNavigationState.sanitizeTabIndex(Int.MAX_VALUE, tabCount))
        assertEquals(0, AppNavigationState.sanitizeTabIndex(3, 0))
        assertEquals(3, AppNavigationState.sanitizeTabIndex(3, tabCount))
    }

    @Test
    fun systemBackFromSecondaryTabReturnsToTodayBeforeExit() {
        val tabCount = 5
        assertNull(AppNavigationState.backTarget(0, tabCount))
        assertEquals(0, AppNavigationState.backTarget(1, tabCount))
        assertEquals(0, AppNavigationState.backTarget(4, tabCount))
        assertNull(AppNavigationState.backTarget(999, tabCount))
    }
}
