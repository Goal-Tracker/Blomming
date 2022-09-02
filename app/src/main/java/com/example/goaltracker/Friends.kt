package com.example.goaltracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

// 리사이클러 뷰 아이템
class Friends (var UserName : String? = null,
               var Email : String? = null,
               var Uid : String? = null,
                var UserColor : String?=null)
