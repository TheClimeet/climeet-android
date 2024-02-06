package com.climus.climeet.presentation.ui.main.record.timer.data

interface StopwatchStatesRepository {

    fun insertState(state: StopwatchStatesData)

    fun updateState(state: StopwatchStatesData)

    fun deleteState(state: StopwatchStatesData)

    fun getAllStates(): List<StopwatchStatesData>

    fun getState(id: Int): StopwatchStatesData

    fun initialize()
}