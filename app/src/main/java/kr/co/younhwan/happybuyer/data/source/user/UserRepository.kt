package kr.co.younhwan.happybuyer.data.source.user

import kr.co.younhwan.happybuyer.data.UserItem

object UserRepository : UserSource {

    private val userRemoteDataSource = UserRemoteDataSource

    override fun createUser(
        kakaoAccountId: Long,
        kakaoNickname: String?,
        createUserCallback: UserSource.CreateUserCallback?
    ) {
        userRemoteDataSource.createUser(
            kakaoAccountId,
            kakaoNickname,
            object : UserSource.CreateUserCallback {
                override fun onCreateUser(isSuccess: Boolean) {
                    createUserCallback?.onCreateUser(isSuccess)
                }
            })
    }

    override fun readUser(
        kakaoAccountId: Long,
        readUserCallback: UserSource.ReadUserCallback?
    ) {
        userRemoteDataSource.readUser(kakaoAccountId, object : UserSource.ReadUserCallback {
            override fun onReadUser(userItem: UserItem?) {
                readUserCallback?.onReadUser(userItem)
            }
        })
    }

    override fun updateUser(
        kakaoAccountId: Long,
        target: String,
        newContent: String,
        updateUserCallback: UserSource.UpdateUserCallback?
    ) {
        userRemoteDataSource.updateUser(
            kakaoAccountId,
            target,
            newContent,
            object : UserSource.UpdateUserCallback {
                override fun onUpdateUser(isSuccess: Boolean) {
                    updateUserCallback?.onUpdateUser(isSuccess)
                }
            })
    }

    override fun deleteUser(
        kakaoAccountId: Long,
        deleteUserCallback: UserSource.DeleteUserCallback?
    ) {
        userRemoteDataSource.deleteUser(kakaoAccountId, object : UserSource.DeleteUserCallback {
            override fun onDeleteUser(isSuccess: Boolean) {
                deleteUserCallback?.onDeleteUser(isSuccess)
            }
        })
    }
}