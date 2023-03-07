package com.akshayholla.openweather.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshayholla.openweather.data.transformToAppUnit
import com.akshayholla.openweather.repository.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


//TODO: Not implemented yet
@HiltViewModel
class SettingsViewModel @Inject constructor(val userRepo: UserRepo) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingViewState>(value = SettingViewState.Loading)
    val uiState: StateFlow<SettingViewState> = _uiState
    fun getCurrentSettings() {
        viewModelScope.launch {
            val currentUnit = userRepo.getUnit()
            _uiState.value = SettingViewState.AvailableSettings(currentUnit.transformToAppUnit())
        }
    }

    fun setUnitToMetric() {
        viewModelScope.launch {
            userRepo.saveUnit(com.akshayholla.openweather.data.Unit.METRIC.param)
        }
    }

    fun setUnitToImperial() {
        viewModelScope.launch {
            userRepo.saveUnit(com.akshayholla.openweather.data.Unit.IMPERIAL.param)
        }
    }
}

sealed class SettingViewState {
    object Loading : SettingViewState()
    data class AvailableSettings(val unit: com.akshayholla.openweather.data.Unit) : SettingViewState()
}