package com.devapp.fr.data.models.quiz

object QuizConstants {
    fun getQuestion(): ArrayList<QuizData> {
        val questionList = ArrayList<QuizData>()
        val q1 = QuizData(
            "Bạn thường xuyên kết bạn mới",
            QuizData.QuestionType.Mind,
            1
        )
        val q2 = QuizData(
            "Bạn dành nhiều thời gian khám phá những chủ đề linh tinh khiến bạn hứng thú",
            QuizData.QuestionType.Energy,
            2
        )
        val q3 = QuizData(
            "Nhìn người khác khóc khiến bạn muốn khóc theo",
            QuizData.QuestionType.Nature,
            1
        )
        val q4 = QuizData(
            "Bạn thường lên kế hoạch dự phòng cho kế hoạch dự phòng",
            QuizData.QuestionType.Tactics,
            2
        )
        val q5 = QuizData(
            "Bạn luôn giữ bình tình, ngay cả khi chịu nhiều áp lực",
            QuizData.QuestionType.Identity,
            2
        )
        val q6 = QuizData(
            "Ở những sự kiện xã hội, bạn hiếm khi giới thiệu bản thân với người lạ mà chỉ nói chuyện với người quen",
            QuizData.QuestionType.Mind,
            1
        )
        val q7 = QuizData(
            "Bạn thích hoàn thành công việc hiện tại rồi mới bắt đầu cái khác",
            QuizData.QuestionType.Tactics,
            2
        )
        val q8 = QuizData(
            "Bạn là người cảm tính",
            QuizData.QuestionType.Nature,
            1
        )
        val q9 = QuizData(
            "Bạn thích dùng những công cụ sắp xếp công việc như thời gian biểu hay danh sách",
            QuizData.QuestionType.Tactics,
            2
        )
        val q10 = QuizData(
            "Chỉ một sai lầm nhỏ cũng khiến bạn hoài nghi về năng lực và kiến thức của bản thân",
            QuizData.QuestionType.Identity,
            1
        )
        val q11 = QuizData(
            "Bạn thoải mái với việc bắt chuyện với người mà bạn thấy hứng thú",
            QuizData.QuestionType.Mind,
            1
        )
        val q12 = QuizData(
            "Bạn không hứng thú với việc cảm thụ và phân tích các tác phẩm nghệ thuật",
            QuizData.QuestionType.Energy,
            2
        )
        val q13 = QuizData(
            "Bạn có xu hướng nghe theo bộ não thay vì trái tim",
            QuizData.QuestionType.Nature,
            1
        )
        val q14 = QuizData(
            "Bạn thường làm việc tùy hứng vì lên kế hoạch cụ thể cho từng ngày",
            QuizData.QuestionType.Tactics,
            1
        )
        val q15 = QuizData(
            "Bạn hiếm khi lo lắng về việc có gây ấn tượng tốt với những người mình gặp hay không",
            QuizData.QuestionType.Identity,
            1
        )
        val q16 = QuizData(
            "Bạn thích tham gia những hoạt động tập thể",
            QuizData.QuestionType.Mind,
            1
        )
        val q17 = QuizData(
            "Bạn thích những cuốn sách và bộ phim mà cho phép người xem tự suy diễn về cái kết của chúng",
            QuizData.QuestionType.Energy,
            2
        )
        val q18 = QuizData(
            "Bạn thấy hạnh phúc vì giúp người khác đạt được thành công hơn là hạnh phúc vì thành quả của chính mình",
            QuizData.QuestionType.Nature,
            2
        )
        val q19 = QuizData(
            "Bạn thấy hứng thú với quá nhiều thứ nên không biết phải thử làm gì trước",
            QuizData.QuestionType.Tactics,
            1
        )
        val q20 = QuizData(
            "Bạn hay lo lắng rằng mọi chuyện sẽ diễn biến theo chiều hướng xấu",
            QuizData.QuestionType.Identity,
            1
        )
        val q21 = QuizData(
            "Bạn tránh nhận vai trò lãnh đạo trong nhóm",
            QuizData.QuestionType.Mind,
            2
        )
        val q22 = QuizData(
            "Bạn chắc chắn không phải kiểu người nghệ sĩ",
            QuizData.QuestionType.Energy,
            1
        )
        val q23 = QuizData(
            "Bạn cho rằng thế giới sẽ tốt đẹp hơn nhiều nếu mọi người hành xử dựa vào lí tính hay vì cảm tính",
            QuizData.QuestionType.Nature,
            2
        )
        val q24 = QuizData(
            "Bạn luôn hoàn thành việc nhà rồi mới nghỉ ngơi",
            QuizData.QuestionType.Tactics,
            2
        )
        val q25 = QuizData(
            "Bạn thích xem người khác tranh luận",
            QuizData.QuestionType.Energy,
            2
        )
        val q26 = QuizData(
            "Bạn có xu hướng tránh thu hút sự chú ý tới bản thân",
            QuizData.QuestionType.Mind,
            2
        )
        val q27 = QuizData(
            "Bạn có xu hướng tránh thu hút sự chú ý tới bản thân",
            QuizData.QuestionType.Mind,
            2
        )
        val q28 = QuizData(
            "Cảm xúc của bạn có thể thay đổi nhanh chóng",
            QuizData.QuestionType.Identity,
            1
        )
        val q29 = QuizData(
            "Bạn dễ mất kiên nhẫn với những người làm việc không hiệu quả bằng bạn",
            QuizData.QuestionType.Identity,
            1
        )
        val q30 = QuizData(
            "Bạn thường để nước đến chân mới nhảy",
            QuizData.QuestionType.Tactics,
            1
        )
        val q31 = QuizData(
            "Bạn luôn bị hấp dẫn bởi câu hỏi: Liệu chuyện gì xảy ra sau khi chúng ta chết đi?",
            QuizData.QuestionType.Energy,
            2
        )
        val q32 = QuizData(
            "Bạn thích ở cùng với người khác hơn là một mình",
            QuizData.QuestionType.Mind,
            1
        )
        val q33 = QuizData(
            "Bạn thấy chán hay mất hứng khi cuộc tranh luận trở nên nặng nề về mặt lý thuyết",
            QuizData.QuestionType.Mind,
            1
        )
        val q34 = QuizData(
            "Bạn có thể dễ dàng cảm thông với những người có trải nghiệm khác biệt so với mình",
            QuizData.QuestionType.Nature,
            1
        )
        val q35 = QuizData(
            "Bạn thường trì hoãn việc ra quyết định lâu nhất có thể ",
            QuizData.QuestionType.Identity,
            1
        )
        val q36 = QuizData(
            "Bạn hiếm khi hối hận về quyết định của mình",
            QuizData.QuestionType.Identity,
            2
        )
        val q37 = QuizData(
            "Sau một tuần làm việc mệt mỏi, bạn muốn đến một sự kiện xã hội náo nhiệt để giải trí",
            QuizData.QuestionType.Mind,
            1
        )
        val q38 = QuizData(
            "Bạn thích đến xem bảo tàng nghệ thuật",
            QuizData.QuestionType.Energy,
            2
        )
        val q39 = QuizData(
            "Bạn thấy khó lòng mà hiểu được cảm xúc của người khác",
            QuizData.QuestionType.Nature,
            2
        )
        val q40 = QuizData(
            "Bạn thích lên kế hoạch chi tiết mỗi ngày",
            QuizData.QuestionType.Tactics,
            2
        )
        val q41 = QuizData(
            "Bạn thích lên kế hoạch chi tiết mỗi ngày",
            QuizData.QuestionType.Tactics,
            2
        )
        val q42 = QuizData(
            "Bạn hiếm khi cảm thấy bất an",
            QuizData.QuestionType.Identity,
            2
        )
        val q43 = QuizData(
            "Bạn tránh phải gọi điện thoại cho người khác",
            QuizData.QuestionType.Mind,
            2
        )
        val q44 = QuizData(
            "Bạn thường dành nhiều thời gian để cố thấu hiểu quan điểm của người khác",
            QuizData.QuestionType.Nature,
            2
        )
        val q45 = QuizData(
            "Trong cuộc sống, bạn là người hay liên lạc với bạn bè và rủ họ đi chơi",
            QuizData.QuestionType.Mind,
            1
        )
        val q46 = QuizData(
            "Nếu kế hoạch bị ngắt quãng, bạn ưu tiên tiếp tục nó sớm nhất có thể",
            QuizData.QuestionType.Tactics,
            2
        )
        val q47 = QuizData(
            "Bạn vẫn còn thấy áy náy vì những sai lầm phạm phải từ rất lâu về trước",
            QuizData.QuestionType.Identity,
            2
        )
        val q48 = QuizData(
            "Bạn hiếm khi suy ngẫm về những thứ như lý do con người tồn tại hay là ý nghĩa cuộc đời",
            QuizData.QuestionType.Energy,
            1
        )
        val q49 = QuizData(
            "Bạn để cảm xúc điều khiển thay vì điều khiển cảm xúc",
            QuizData.QuestionType.Nature,
            1
        )

        val q50 = QuizData(
            "Bạn làm việc một cách bùng nổ vào những thời điểm tự phát thay vì làm việc một cách ổn định và có kế hoạch",
            QuizData.QuestionType.Tactics,
            1
        )
        val q51 = QuizData(
            "Khi người khác đánh giá cao bạn, bạn tự hỏi rằng liệu người đó có thất vọng về bạn trong tương lai không",
            QuizData.QuestionType.Identity,
            1
        )
        val q52 = QuizData(
            "Bạn thích những công việc mà cho phép bạn làm việc một mình",
            QuizData.QuestionType.Mind,
            2
        )
        val q53 = QuizData(
            "Bạn cho rằng ngẫm nghĩ về những câu hỏi triết học trừu tượng thật là vô bổ",
            QuizData.QuestionType.Energy,
            2
        )
        val q54 = QuizData(
            "Bạn bị thu hút bởi những chốn đông đúc, náo nhiệt hơn là những nơi yên tĩnh, ấm cúng",
            QuizData.QuestionType.Mind,
            1
        )
        val q55 = QuizData(
            "Bạn nhìn cái là biết ngay người khác đang cảm thấy thế nào",
            QuizData.QuestionType.Nature,
            1
        )
        val q56 = QuizData(
            "Bạn thường thấy mình bị quá tải",
            QuizData.QuestionType.Identity,
            1
        )
        val q57 = QuizData(
            "Bạn hoàn thành mọi việc chuẩn chỉnh như sách giáo khoa mà không bỏ qua bước nào",
            QuizData.QuestionType.Tactics,
            2
        )

        val q58 = QuizData(
            "Bạn sẽ nhường lại cơ hội cho người khác nếu bạn nghĩ người đó cần nó hơn mình",
            QuizData.QuestionType.Nature,
            1
        )
        val q59 = QuizData(
            "Bạn khốn khổ vì bị deadline dí liên tục",
            QuizData.QuestionType.Tactics,
            1
        )
        val q60 = QuizData(
            "Bạn tự tin rằng mọi chuyện rồi sẽ ổn thôi",
            QuizData.QuestionType.Identity,
            1
        )
        questionList.add(q1)
        questionList.add(q2)
        questionList.add(q3)
        questionList.add(q4)
        questionList.add(q5)
        questionList.add(q6)
        questionList.add(q7)
        questionList.add(q8)
        questionList.add(q9)
        questionList.add(q10)
        questionList.add(q11)
        questionList.add(q12)
        questionList.add(q13)
        questionList.add(q14)
        questionList.add(q15)
        questionList.add(q16)
        questionList.add(q17)
        questionList.add(q18)
        questionList.add(q19)
        questionList.add(q20)
        questionList.add(q21)
        questionList.add(q22)
        questionList.add(q23)
        questionList.add(q24)
        questionList.add(q25)
        questionList.add(q26)
        questionList.add(q27)
        questionList.add(q28)
        questionList.add(q29)
        questionList.add(q30)
        questionList.add(q31)
        questionList.add(q32)
        questionList.add(q33)
        questionList.add(q34)
        questionList.add(q35)
        questionList.add(q36)
        questionList.add(q37)
        questionList.add(q38)
        questionList.add(q39)
        questionList.add(q40)
        questionList.add(q41)
        questionList.add(q42)
        questionList.add(q43)
        questionList.add(q44)
        questionList.add(q45)
        questionList.add(q46)
        questionList.add(q47)
        questionList.add(q48)
        questionList.add(q49)
        questionList.add(q50)
        questionList.add(q51)
        questionList.add(q52)
        questionList.add(q53)
        questionList.add(q54)
        questionList.add(q55)
        questionList.add(q56)
        questionList.add(q57)
        questionList.add(q58)
        questionList.add(q59)
        questionList.add(q60)


















        return questionList

    }
}