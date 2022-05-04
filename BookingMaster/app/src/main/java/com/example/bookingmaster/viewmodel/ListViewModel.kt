package com.example.bookingmaster.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmaster.api.BookingMasterRepository
import com.example.bookingmaster.model.*
import kotlinx.coroutines.launch

class ListViewModel(private val repository: BookingMasterRepository, private val sharedPref: SharedPreferences) : ViewModel() {

    var tag = "ListViewModel"
    var accommodations: MutableLiveData<ArrayList<Accommodation>> = MutableLiveData()
    var accommodationsBudapest: MutableLiveData<ArrayList<Accommodation>> = MutableLiveData()
    var accommodationsDebrecen: MutableLiveData<ArrayList<Accommodation>> = MutableLiveData()
    var currentAccommodation: Accommodation? = null
    var token: String
    var currentRoom: Room? = null

    var userBookings: MutableLiveData<ArrayList<BookingFull>> = MutableLiveData()

    init {

        token = "Bearer ${sharedPref.getString("token", "").toString()}"
        Log.d(tag, "ListViewModel - token: ${token}")

        getDefaultAccommodations()
        getUserBookings()
    }

    private fun getUserBookings() {
        viewModelScope.launch {
            try{
                var email = sharedPref.getString("email","").toString()
                var result = repository.getUserBookings(email)
                userBookings.value = result.bookingsByUser as ArrayList<BookingFull>

                Log.d(tag, "ListViewModel user bookings result: ${result}")

            }catch(e: Exception){
                Log.d(tag, "ListViewModel exception: $e")
            }
        }
    }

    private fun getDefaultAccommodations() {
        viewModelScope.launch {
            try {


                var result = repository.getAccommodationsByLocation(token = token,"Budapest")
                accommodations.value = result.accomodations as ArrayList<Accommodation>
                accommodationsBudapest.value = accommodations.value

                result = repository.getAccommodationsByLocation(token = token,"Debrecen")
                accommodationsDebrecen.value = result.accomodations as ArrayList<Accommodation>

                Log.d(tag, "ListViewModel - #accommodations:  ${result.accomodations}")
            } catch (e: Exception) {
                Log.d(tag, "ListViewModel exception: $e")
            }
        }
    }

    fun getAccommodations(location : String) {
        viewModelScope.launch {
            try {

                var result = repository.getAccommodationsByLocation(token = token, location)
                accommodations.value = result.accomodations as ArrayList<Accommodation>
                Log.d(tag, "ListViewModel - #accommodations:  ${result.accomodations}")
            } catch (e: Exception) {
                Log.d(tag, "ListViewModel exception: $e")
            }
        }
    }

    fun addBooking(booking: BookingRequest) {
        viewModelScope.launch {
            val result = repository.addBooking(booking)
            Log.i("result", result.toString())
        }
    }


}