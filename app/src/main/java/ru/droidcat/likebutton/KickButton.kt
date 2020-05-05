package ru.droidcat.likebutton

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.like.LikeButton
import com.like.OnAnimationEndListener
import com.like.OnLikeListener

@BindingMethods(value = [
    BindingMethod(
            type = KickButton::class,
            attribute = "app:onToggle",
            method = "setOnToggleListener")])
class KickButton constructor(
        context: Context, attrs: AttributeSet? = null
): LikeButton(context, attrs, 0), OnLikeListener, OnAnimationEndListener {

    private var mToggleListener: OnToggleListener? = null

    interface OnToggleListener {
        fun onToggle()
    }

    fun setOnToggleListener(listener: OnToggleListener) {
        this.mToggleListener = listener
    }

    init {
        this.setOnAnimationEndListener(this)
        this.setOnLikeListener(this)
    }

    override fun liked(likeButton: LikeButton?) {
        Log.d("Like button", "liked")
    }

    override fun unLiked(likeButton: LikeButton?) {
        mToggleListener?.onToggle()
    }

    override fun onAnimationEnd(likeButton: LikeButton?) {
        mToggleListener?.onToggle()
    }

}