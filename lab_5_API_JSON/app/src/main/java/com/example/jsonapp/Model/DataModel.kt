package com.example.jsonapp.Model

class DataModel {
    private var id: Int? = null
    private var name: String? = null
    private var surname: String? = null
    private var email: String? = null

    fun getId(): Int {
        return id!!.toInt()
    }

    fun getName(): String{
        return name.toString()
    }

    fun getSurname(): String{
        return surname.toString()
    }

    fun getEmail(): String{
        return email.toString()
    }

    fun setId(id: Int){
        this.id = id
    }

    fun setName(name: String){
        this.name = name
    }

    fun setSurname(surname: String){
        this.surname = surname
    }

    fun setEmail(email: String){
        this.email = email
    }
}