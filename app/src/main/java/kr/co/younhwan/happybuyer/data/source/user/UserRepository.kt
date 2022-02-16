package kr.co.younhwan.happybuyer.data.source.user

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import kr.co.younhwan.happybuyer.data.source.category.CategoryLocalDataSource

object UserRepository : UserSource {

    private val userRemoteDataSource = UserRemoteDataSource

    override fun createUser(kakaoLoginId:Long?, kakaoNickname:String?, createUserCallback: UserSource.createUserCallback?) {
        userRemoteDataSource.createUser(kakaoLoginId, kakaoNickname, object : UserSource.createUserCallback{
            override fun onCreateUser(isSuccess: Boolean) {
                createUserCallback?.onCreateUser(isSuccess)
            }
        })
    }
}