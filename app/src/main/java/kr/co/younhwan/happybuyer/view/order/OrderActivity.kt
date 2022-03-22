package kr.co.younhwan.happybuyer.view.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.younhwan.happybuyer.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    lateinit var viewDataBinding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)


    }
}