package kr.co.younhwan.happybuyer.data.source.category

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem

object CategoryLocalDataSource : CategorySource {

    /* 메인 화면에 보여질 핵심 카테고리 리스트 */
    private val labelList = arrayListOf<String>(
        "행사", "과일", "정육", "채소",
        "수산/건해산", "쌀/잡곡", "우유/유제품", "생수/음료/커피/차",
        "장/양념", "라면/즉석식품",  "세탁/청소/주방", "제지/위생",
        "과자/빙과", "냉장/냉동식품", "반려동물", "김치/반찬",
        "기타"
    )

    private val imageNameList = arrayListOf<String>(
        "category_event","category_fruit", "category_meat", "category_vegetable",
        "category_fish", "category_rice", "category_milk",  "category_water",
        "category_seasoning", "category_ramen",  "category_cleaning", "category_tissue",
        "category_chips", "category_frozen", "category_pet",  "category_kimchi",
        "category_etc"
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