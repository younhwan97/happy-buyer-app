package kr.co.younhwan.happybuyer.data.source.user

import kr.co.younhwan.happybuyer.data.UserItem

interface UserSource {
    interface CreateCallback {
        fun onCreate(isSuccess: Boolean)
    }

    fun create(kakaoAccountId: Long, kakaoNickname:String?, createCallback: CreateCallback?)

    interface ReadCallback{
        fun onRead(userItem: UserItem?)
    }

    fun read(kakaoAccountId: Long, readCallback: ReadCallback?)

    interface UpdateUserCallback {
        fun onUpdateUser(isSuccess: Boolean)
    }

    fun updateUser(kakaoAccountId: Long, target: String, newContent: String, updateUserCallback: UpdateUserCallback?)

    interface DeleteUserCallback{
        fun onDeleteUser(isSuccess: Boolean)
    }

    fun deleteUser(kakaoAccountId: Long, deleteUserCallback: DeleteUserCallback?)
}