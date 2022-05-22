package kr.co.younhwan.happybuyer.data.source.category

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem

object CategoryLocalDataSource : CategorySource {

    /* 메인 화면에 보여질 핵심 카테고리 리스트 */
    private val labelList = arrayListOf<String>(
        "행사", "과일", "정육/계란", "채소",
        "수산/건해산", "냉장/냉동/간편식", "우유/유제품", "생수/음료/커피",
        "소스/양념/장", "면/통조림",  "세탁/청소/주방", "제지/위생",
        "과자/빙과", "쌀/잡곡", "반려동물", "김치/반찬",
        "기타"
    )

    private val imageNameList = arrayListOf<String>(
        "ic_category_sale","ic_category_fruit", "ic_category_meat", "ic_category_vegetable",
        "ic_category_fish", "ic_category_fastfood", "ic_category_milk",  "ic_category_water",
        "ic_category_sauce", "ic_category_ramen",  "ic_category_kitchen", "ic_category_tissue",
        "ic_category_chips", "ic_category_rice", "ic_category_pet",  "ic_category_kimchi",
        "ic_category_etc"
    )

    override fun readCategories(context: Context, loadImageCallback: CategorySource.ReadCategoryCallback?) {
        val list = ArrayList<CategoryItem>()
        for (index in 1..imageNameList.size) {
            val resource = context.resources.getIdentifier(imageNameList[index-1], "drawable", context.packageName)
            list.add(CategoryItem(resource, labelList[index-1]))
        }
        loadImageCallback?.onReadCategories(list)
    }
}