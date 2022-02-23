package kr.co.younhwan.happybuyer.data.source.user

import kr.co.younhwan.happybuyer.data.UserItem

interface UserSource {

    interface createUserCallback {
        fun onCreateUser(isSuccess: Boolean)
    }

    interface readUserCallback{
        fun onReadUser(userItem: UserItem?)
    }

    interface updateUserCallback {
        fun onUpdateUser(isSuccess: Boolean)
    }

    fun createUser(kakaoAccountId: Long, kakaoNickname:String?, createUserCallback: createUserCallback?)

    fun readUser(kakaoAccountId: Long, readUserCallback: readUserCallback?)

    fun updateUser(kakaoAccountId: Long, target: String, newContent: String, updateUserCallback: updateUserCallback?)
}