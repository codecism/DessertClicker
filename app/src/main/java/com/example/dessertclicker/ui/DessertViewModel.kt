package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    fun onDessertClicked() {
        _uiState.update { currentState ->
            val newDessertsSold = currentState.dessertsSold + 1
            val newDessertToShow = determineDessertToShow(newDessertsSold)
            currentState.copy(
                // update revenue
                revenue = currentState.revenue + currentState.currentDessertPrice,
                dessertsSold = newDessertsSold,

                // show next desert
                currentDessertImageId = newDessertToShow.imageId,
                currentDessertPrice = newDessertToShow.price,
            )
        }
    }

    private fun determineDessertToShow(dessertsSold: Int): Dessert {
        var dessertToShow = dessertList.first()
        for (dessert in dessertList) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }
}