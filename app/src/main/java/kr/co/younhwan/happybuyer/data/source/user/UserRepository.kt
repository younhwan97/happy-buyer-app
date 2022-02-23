package kr.co.younhwan.happybuyer.data.source.user

import kr.co.younhwan.happybuyer.data.UserItem

object UserRepository : UserSource {

    private val userRemoteDataSource = UserRemoteDataSource

    override fun createUser(
        kakaoAccountId: Long,
        kakaoNickname: String?,
        createUserCallback: UserSource.createUserCallback?
    ) {
        userRemoteDataSource.createUser(
            kakaoAccountId,
            kakaoNickname,
            object : UserSource.createUserCallback {
                override fun onCreateUser(isSuccess: Boolean) {
                    createUserCallback?.onCreateUser(isSuccess)
                }
            })
    }

    override fun updateUser(
        kakaoAccountId: Long,
        target: String,
        newContent: String,
        updateUserCallback: UserSource.updateUserCallback?
    ) {
        userRemoteDataSource.updateUser(
            kakaoAccountId,
            target,
            newContent,
            object : UserSource.updateUserCallback {
                override fun onUpdateUser(isSuccess: Boolean) {
                    updateUserCallback?.onUpdateUser(isSuccess)
                }
            })
    }

    override fun readUser(
        kakaoAccountId: Long,
        readUserCallback: UserSource.readUserCallback?
    ) {
        userRemoteDataSource.readUser(kakaoAccountId, object :UserSource.readUserCallback{
            override fun onReadUser(userItem: UserItem?) {
                readUserCallback?.onReadUser(userItem)
            }
        })
    }
}