package kr.co.younhwan.happybuyer.view.address

interface AddressContract {
    interface View {

        fun getAct(): AddressActivity

    }

    interface Model {

        fun loadAddress()

    }
}