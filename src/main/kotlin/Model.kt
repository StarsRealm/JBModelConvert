package com.starsrealm.jbmodelconvert

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.util.Objects

@Serializable
data class Model(
    var parent : String?,
    var render_type: String?,
    var texture_size: List<Int> = listOf(16, 16),
    val textures: LinkedHashMap<String, String>,
    val elements: List<Element>,
)

@Serializable
data class Element(
    var from: List<Double>,
    var to: List<Double>,
    var rotation: Rotation?,
    var faces: Map<String, Faces>
)

@Serializable
data class Rotation(
    var angle: Double?,
    var axis: String?,
    var origin: List<Double>?
)

@Serializable
data class Faces(
    var uv: List<Double>,
    var texture: String,
    var rotation: Double?
)