package com.pavelprymak.propodcast.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_LANGUAGES
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_filter_language.*

internal class LanguageAdapter(private val mClickListener: LanguageClickListener?) :
    RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    private var mLanguages: List<String?>? = null
    private var mContext: Context? = null
    private var mSelectedLanguage = ALL_LANGUAGES

    fun updateList(languages: List<String>, selectedLanguage: String) {
        mLanguages = languages
        mSelectedLanguage = selectedLanguage
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        return LanguageViewHolder(inflater.inflate(R.layout.item_filter_language, parent, false))
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mLanguages?.size ?: 0
    }

    internal inner class LanguageViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(position: Int) {
            mLanguages?.get(position)?.let { language ->
                itemContainer.setOnClickListener {
                    onClick(language)
                }
                //Title
                tvValue.text = language
                if (language == mSelectedLanguage) {
                    ivIsSelected.visibility = View.VISIBLE
                } else {
                    ivIsSelected.visibility = View.GONE
                }
            }
        }

        private fun onClick(language: String) {
            if (mSelectedLanguage != language) {
                val previousPosition = getPositionByLanguageName(mSelectedLanguage)
                mSelectedLanguage = language
                notifyItemChanged(previousPosition)
                notifyItemChanged(adapterPosition)
                mClickListener?.onLanguageItemClick(language)
            }
        }
    }

    fun getPositionByLanguageName(regShortName: String?): Int {
        return mLanguages?.indexOfFirst { it == regShortName } ?: 0
    }
}


