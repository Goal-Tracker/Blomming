package com.example.goaltracker



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class FriendAdapter(private val context: MutableList<Friends>) :
    RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    private var friend = mutableListOf<Friends>()


    fun loadFriend(newData: MutableList<Friends>) {
        friend.clear()
        friend.addAll(newData)


    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val FriendName: TextView = itemView.findViewById(R.id.FriendName)
        private val FriendEmail: TextView = itemView.findViewById(R.id.FriendEmail)
        private val FriendColor: ImageView = itemView.findViewById(R.id.FriendColor)

        fun setFriendName(item: Friends){
            FriendName.text = item.userName
        }
        fun SetFriendColor(item:Friends){
            var circleResource = 0
            when (item.userColor) {
                "f69b94" -> circleResource = R.drawable.b_f69b94
                "f8c8c4" -> circleResource = R.drawable.b_f8c8c4
                "fcdcce" -> circleResource = R.drawable.b_fcdcce
                "96b0e5" -> circleResource = R.drawable.b_96b0e5
                "92b9e2" -> circleResource = R.drawable.b_92b9e2
                "ebc0c7" -> circleResource = R.drawable.b_ebc0c7
                "7bb6c8" -> circleResource = R.drawable.b_7bb6c8
                "aad3d7" -> circleResource = R.drawable.b_aad3d7
                "f5f1f0" -> circleResource = R.drawable.b_f5f1f0
                "d5e3e6" -> circleResource = R.drawable.b_d5e3e6
                "f2a4b1" -> circleResource = R.drawable.b_f2a4b1
                "7175a5" -> circleResource = R.drawable.b_7175a5
                "a1b3d7" -> circleResource = R.drawable.b_a1b3d7
                "bd83cf" -> circleResource = R.drawable.b_bd83cf
                "e5afe9" -> circleResource = R.drawable.b_e5afe9

            }
            FriendColor.setImageResource(circleResource)
        }
        fun SetFriendEmail(item:Friends){
            FriendEmail.text = item.email
        }

    }

    // 리사이클러뷰의 아이템 총 개수 반환
    override fun getItemCount() = friend.size

    // xml파일을 inflate하여 ViewHolder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(view)
    }

    // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        holder.setFriendName(friend[position])
        holder.SetFriendColor(friend[position])
        holder.SetFriendEmail(friend[position])


    }


}
