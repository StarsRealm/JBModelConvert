package com.starsrealm.jbmodelconvert

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

fun main() {
    test()
    val image1 = ImageIO.read(File("C:\\Users\\Peng_Lx\\Documents\\StarsRealm\\JBModelConvert\\test\\black_cushion.png"))
    val image2 = ImageIO.read(File("C:\\Users\\Peng_Lx\\Documents\\StarsRealm\\JBModelConvert\\test\\acacia_bench.png"))

    // 计算合并后的宽度和高度
    val width = image1.width + image2.width
    val height = maxOf(image1.height, image2.height)

    // 创建一个新的图像
    val combinedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g: Graphics2D = combinedImage.createGraphics()

    // 将第一个图像绘制到新图像上
    g.drawImage(image1, 0, 0, null)

    // 将第二个图像绘制到新图像上
    g.drawImage(image2, image1.width, 0, null)

    // 释放图形上下文
    g.dispose()

    // 保存合并后的图像
    ImageIO.write(combinedImage, "png", File("C:\\Users\\Peng_Lx\\Documents\\StarsRealm\\JBModelConvert\\test\\output.png"))
}

fun test() {
    val json = Json {
        encodeDefaults = false // 忽略默认值（包括null值）
        ignoreUnknownKeys = true // 允许忽略未知的键
        explicitNulls = false // 忽略null值的字段
    }

    var path = "C:\\Users\\Peng_Lx\\Documents\\StarsRealm\\JBModelConvert\\test\\corner.json"

    val model = json.decodeFromString<Model>(File(path).readText())

    Convert().remapUV(model)
    println(json.encodeToJsonElement(model))
}

fun load() {
//    val testModel = Model("thx simple", "not parten", "123123", listOf(128, 128), mapOf(Pair("0", "123")), listOf(
//        Element(
//            listOf(0.0, 14.0, 13.0),
//            listOf(3.0, 28.0, 16.0),
//            Rotation(
//                7.5,
//                "z",
//                listOf(3.0, 14.0, 13.0)
//            ),
//            mapOf(
//                Pair("north", Faces(
//                    listOf(9.75, 12.375, 10.125, 14.125),
//                    "#1",
//                    null,
//                )),
//                Pair("south", Faces(
//                    listOf(9.375, 12.375, 9.75, 14.125),
//                    "#1",
//                    null,
//                ))
//            )
//        )
//    ))
//
//    println(Json.encodeToJsonElement(testModel))
}