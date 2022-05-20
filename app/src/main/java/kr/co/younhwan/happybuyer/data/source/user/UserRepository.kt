package kr.co.younhwan.happybuyer.data.source.user

import kr.co.younhwan.happybuyer.data.UserItem

object UserRepository : UserSource {

    private val userRemoteDataSource = UserRemoteDataSource

    // CREATE
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

    // READ
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

    // UPDATE
    override fun update(
        kakaoAccountId: Long,
        updateTarget: String,
        newContent: String,
        updateCallback: UserSource.UpdateCallback?
    ) {
        userRemoteDataSource.update(
            kakaoAccountId,
            updateTarget,
            newContent,
            object : UserSource.UpdateCallback {
                override fun onUpdate(isSuccess: Boolean) {
                    updateCallback?.onUpdate(isSuccess)
                }
            })
    }
}