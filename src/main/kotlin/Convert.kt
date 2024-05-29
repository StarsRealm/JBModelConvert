package com.starsrealm.jbmodelconvert

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File

object Convert {

    fun process() {
        val json = Json {
            encodeDefaults = false // 忽略默认值（包括null值）
            ignoreUnknownKeys = true // 允许忽略未知的键
            explicitNulls = false // 忽略null值的字段
        }

        val file = File("data/model")

        if (!file.exists()) {
            file.mkdirs()
        }

        file.listFiles()?.forEach {
            val model = json.decodeFromString<Model>(it.readText())
            Convert.remapUV(model)
            it.delete()
            it.createNewFile()
            it.writeText(json.encodeToJsonElement(model).toString())
        }

    }

    fun remapUV(model: Model): Model {
        val texturesAmount = getTexturesAmount(model)
        if(texturesAmount == 1) return model
        model.elements.forEach {
            processElements(it, model.textures, model.texture_size[0], model.texture_size[0] * texturesAmount)
        }
        model.texture_size = listOf(model.texture_size[0] * texturesAmount, model.texture_size[1])
        return model
    }

    private fun processElements(element: Element, map: LinkedHashMap<String, String>, oldSize: Int, newSize: Int) {
        element.faces.values.forEach { face ->
            val key = face.texture
            val index = getIndex(key, map)
            face.uv = listOf(face.uv[0] * oldSize / 16 + index * oldSize, face.uv[1], face.uv[2] * oldSize / 16 + index * oldSize, face.uv[3])
            face.uv = listOf(face.uv[0] / newSize * 16,  face.uv[1], face.uv[2] / newSize  * 16, face.uv[3])
        }
    }

    private fun getIndex(key: String, map: LinkedHashMap<String, String>) : Int {
        var index = 0
        map.forEach { (t, _) ->
            if("#$t".equals(key, ignoreCase = true)) {
                return index
            } else {
                index++
            }
        }

        return index
    }


    private fun getTexturesAmount(model: Model): Int {
        var amount = 0
        model.textures.forEach {
            if(it.key != "particle") {
                amount++
            }
        }
        return amount;
    }
}