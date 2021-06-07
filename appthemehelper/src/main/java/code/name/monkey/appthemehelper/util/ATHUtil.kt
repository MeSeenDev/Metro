package code.name.monkey.appthemehelper.util

import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes

/**
 * @author Aidan Follestad (afollestad)
 */
object ATHUtil {

    fun isWindowBackgroundDark(context: Context): Boolean {
        return !ColorUtil.isColorLight(resolveColor(context, android.R.attr.windowBackground))
    }

    @JvmOverloads
    fun resolveColor(context: Context, @AttrRes attr: Int, fallback: Int = 0): Int {
        val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
        return try {
            a.getColor(0, fallback)
        }catch (thr: Throwable){ //TODO fix this
            Color.BLACK
        } finally {
            a.recycle()
        }
    }
}