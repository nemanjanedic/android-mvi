package com.memtrip.pinyin.app.list.phonetic

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.pinyin.AdapterClick
import com.memtrip.pinyin.AdapterEvent
import com.memtrip.pinyin.R
import com.memtrip.pinyin.api.PinyinEntity
import com.memtrip.pinyin.kit.SimpleAdapter
import com.memtrip.pinyin.kit.SimpleAdapterViewHolder
import com.memtrip.pinyin.kit.gone
import com.memtrip.pinyin.kit.visible
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class PinyinPhoneticAdapter(
        context: Context,
        interaction: Consumer<AdapterEvent<PinyinEntity>>) : SimpleAdapter<PinyinEntity>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<PinyinEntity> {
        val viewHolder = PinyinPhoneticViewHolder(inflater.inflate(
                R.layout.pinyin_phonetic_list_item, parent, false))

        RxView.clicks(viewHolder.audioButton).flatMap({
            ObservableSource<AdapterEvent<PinyinEntity>> {
                val position = viewHolder.getAdapterPosition()
                it.onNext(AdapterClick(viewHolder.audioButton.getId(), data[position]))
            }
        }).subscribe(interaction)

        return viewHolder
    }
}

class PinyinPhoneticViewHolder(itemView: View) : SimpleAdapterViewHolder<PinyinEntity>(itemView) {

    @BindView(R.id.pinyin_phonetic_list_item_value)
    lateinit var phoneticTextView: TextView

    @BindView(R.id.pinyin_phonetic_list_audio_button)
    lateinit var audioButton: ImageButton

    init {
        ButterKnife.bind(this, itemView)
    }

    override fun populate(position: Int, value: PinyinEntity) {
        phoneticTextView.text = value.phoneticScriptText
        value.audioSrc?.let { audioButton.visible() } ?: audioButton.gone()
    }
}