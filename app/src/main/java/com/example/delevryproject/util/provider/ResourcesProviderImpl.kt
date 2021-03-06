package com.example.delevryproject.util.provider

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourcesProviderImpl (
    private val context: Context
) : ResourcesProvider {

    override fun getString(@StringRes resId: Int): String = context.getString(resId)

    override fun getString(@StringRes resId: Int, vararg formatArgs: Any): String = context.getString(resId, *formatArgs)

    override fun getColorStateList(@ColorRes resId: Int) = context.getColorStateList(resId)

}
