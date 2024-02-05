package com.climus.climeet.presentation.ui.main.record.timer.data

import javax.inject.Inject

class StopwatchStatesRepositoryImpl @Inject constructor(
    private val stopwatchStatesDao: StopwatchStatesDao
) : StopwatchStatesRepository {

    override fun insertState(state: StopwatchStatesData) {
        stopwatchStatesDao.insert(state)
    }

    override fun updateState(state: StopwatchStatesData) {
        stopwatchStatesDao.update(state)
    }

    override fun deleteState(state: StopwatchStatesData) {
        stopwatchStatesDao.delete(state)
    }

    override fun getAllStates(): List<StopwatchStatesData> {
        return stopwatchStatesDao.getAll()
    }

    override fun getState(id: Int): StopwatchStatesData {
        return stopwatchStatesDao.getState(id)
    }

    override fun initialize() {
        stopwatchStatesDao.initialize()
    }
}