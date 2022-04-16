package kr.co.younhwan.happybuyer.view.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivityUpdateBinding
import kr.co.younhwan.happybuyer.util.replace
import kr.co.younhwan.happybuyer.view.update.nickname.NicknameFragment
import kr.co.younhwan.happybuyer.view.update.point.PointFragment

class UpdateActivity : AppCompatActivity() {
    lateinit var viewDataBinding: ActivityUpdateBinding

    private val nicknameFragment: NicknameFragment by lazy {
        NicknameFragment()
    }

    private val pointFragment: PointFragment by lazy {
        PointFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 인텐트에서 데이터 추출
        val updateTarget = intent.getStringExtra("update_target") ?: ""

        if (updateTarget == "") {
            finish()
        } else {
            // 툴바
            viewDataBinding.updateToolbar.setNavigationOnClickListener {
                finish()
            }

            // 초기 프래그먼트 설정
            if (updateTarget == "point") {
                viewDataBinding.updateToolbar.title = "포인트 번호 변경"
                replace(R.id.fragmentContainerInUpdateAct, pointFragment)
            } else {
                viewDataBinding.updateToolbar.title = "닉네임 변경"
                replace(R.id.fragmentContainerInUpdateAct, nicknameFragment)
            }
        }
    }
}