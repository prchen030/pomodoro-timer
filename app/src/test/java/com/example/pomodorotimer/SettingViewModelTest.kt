package com.example.pomodorotimer

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.pomodorotimer.data.SettingKeys
import com.example.pomodorotimer.data.SettingRepository
import com.example.pomodorotimer.model.PrefKeys
import com.example.pomodorotimer.model.TimerStates
import com.example.pomodorotimer.viewModel.SettingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import java.io.File
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var dataStoreFile: File
    private lateinit var repository: SettingRepository
    private lateinit var viewModel: SettingViewModel

    @Before
    fun setup() {
        dataStoreFile = File.createTempFile("test_prefs", ".preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { dataStoreFile }
        )
        repository = SettingRepository(dataStore)
        viewModel = SettingViewModel(repository)
    }

    @After
    fun tearDown() {
        dataStoreFile.delete()
        Dispatchers.resetMain()
    }

    @Test
    fun updateIsGranted_setGrantValue() = runTest {
        assertEquals(false, viewModel.isGranted.value)
        viewModel.setGrantValue(true)
        advanceUntilIdle()
        val res = viewModel.isGranted.first()
        assertEquals(res, true)
    }

    @Test
    fun updatePomodoroTime_updateIntValue() = runTest {
        assertEquals(TimerStates.POMODORO.default, viewModel.pomodoroTime.value)
        viewModel.updateIntValue(key = PrefKeys.KEY_POMODORO_TIME, value = 1)
        advanceUntilIdle()
        val res = viewModel.pomodoroTime.first { it == 1 }
        assertEquals(res, 1)
    }

    @Test
    fun updateShortBreakTime_updateIntValue() = runTest {
        assertEquals(TimerStates.SHORT_BREAK.default, viewModel.shortBreakTime.value)
        viewModel.updateIntValue(key = PrefKeys.KEY_SHORT_BREAK_TIME, value = 2)
        advanceUntilIdle()
        val res = viewModel.shortBreakTime.first { it == 2}
        assertEquals(res,2)
    }

    @Test
    fun updateLongBreakTime_updateIntValue() = runTest {
        assertEquals(TimerStates.LONG_BREAK.default, viewModel.longBreakTime.value)
        viewModel.updateIntValue(key = PrefKeys.KEY_LONG_BREAK_TIME, value = 3)
        advanceUntilIdle()
        val res = viewModel.longBreakTime.first { it == 3}
        assertEquals(res,3)
    }

    @Test
    fun updateIfNotification_updateBooleanValue() = runTest {
        assertEquals(false, viewModel.ifNotification.value)
        viewModel.updateBooleanValue(key = PrefKeys.KEY_IF_NOTIFICATION, value = true)
        advanceUntilIdle()
        val res = viewModel.ifNotification.first { it }
        assertEquals(res,true)
    }

    @Test
    fun updateIfSound_updateBooleanValue() = runTest {
        assertEquals(false, viewModel.ifSound.value)
        viewModel.updateBooleanValue(key = PrefKeys.KEY_IF_SOUND, value = true)
        advanceUntilIdle()
        val res = viewModel.ifSound.first { it }
        assertEquals(res,true)
    }

}