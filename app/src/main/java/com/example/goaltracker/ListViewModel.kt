package com.example.goaltracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//class ListViewModel : ViewModel() {
//
//    private val repo = Repo()
//
//    //firestore에 저장된 데이터 불러오기
//    fun fetchData(): LiveData<MutableList<Goal>> {
//        val mutableData = MutableLiveData<MutableList<Goal>>()
//        repo.getData().observeForever {
//            mutableData.value=it
//        }
//        return mutableData
//    }
//}