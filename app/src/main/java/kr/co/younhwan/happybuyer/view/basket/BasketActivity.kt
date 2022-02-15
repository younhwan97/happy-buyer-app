package kr.co.younhwan.happybuyer.view.basket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.younhwan.happybuyer.databinding.ActivityBasketBinding

class BasketActivity : AppCompatActivity() {
    lateinit var basketActivityBinding: ActivityBasketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding 객체 생성
        basketActivityBinding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(basketActivityBinding.root)


    }
}