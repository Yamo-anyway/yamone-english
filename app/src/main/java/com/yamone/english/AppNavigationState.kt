package com.yamone.english

internal object AppNavigationState {
    fun sanitizeTabIndex(index: Int, tabCount: Int): Int {
        if (tabCount <= 0) return 0
        return if (index in 0 until tabCount) index else 0
    }

    fun backTarget(index: Int, tabCount: Int): Int? {
        val safeIndex = sanitizeTabIndex(index, tabCount)
        return if (safeIndex == 0) null else 0
    }
}
