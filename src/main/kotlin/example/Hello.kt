package example

import com.subgraph.orchid.encoders.Hex
import org.bitcoinj.core.Block
import org.bitcoinj.core.Context
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.utils.BlockFileLoader
import java.io.File
import java.io.FileOutputStream

val mainNetParams = MainNetParams()
val blockChainFiles = listOf(File("./btc-data/blocks/blk00000.dat"))

val leadingMagicBytes = Hex.decode("f9beb4d91d010000")!!

fun writeToFile(block: Block, file: File) {
    if (!file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    val byteArrayStream = FileOutputStream(file)
    byteArrayStream.write(leadingMagicBytes)
    block.bitcoinSerialize(byteArrayStream)
    byteArrayStream.close()
}

fun loadAgain(file: File) {
    val loader = BlockFileLoader(mainNetParams, listOf(file))
    val block = loader.next()
    println(block)
}

fun main(args: Array<String>) {
    Context.getOrCreate(mainNetParams)

    val loader = BlockFileLoader(mainNetParams, blockChainFiles)
    val block = loader.next()

    val blockFile = File("./out/block.data")
    writeToFile(block, blockFile)

    loadAgain(blockFile)
}