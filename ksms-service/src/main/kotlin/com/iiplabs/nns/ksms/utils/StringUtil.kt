package com.iiplabs.nns.ksms.utils

class StringUtil {
    companion object {
        fun getLastField(path: String): String {
            return path.split("\\.".toRegex()).last()
        }
    }
}