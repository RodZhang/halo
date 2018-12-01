package com.rod.halo

import java.io.File

/**
 *
 * @author Rod
 * @date 2018/12/2
 */
object FileUtil {

    fun isFileExist(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }
}