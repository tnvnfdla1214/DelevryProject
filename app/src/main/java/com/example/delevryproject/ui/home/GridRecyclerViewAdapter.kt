package com.example.delevryproject.ui.home

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.delevryproject.R
import com.example.delevryproject.data.local.model.GridItem
import com.example.delevryproject.ui.home.event.EventActivity

import kotlinx.android.synthetic.main.item_layout_grid.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GridRecyclerViewAdapter(private val interaction: Interaction) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var gridItemList: List<GridItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GridItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_grid, parent, false),
            interaction
        )
    }

    override fun getItemCount(): Int {
        return gridItemList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        gridItemList?.let {
            (holder as GridItemViewHolder).bind(it[position])
        }
    }

    //functions
    fun submitList(list: List<GridItem>?) {
        gridItemList = list
        notifyDataSetChanged()
    }


    class GridItemViewHolder(itemView: View ,private val interaction: Interaction) : RecyclerView.ViewHolder(itemView) {
        fun bind(gridItem: GridItem) {
            itemView.iv_grid_image.setImageResource(gridItem.image)
            itemView.tv_grid_title.text = gridItem.title

            if(gridItem.image == R.drawable.b){
                animateView(itemView.iv_grid_image)
            }
            itemView.setOnClickListener {
                //아이템넘겨주는건 여기 나중에 수정
                interaction.onGridItemClicked(itemView)
            }

        }

        // 애니메이션 함수
        private fun animateView(ivGridImage: ImageView?) {
            var count=0
            ObjectAnimator.ofFloat(ivGridImage, "translationY", 7f).apply {
                duration = 100 //몇 milli초 동안 animation을 진행할 것이냐
                repeatCount = 2 //몇번 반복하냐
                addListener(object: Animator.AnimatorListener{ //리스너 부분
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) { //최소 몇초후에 다시 리스너가 켜질지
                        count++
                        CoroutineScope(Main).launch{
                            if(count%2==0) {
                                delay(1000) //재시작 시각
                            }else{
                                delay(100) //두번째 애니메이션
                            }
                            start()
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
                start()
            }
        }

    }
}