package kr.co.younhwan.happybuyer.data.source.user

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