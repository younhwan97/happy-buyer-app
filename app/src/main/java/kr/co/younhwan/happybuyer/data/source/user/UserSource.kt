package kr.co.younhwan.happybuyer.data.source.user

import kr.co.younhwan.happybuyer.data.UserItem

interface UserSource {
    fun create(kakaoAccountId: Long, kakaoNickname:String?, createCallback: CreateCallback?)

    interface CreateCallback {
        fun onCreate(isSuccess: Boolean)
    }

    fun read(kakaoAccountId: Long, readCallback: ReadCallback?)

    interface ReadCallback{
        fun onRead(userItem: UserItem?)
    }

    fun update(kakaoAccountId: Long, updateTarget: String, newContent: String, updateCallback: UpdateCallback?)

    interface UpdateCallback {
        fun onUpdate(isSuccess: Boolean)
    }

    fun deleteUser(kakaoAccountId: Long, deleteUserCallback: DeleteUserCallback?)

    interface DeleteUserCallback{
        fun onDeleteUser(isSuccess: Boolean)
    }
}