package kr.co.younhwan.happybuyer.view.update.nickname.presenter

import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class NicknamePresenter(
    val view: NicknameContract.View,
    private val userData: UserRepository,
) : NicknameContract.Model {

    override fun updateUserNickname(nicknameToUpdate: String) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                view.updateFailCallback()
            } else if (user != null) {
                val kakaoLoginId = user.id
                userData.updateUser(
                    kakaoLoginId,
                    nicknameToUpdate,
                    object : UserSource.updateUserCallback {
                        override fun onUpdateUser(isSuccess:Boolean) {
                            if(isSuccess){
                                view.updateSuccessCallback(nicknameToUpdate)
                            }else{
                                view.updateFailCallback()
                            }
                        }
                    })
            }
        }
    }
}