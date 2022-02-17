package kr.co.younhwan.happybuyer.view.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivityUpdateBinding
import kr.co.younhwan.happybuyer.util.replace
import kr.co.younhwan.happybuyer.view.update.nickname.NicknameFragment
import kr.co.younhwan.happybuyer.view.update.phone.PhoneFragment
import kr.co.younhwan.happybuyer.view.update.point.PointFragment

class UpdateActivity : AppCompatActivity() {
    /* View Binding */
    lateinit var viewDataBinding : ActivityUpdateBinding

    /* UpdateActivity 에서 사용할 프래그먼트 */
    private val nicknameFragment : NicknameFragment by lazy {
        NicknameFragment()
    }

    private val phoneFragment : PhoneFragment by lazy {
        PhoneFragment()
    }

    private val pointFragment : PointFragment by lazy {
        PointFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 액션바 -> 툴바
        setSupportActionBar(viewDataBinding.updateToolbar)
        supportActionBar?.run {
            title = ""
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val target = intent.getStringExtra("target")

        if(target == "pointNumber"){
            replace(R.id.fragmentContainerInUpdateAct, pointFragment)
        } else if(target == "phoneNumber"){
            replace(R.id.fragmentContainerInUpdateAct, phoneFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}