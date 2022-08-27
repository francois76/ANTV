package fr.fgognet.antv.mapping

expect class Bundle() {


    fun deepCopy(): Bundle?

    fun clear()

    fun remove(key: String?)

    fun putAll(bundle: Bundle?)


    fun hasFileDescriptors(): Boolean


    fun putByte(key: String?, value: Byte)

    fun putChar(key: String?, value: Char)

    fun putShort(key: String?, value: Short)

    fun putFloat(key: String?, value: Float)

    fun putCharSequence(key: String?, value: CharSequence?)


    fun putByteArray(key: String?, value: ByteArray?)

    fun putShortArray(key: String?, value: ShortArray?)

    fun putCharArray(key: String?, value: CharArray?)

    fun putFloatArray(key: String?, value: FloatArray?)

    fun putCharSequenceArray(key: String?, value: Array<CharSequence?>?)

    fun putBundle(key: String?, value: Bundle?)

    fun putString(key: String?, value: String?)


    fun getByte(key: String?): Byte

    fun getByte(key: String?, defaultValue: Byte): Byte?

    fun getChar(key: String?): Char

    fun getChar(key: String?, defaultValue: Char): Char

    fun getShort(key: String?): Short

    fun getShort(key: String?, defaultValue: Short): Short

    fun getFloat(key: String?): Float

    fun getFloat(key: String?, defaultValue: Float): Float


    fun getCharSequence(key: String?): CharSequence?

    fun getCharSequence(key: String?, defaultValue: CharSequence?): CharSequence?


    fun getBundle(key: String?): Bundle?


    fun getByteArray(key: String?): ByteArray?


    fun getShortArray(key: String?): ShortArray?


    fun getCharArray(key: String?): CharArray?


    fun getFloatArray(key: String?): FloatArray?


    fun getCharSequenceArray(key: String?): Array<CharSequence?>?


    fun describeContents(): Int


    override fun toString(): String


}