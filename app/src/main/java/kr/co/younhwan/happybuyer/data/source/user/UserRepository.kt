package kr.co.younhwan.happybuyer.data.source.user

import kr.co.younhwan.happybuyer.data.UserItem

object UserRepository : UserSource {

    private val userRemoteDataSource = UserRemoteDataSource

    override fun create(
        kakaoAccountId: Long,
        kakaoNickname: String?,
        createCallback: UserSource.CreateCallback?
    ) {
        userRemoteDataSource.create(
            kakaoAccountId,
            kakaoNickname,
            object : UserSource.CreateCallback {
                override fun onCreate(isSuccess: Boolean) {
                    createCallback?.onCreate(isSuccess)
                }
            })
    }

    override fun read(
        kakaoAccountId: Long,
        readCallback: UserSource.ReadCallback?
    ) {
        userRemoteDataSource.read(kakaoAccountId, object : UserSource.ReadCallback {
            override fun onRead(userItem: UserItem?) {
                readCallback?.onRead(userItem)
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