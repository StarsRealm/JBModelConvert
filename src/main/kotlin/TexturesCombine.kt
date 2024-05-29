package com.starsrealm.jbmodelconvert

import kotlinx.coroutines.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object TexturesCombine {
    fun convert() {
        val folderPaths = listOf("data/folder1", "data/folder2", "data/folder3", "data/folder4", "data/folder5", "data/folder6")
        val outputFolder = "data/output"

        runBlocking {
            combineImages(folderPaths, outputFolder)
        }
    }

    suspend fun combineImages(folderPaths: List<String>, outputFolder: String) {
        val imageLists = folderPaths.map { loadImagesFromFolder(it) }.filter { it.isNotEmpty() }
        val combinations = cartesianProduct(imageLists)

        val outputDir = File(outputFolder)
        if (!outputDir.exists()) outputDir.mkdirs()

        combinations.forEach { images ->
            val combinedImage = combineImagesHorizontally(images.map { it.first })
            val outputFileName = images.joinToString("_") { it.second }
            val outputFile = File(outputDir, "$outputFileName.png")
            ImageIO.write(combinedImage, "png", outputFile)
        }
    }

    fun loadImagesFromFolder(folderPath: String): List<Pair<BufferedImage, String>> {
        val folder = File(folderPath)
        return folder.listFiles { _, name -> name.endsWith(".png") || name.endsWith(".jpg") }
            ?.mapNotNull { file ->
                val image = ImageIO.read(file)
                if (image != null) Pair(image, file.nameWithoutExtension) else null
            } ?: emptyList()
    }

    fun cartesianProduct(lists: List<List<Pair<BufferedImage, String>>>): List<List<Pair<BufferedImage, String>>> {
        if (lists.isEmpty()) return listOf(emptyList())
        val result = mutableListOf<List<Pair<BufferedImage, String>>>()
        val firstList = lists[0]
        val remainingLists = lists.subList(1, lists.size)
        val remainingProduct = cartesianProduct(remainingLists)
        for (item in firstList) {
            for (product in remainingProduct) {
                val newList = mutableListOf(item)
                newList.addAll(product)
                result.add(newList)
            }
        }
        return result
    }

    fun combineImagesHorizontally(images: List<BufferedImage>): BufferedImage {
        val width = images.sumOf { it.width }
        val height = images.maxOf { it.height }
        val combined = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        val g = combined.graphics
        var xOffset = 0
        images.forEach { image ->
            g.drawImage(image, xOffset, 0, null)
            xOffset += image.width
        }
        g.dispose()

        return combined
    }
}