package fr.fgognet.antv.view.card

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korio.async.launch
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.external.image.ImageRepository.getLiveImage

class CardViewModel : ViewModel() {
    private val _image: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val image: LiveData<Bitmap?> = _image.readOnly()


    fun loadImage(code: String) {
        viewModelScope.launch {
            try {
                _image.value = getLiveImage(code)
            } catch (error: Exception) {
            }
        }
    }
}