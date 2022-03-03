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
        "category_event","category_fruit", "category_meat", "category_vegetable",
        "category_fish", "category_fastfood", "category_milk",  "category_water",
        "category_seasoning", "category_ramen",  "category_kitchen", "category_tissue",
        "category_chips", "category_rice", "category_chips",  "category_chips",
        "category_chips"
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