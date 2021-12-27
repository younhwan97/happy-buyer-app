package kr.co.younhwan.happybuyer

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account_login.*
import kr.co.younhwan.happybuyer.Navigation.AccountFragment
import kr.co.younhwan.happybuyer.Navigation.HomeFragment
import kr.co.younhwan.happybuyer.Navigation.AccountLoginFragment
import kr.co.younhwan.happybuyer.Navigation.SearchFragment
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    // 권한 목록
    private val permissionList = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.ACCESS_NETWORK_STATE
    )

    // MainActivity 에서 사용할 프래그먼트
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val accountFragment = AccountFragment()
    private val accountLoginFragment = AccountLoginFragment()

    // 파이어베이스 인증
    var mAuth: FirebaseAuth? = null
    var codeSent: String? = null

    // 캐쉬데이터
    var pref: SharedPreferences? = null
    var account: String? = null

    // 툴바의 search item
    // 추후 추후 프래그먼트 전환 시 강제로 expand 하기 위해서 필요
    var searchItem: MenuItem? = null

    // 어플리케이션이 실행되고 단 1번 호출!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 권한 요청
        requestPermissions(permissionList, 0)

        // 파이어베이스 인증 객체 생성
        mAuth = FirebaseAuth.getInstance()

        // 캐쉬데이터를 가져온다.
        // 회원가입을 하지않은 사용자의 경우 account 캐쉬가 비어있을 것 이다.
        // account 값에 따라 보여줄 프래그먼트를 추후에 분기할 것 이다.
        pref = getSharedPreferences("account", MODE_PRIVATE)
        account = pref?.getString("account", "")

        // 액션바 -> 툴바
        mainToolbar.title = "HappyBuyer/나비마트"
        mainToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleTheme)
        setSupportActionBar(mainToolbar)

        // 바텀 내비게이션의 이벤트 리스너를 설정
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    setFragment("home")
                    true
                }
                R.id.action_search -> {
                    searchItem?.expandActionView()
                    true
                }
                R.id.action_account -> {
                    // 막 회원가입을 한 사용자의 경우 account 값이 비어있을 것이다.
                    // 때문에 다시한번 account 캐쉬 데이터를 얻어온다.
                    account = pref?.getString("account", "")

                    if (account.isNullOrEmpty())
                        setFragment("login") // 로그인 x -> 로그인/회원가입 페이지를 노출한다.
                     else
                        setFragment("account") // 로그인 o -> 각 사용자의 계정 페이지를 노출한다.

                    true
                }
                else -> false
            }
        }

        // 초기 화면의 프래그먼트를 설정한다.
        setFragment("home")
    }

    // -----------------------------------------------------
    // ---- 툴바 설정
    // 툴바의 메뉴 아이템을 생성하고, 이벤트 리스너를 장착한다.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 메뉴 view를 생성
        menuInflater.inflate(R.menu.main_menu, menu)

        // 메뉴의 search view 에 이벤트 리스너 설정을 위해 객체를 얻어온다.
        searchItem = menu?.findItem(R.id.search_item)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        // setting search view
        searchView.queryHint = "검색어를 입력하세요."

        // searchView 가 펼쳐졌을 때 발생하는 이벤트를 처리하는 리스너
        val expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                bottomNavigation.visibility = View.VISIBLE
                bottomNavigation.findViewById<View>(R.id.action_home).performClick()
                return true
            }

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                bottomNavigation.visibility = View.GONE
                setFragment("search")
                return true
            }
        }

        // searchView 에 text 가 입력 됐을 때 발생하는 이벤트를 처리하는 리스너
        val inputListener = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        }

        searchItem?.setOnActionExpandListener(expandListener)
        searchView.setOnQueryTextListener(inputListener)
        return true
    }

    // 툴바가 클릭됐을 때 발생하는 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_item -> {
                // CollapseActionView 설정에 따라 뷰가 확장되고 TextEdit view가 나타날 것 이다.
            }
            R.id.alarm_item -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }
    // 툴바 설정 ----
    // -----------------------------------------------------

    // -----------------------------------------------------
    // ---- 로그인, 로그아웃 및 회원가입
    // 휴대폰 번호를 입력하고 '인증문자 받기' 버튼을 클릭했을 때
    fun sendVerificationCode() {
        var phoneNumber = phoneNumberInput.editText?.text.toString()
        if (phoneNumber.isEmpty()) {
            phoneNumberInput.editText?.setError("Phone number is required")
            phoneNumberInput.editText?.requestFocus()
            return;
        } else {
            phoneNumber = phoneNumber.substring(1)
            phoneNumber = "+82 $phoneNumber"
        }

        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {}
        override fun onVerificationFailed(p0: FirebaseException) {}
        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            codeSent = p0
        }
    }

    // 인증 번호를 입력하고, 인증번호 확인을 클릭했을 때
    fun verifySignInputCode() {
        val userCode = certificationInput.editText?.text.toString() // 사용자가 입력한 코드
        val credential = PhoneAuthProvider.getCredential(codeSent!!, userCode)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // here you can open new activty
                    val phoneNumber = phoneNumberInput.editText?.text.toString()
                    val pref = getSharedPreferences("account", MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putString("account", phoneNumber)
                        .commit()
                    setFragment("account")
                    Toast.makeText(this, "로그인에 성공하셨습니다 :)", Toast.LENGTH_SHORT).show()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Incorrect Verification Code", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        
    }

    fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그아웃")
            .setMessage("정말 로그아웃 하시겠나요?")
            .setNegativeButton("닫기", null)
            .setPositiveButton("로그아웃") { dialogInterface: DialogInterface, i: Int ->
                val pref = getSharedPreferences("account", MODE_PRIVATE)
                val editor = pref.edit()
                editor.remove("account")
                    .commit()
                bottomNavigation.findViewById<View>(R.id.action_home).performClick()
                Toast.makeText(this, "로그아웃에 성공하셨습니다 :)", Toast.LENGTH_SHORT).show()
            }.show()
    }
    // 로그인, 로그아웃 및 회원가입----
    // -----------------------------------------------------

    // set Fragment
    fun setFragment(requestFragment: String) {
        val tran = supportFragmentManager.beginTransaction()

        when (requestFragment) {
            "home" -> {
                mainToolbar.title = "HappyBuyer/나비마트"
                tran.replace(R.id.mainContainer, homeFragment)
            }
            "search" -> {
                mainToolbar.title = "검색"
                tran.replace(R.id.mainContainer, searchFragment)
            }
            "account" -> {
                mainToolbar.title = "내 정보"
                tran.replace(R.id.mainContainer, accountFragment)
            }
            "login" -> {
                mainToolbar.title = "로그인/회원가입"
                tran.replace(R.id.mainContainer, accountLoginFragment)
            }
        }

        tran.commit()
    }
}