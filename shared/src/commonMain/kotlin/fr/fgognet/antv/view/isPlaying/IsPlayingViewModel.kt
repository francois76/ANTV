package fr.fgognet.antv.view.isPlaying

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel

private const val TAG = "ANTV/IsPlayingViewModel"

data class IsPlayingData(
    val hasPlayingData: Boolean,
    val imageCode: String,
    val title: String,
    val description: String,
)


abstract class IsPlayingViewModelCommon : ViewModel() {

    fun start() = apply { initialize() }

    protected val _isPlayingData: MutableLiveData<IsPlayingData> =
        MutableLiveData(
            IsPlayingData(
                hasPlayingData = false,
                imageCode = "",
                title = "",
                description = ""
            )
        )
    val isPlayingData: LiveData<IsPlayingData> get() = _isPlayingData


    abstract fun initialize()


}

expect class IsPlayingViewModel() : IsPlayingViewModelCommon {

}

