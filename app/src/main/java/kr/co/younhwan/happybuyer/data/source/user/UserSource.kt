package kr.co.younhwan.happybuyer.data.source.user

import kr.co.younhwan.happybuyer.data.UserItem

interface UserSource {
    // CREATE
    fun create(kakaoAccountId: Long, kakaoNickname:String?, createCallback: CreateCallback?)

    interface CreateCallback {
        fun onCreate(isSuccess: Boolean)
    }

    // READ
    fun read(kakaoAccountId: Long, readCallback: ReadCallback?)

    interface ReadCallback{
        fun onRead(userItem: UserItem?)
    }

    // UPDATE
    fun update(kakaoAccountId: Long, updateTarget: String, newContent: String, updateCallback: UpdateCallback?)

    interface UpdateCallback {
        fun onUpdate(isSuccess: Boolean)
    }
}