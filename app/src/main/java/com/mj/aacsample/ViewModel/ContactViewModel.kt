package com.mj.aacsample.ViewModel

import android.app.Application
import androidx.lifecycle.*
import com.mj.aacsample.Room.Contact

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContactRepository(application)
    private val contacts = repository.getAll()

    fun getAll(): LiveData<List<Contact>> {
        return this.contacts
    }

    fun insert(contact: Contact) {
        repository.insert(contact)
    }

    fun delete(contact: Contact) {
        repository.delete(contact)
    }


    class Factory(val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            return ContactViewModel(application) as T
        }
    }
}