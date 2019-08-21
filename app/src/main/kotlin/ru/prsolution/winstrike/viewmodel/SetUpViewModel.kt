package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.domain.models.arena.mapToPresentation
import ru.prsolution.winstrike.domain.usecases.ArenaUseCase
import ru.prsolution.winstrike.presentation.model.arena.ScheduleItem
import ru.prsolution.winstrike.presentation.model.arena.SchemaItem
import ru.prsolution.winstrike.presentation.model.arena.mapToPresentation
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class SetUpViewModel constructor(val arenaUseCase: ArenaUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    // Выбранная  арена по времени и дате
    val arenaSchema = SingleLiveEvent<SchemaItem?>()
    val error = SingleLiveEvent<String>()
    // Расписание работы арен
    val schedulers = SingleLiveEvent<List<ScheduleItem>?>()

    fun fetchSchema(arenaPid: String?, time: Map<String, String>) {
        scope.launch {
            val schema = arenaUseCase.get(arenaPid, time, refresh = true)
            if (schema?.state == ResourceState.SUCCESS){
                arenaSchema.postValue(schema?.data?.mapToPresentation())
            } else error.postValue(schema?.message)

        }
    }

    fun fetchHallSchema(arenaPid: String?, time: Map<String, String>) {
        scope.launch {
            val schema = arenaUseCase.getHall(arenaPid, time, refresh = true)
            if (schema?.state == ResourceState.SUCCESS){
                arenaSchema.postValue(schema?.data?.mapToPresentation())
            } else error.postValue(schema?.message)

        }
    }

    // Получить расписание для арен
    fun getSchedule() {
        scope.launch {
            val schedule = arenaUseCase.getSchedule()
            if (schedule?.state == ResourceState.SUCCESS){
                schedulers.postValue(schedule?.data?.mapToPresentation())
            } else error.postValue(schedule?.message)

        }
    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}