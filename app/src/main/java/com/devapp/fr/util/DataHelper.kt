package com.devapp.fr.util

import com.devapp.fr.R
import com.devapp.fr.data.models.items.InformationItem
import com.devapp.fr.data.models.items.RadioItem

internal object DataHelper {
     internal fun getListChild() = listOf(
        RadioItem("Có vào một ngày nào đó"),
        RadioItem("Tôi sắp có"),
        RadioItem("Tôi không muốn có em bé"),
        RadioItem("Tôi đã có"),
        RadioItem("Tôi không muốn nói"),
    )

    internal fun getListDrink() = listOf(
        RadioItem("Có"),
        RadioItem("Không"),
        RadioItem("Thỉnh thoảng"),
        RadioItem("Tôi không muốn nói"),
        )

    internal fun getListMaritalStatus() = listOf(
        RadioItem("Độc thân"),
        RadioItem("Đang hẹn hò"),
        RadioItem("Phức tạp"),
        RadioItem("Đang mở lòng"),
        RadioItem("Tôi không muốn nói"),
    )

    internal fun getListGender() = listOf(
        RadioItem("Giới tính thẳng"),
        RadioItem("Gay"),
        RadioItem("Đồng tính nữ"),
        RadioItem("Lưỡng tính"),
        RadioItem("Vô tính"),
        RadioItem("Á tính"),
        RadioItem("Toàn tính luyến ái"),
        RadioItem("Queer"),
        RadioItem("Đang tự hỏi"),
        RadioItem("Tôi không muốn nói")
    )

    internal fun getListSmoke() = listOf(
        RadioItem("Có"),
        RadioItem("Không"),
        RadioItem("Thỉnh thoảng"),
        RadioItem("Tôi không muốn nói"),
    )

    internal fun getListPet() = listOf(
        RadioItem("Mèo"),
        RadioItem("Chó"),
        RadioItem("Cả mèo và chó"),
        RadioItem("Động vật khác"),
        RadioItem("Không thú cưng"),
        RadioItem("Tôi không muốn nói"),
        )

    internal fun getListReligion() = listOf(
        RadioItem("Bất khả tri"),
        RadioItem("Vô thần"),
        RadioItem("Đạo phật"),
        RadioItem("Công giáo"),
        RadioItem("Cơ đốc giáo"),
        RadioItem("Hindu"),
        RadioItem("Do thái"),
        RadioItem("Mặc Môn"),
        RadioItem("Hồi giáo"),
        RadioItem("Hỏa giáo"),
        RadioItem("Sikh"),
        RadioItem("Tâm linh"),
        RadioItem("Khác"),
        RadioItem("Tôi không muốn nói"),
    )

    internal fun getListCertificate() = listOf(
        RadioItem("Trường trung học"),
        RadioItem("Thạc sĩ hoặc cao hơn"),
        RadioItem("Trong trường đại học"),
        RadioItem("Trong trường cao đẳng"),
        RadioItem("Bằng đại học"),
        RadioItem("Tôi không muốn nói"),
    )

    internal fun getListItemInformation() = listOf(
        InformationItem(R.drawable.ic_tall,"Cao",""),
        InformationItem(R.drawable.ic_kid,"Trẻ con",""),
        InformationItem(R.drawable.ic_beer,"Rượu bia",""),
        InformationItem(R.drawable.ic_status_marry,"Về hôn nhân",""),
        InformationItem(R.drawable.ic_gender,"Giới tính",""),
        InformationItem(R.drawable.ic_smoking,"Hút thuốc",""),
        InformationItem(R.drawable.ic_pet,"Thú cưng",""),
        InformationItem(R.drawable.ic_religion,"Tôn giáo",""),
        InformationItem(R.drawable.ic_certificate,"Học vấn",""),
        InformationItem(R.drawable.ic_personality,"Tính cách",""),
    )

    internal fun getListSexuality() = listOf(
        RadioItem("Chat và gặp gỡ người mới"),
        RadioItem("Hẹn hò"),
        RadioItem("Hẹn hò nghiêm túc"),
        RadioItem("Xem điều gì xảy ra"),
        RadioItem("Tìm mối quan hệ lâu dài"),
        RadioItem("Tôi không muốn nói"),
    )
    internal fun getListPersonality() = listOf(
        "Architect",
        "Logician",
        "Commander",
        "Debater",
        "Advocate",
        "Mediator",
        "Protagonist",
        "Campaigner",
        "Logistician",
        "Defender",
        "Executive",
        "Consul",
        "Virtuoso",
        "Adventurer",
        "Entrepreneur",
        "Entertainer",
    )

}