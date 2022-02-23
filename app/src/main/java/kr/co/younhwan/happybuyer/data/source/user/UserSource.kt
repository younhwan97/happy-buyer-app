package kr.co.younhwan.happybuyer.data.source.user

import kr.co.younhwan.happybuyer.data.UserItem

interface UserSource {

    // Create User
    interface CreateUserCallback {
        fun onCreateUser(isSuccess: Boolean)
    }

    fun createUser(kakaoAccountId: Long, kakaoNickname:String?, createUserCallback: CreateUserCallback?)

    // Read user
    interface ReadUserCallback{
        fun onReadUser(userItem: UserItem?)
    }

    fun readUser(kakaoAccountId: Long, readUserCallback: ReadUserCallback?)

    // Update User
    interface UpdateUserCallback {
        fun onUpdateUser(isSuccess: Boolean)
    }

    fun updateUser(kakaoAccountId: Long, target: String, newContent: String, updateUserCallback: UpdateUserCallback?)

    // Delete User
    interface DeleteUserCallback{
        fun onDeleteUser(isSuccess: Boolean)
    }

    fun deleteUser(kakaoAccountId: Long, deleteUserCallback: DeleteUserCallback?)
}