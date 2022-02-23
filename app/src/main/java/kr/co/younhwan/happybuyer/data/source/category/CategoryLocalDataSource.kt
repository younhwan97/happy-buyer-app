package kr.co.younhwan.happybuyer.data.source.category

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem

object CategoryLocalDataSource : CategorySource {
    private val labelList = arrayListOf<String>(
        "과일", "정육", "채소",
        "우유/유제품", "김치/반찬", "수산/건해산",
        "생수/음료", "커피/차", "과자/빙과",
        "냉장/냉동식품", "라면/즉석식품", "장/양념"

    )

//    "쌀/잡곡", "세탁/청소", "제지/위생",
//    "주방", "반려동물"

    private val nameList = arrayListOf<String>(
        "category_fruit", "category_meat", "category_vegetable",
        "category_milk", "category_kimchi", "category_fish",
        "category_water", "category_coffee", "category_chips",
        "category_frozen", "category_ramen", "category_seasoning"
    )


//    "category_rice", "category_cleaning", "category_tissue",
//    "category_kitchen", "category_pet"

    override fun getCategories(context: Context, loadImageCallback: CategorySource.LoadCategoryCallback?) {
        val list = ArrayList<CategoryItem>()
        for (index in 1..nameList.size) {
            val resource = context.resources.getIdentifier(nameList[index-1], "drawable", context.packageName)
            list.add(CategoryItem(resource, labelList[index-1]))
        }
        loadImageCallback?.onLoadCategories(list)
    }
}