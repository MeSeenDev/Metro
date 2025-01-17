/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package io.github.muntashirakon.music.ui.transform

import android.view.View
import androidx.viewpager.widget.ViewPager

class VerticalStackTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        if (position >= 0) {
            page.scaleX = (0.9f - 0.05f * position)
            page.scaleY = 0.9f
            page.translationX = -page.width * position
            page.translationY = -30 * position
        }
    }
}
