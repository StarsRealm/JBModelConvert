package com.starsrealm.jbmodelconvert

class Convert {

    fun remapUV(model: Model): Model {
        val texturesAmount = getTexturesAmount(model)
        if(texturesAmount == 1) return model
        model.elements.forEach {
            processElements(it, model.textures, model.texture_size[0], model.texture_size[0] * texturesAmount)
        }
        model.texture_size = listOf(model.texture_size[0] * texturesAmount, model.texture_size[1])
        return model
    }

    fun processElements(element: Element, map: LinkedHashMap<String, String>, oldSize: Int, newSize: Int) {
        element.faces.values.forEach { face ->
            val key = face.texture
            val index = getIndex(key, map)
            face.uv = listOf(face.uv[0] * oldSize / 16 + index * oldSize, face.uv[1], face.uv[2] * oldSize / 16 + index * oldSize, face.uv[3])
            face.uv = listOf(face.uv[0] / newSize * 16,  face.uv[1], face.uv[2] / newSize  * 16, face.uv[3])
        }
    }

    fun getIndex(key: String, map: LinkedHashMap<String, String>) : Int {
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


    fun getTexturesAmount(model: Model): Int {
        var amount = 0
        model.textures.forEach {
            if(it.key != "particle") {
                amount++
            }
        }
        return amount;
    }
}