package com.example.delevryproject.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.delevryproject.BaseApplication
import com.example.delevryproject.model.CellType
import com.example.delevryproject.model.Model
import com.example.delevryproject.ui.base.BaseViewModel
import com.example.delevryproject.util.mapper.ModelViewHolderMapper
import com.example.delevryproject.util.provider.DefaultResourcesProvider
import com.example.delevryproject.util.provider.ResourcesProvider
import com.example.delevryproject.widget.adapter.listener.AdapterListener
import com.example.delevryproject.widget.adapter.viewholder.ModelViewHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class ModelRecyclerAdapter<M : Model, VM: BaseViewModel> @Inject constructor(
    private var modelList: List<Model>,
    private var viewModel: VM,
    private val resourcesProvider: ResourcesProvider = DefaultResourcesProvider(BaseApplication.appContext!!), //모듈에 넣어줘야 함
    private val adapterListener: AdapterListener
) : ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK) {

    override fun getItemCount(): Int = modelList.size

    //ordinal : enum 클래스에서 해당하는 값을 불러오기 위한 인덱스로써 반환
    override fun getItemViewType(position: Int) = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
        return ModelViewHolderMapper.map(parent, CellType.values()[viewType], viewModel, resourcesProvider)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder) {
            bindData(modelList[position] as M)
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    //let : null이 아닐경우에 코드를 실행햐아 함
    override fun submitList(list: List<Model>?) {
        list?.let { modelList = it }
        super.submitList(list)
    }
}
