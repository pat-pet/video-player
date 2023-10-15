package com.patpat.videoplayer.navigation

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.patpat.videoplayer.domain.models.ContentVideoModel

class AssetParamType : NavType<ContentVideoModel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ContentVideoModel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, ContentVideoModel::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): ContentVideoModel {
        return Gson().fromJson(value, ContentVideoModel::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: ContentVideoModel) {
        bundle.putParcelable(key, value)
    }
}
