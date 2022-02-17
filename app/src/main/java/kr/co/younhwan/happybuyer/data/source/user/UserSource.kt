package kr.co.younhwan.happybuyer.data.source.user

interface UserSource {

    interface createUserCallback {
        fun onCreateUser(isSuccess: Boolean)
    }

    fun createUser(kakaoLoginId: Long?, kakaoNickname:String?, createUserCallback: createUserCallback?)
}