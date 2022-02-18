package com.devapp.fr.data.models.quiz

object QuizConstants {
    fun getQuestion(): ArrayList<QuizData> {
        val questionList = ArrayList<QuizData>()
        val q1 = QuizData(
            1,
            "Bạn thường xuyên kết bạn mới",
            QuizData.QuestionType.Mind,
            0
        )
        val q2 = QuizData(
            2,
            "Bạn dành nhiều thời gian khám phá những chủ đề linh tinh khiến bạn hứng thú",
            QuizData.QuestionType.Energy,
            1
        )
        val q3 = QuizData(
            3,
            "Nhìn người khác khóc khiến bạn muốn khóc theo",
            QuizData.QuestionType.Nature,
            0
        )
        val q4 = QuizData(
            4,
            "Bạn thường lên kế hoạch dự phòng cho kế hoạch dự phòng",
            QuizData.QuestionType.Tactics,
            1
        )
        val q5 = QuizData(
            5,
            "Bạn luôn giữ bình tình, ngay cả khi chịu nhiều áp lực",
            QuizData.QuestionType.Identity,
            1
        )
        val q6 = QuizData(
            6,
            "Ở những sự kiện xã hội, bạn hiếm khi giới thiệu bản thân với người lạ mà chỉ nói chuyện với người quen",
            QuizData.QuestionType.Mind,
            1
        )
        val q7 = QuizData(
            7,
            "Bạn thích hoàn thành công việc hiện tại rồi mới bắt đầu cái khác",
            QuizData.QuestionType.Tactics,
            1
        )
        val q8 = QuizData(
            8,
            "Bạn là người cảm tính",
            QuizData.QuestionType.Nature,
            0
        )
        val q9 = QuizData(
            9,
            "Bạn thích dùng những công cụ sắp xếp công việc như thời gian biểu hay danh sách",
            QuizData.QuestionType.Tactics,
            1
        )
        val q10 = QuizData(
            10,
            "Chỉ một sai lầm nhỏ cũng khiến bạn hoài nghi về năng lực và kiến thức của bản thân",
            QuizData.QuestionType.Identity,
            0
        )
        val q11 = QuizData(
            11,
            "Bạn thoải mái với việc bắt chuyện với người mà bạn thấy hứng thú",
            QuizData.QuestionType.Mind,
            0
        )
        val q12 = QuizData(
            12,
            "Bạn không hứng thú với việc cảm thụ và phân tích các tác phẩm nghệ thuật",
            QuizData.QuestionType.Energy,
            0
        )
        val q13 = QuizData(
            13,
            "Bạn có xu hướng nghe theo bộ não thay vì trái tim",
            QuizData.QuestionType.Nature,
            1
        )
        val q14 = QuizData(
            14,
            "Bạn thường làm việc tùy hứng vì lên kế hoạch cụ thể cho từng ngày",
            QuizData.QuestionType.Tactics,
            0
        )
        val q15 = QuizData(
            15,
            "Bạn hiếm khi lo lắng về việc có gây ấn tượng tốt với những người mình gặp hay không",
            QuizData.QuestionType.Identity,
            1
        )
        val q16 = QuizData(
            16,
            "Bạn thích tham gia những hoạt động tập thể",
            QuizData.QuestionType.Mind,
            0
        )
        val q17 = QuizData(
            17,
            "Bạn thích những cuốn sách và bộ phim mà cho phép người xem tự suy diễn về cái kết của chúng",
            QuizData.QuestionType.Energy,
            1
        )
        val q18 = QuizData(
            18,
            "Bạn thấy hạnh phúc vì giúp người khác đạt được thành công hơn là hạnh phúc vì thành quả của chính mình",
            QuizData.QuestionType.Nature,
            0
        )
        val q19 = QuizData(
            19,
            "Bạn thấy hứng thú với quá nhiều thứ nên không biết phải thử làm gì trước",
            QuizData.QuestionType.Tactics,
            0
        )
        val q20 = QuizData(
            20,
            "Bạn hay lo lắng rằng mọi chuyện sẽ diễn biến theo chiều hướng xấu",
            QuizData.QuestionType.Identity,
            0
        )
        val q21 = QuizData(
            21,
            "Bạn tránh nhận vai trò lãnh đạo trong nhóm",
            QuizData.QuestionType.Mind,
            1
        )
        val q22 = QuizData(
            22,
            "Bạn chắc chắn không phải kiểu người nghệ sĩ",
            QuizData.QuestionType.Energy,
            0
        )
        val q23 = QuizData(
            23,
            "Bạn cho rằng thế giới sẽ tốt đẹp hơn nhiều nếu mọi người hành xử dựa vào lí tính hay vì cảm tính",
            QuizData.QuestionType.Nature,
            1
        )
        val q24 = QuizData(
            24,
            "Bạn luôn hoàn thành việc nhà rồi mới nghỉ ngơi",
            QuizData.QuestionType.Tactics,
            1
        )
        val q25 = QuizData(
            25,
            "Bạn thích xem người khác tranh luận",
            QuizData.QuestionType.Energy,
            1
        )
        val q26 = QuizData(
            26,
            "Bạn có xu hướng tránh thu hút sự chú ý tới bản thân",
            QuizData.QuestionType.Mind,
            1
        )
        val q27 = QuizData(
            27,
            "Bạn là người sống tình cảm",
            QuizData.QuestionType.Nature,
            0
        )
        val q28 = QuizData(
            28,
            "Cảm xúc của bạn có thể thay đổi nhanh chóng",
            QuizData.QuestionType.Identity,
            0
        )
        val q29 = QuizData(
            29,
            "Bạn dễ mất kiên nhẫn với những người làm việc không hiệu quả bằng bạn",
            QuizData.QuestionType.Identity,
            0
        )
        val q30 = QuizData(
            30,
            "Bạn thường để nước đến chân mới nhảy",
            QuizData.QuestionType.Tactics,
            0
        )
        val q31 = QuizData(
            31,
            "Bạn luôn bị hấp dẫn bởi câu hỏi: Liệu chuyện gì xảy ra sau khi chúng ta chết đi?",
            QuizData.QuestionType.Energy,
            1
        )
        val q32 = QuizData(
            32,
            "Bạn thích ở cùng với người khác hơn là một mình",
            QuizData.QuestionType.Mind,
            0
        )
        val q33 = QuizData(
            33,
            "Bạn thấy chán hay mất hứng khi cuộc tranh luận trở nên nặng nề về mặt lý thuyết",
            QuizData.QuestionType.Mind,
            0
        )
        val q34 = QuizData(
            34,
            "Bạn có thể dễ dàng cảm thông với những người có trải nghiệm khác biệt so với mình",
            QuizData.QuestionType.Nature,
            0
        )
        val q35 = QuizData(
            35,
            "Bạn thường trì hoãn việc ra quyết định lâu nhất có thể ",
            QuizData.QuestionType.Identity,
            0
        )
        val q36 = QuizData(
            36,
            "Bạn hiếm khi hối hận về quyết định của mình",
            QuizData.QuestionType.Identity,
            1
        )
        val q37 = QuizData(
            37,
            "Sau một tuần làm việc mệt mỏi, bạn muốn đến một sự kiện xã hội náo nhiệt để giải trí",
            QuizData.QuestionType.Mind,
            0
        )
        val q38 = QuizData(
            38,
            "Bạn thích đến xem bảo tàng nghệ thuật",
            QuizData.QuestionType.Energy,
            1
        )
        val q39 = QuizData(
            39,
            "Bạn thấy khó lòng mà hiểu được cảm xúc của người khác",
            QuizData.QuestionType.Nature,
            1
        )
        val q40 = QuizData(
            40,
            "Bạn thích lên kế hoạch công việc mỗi ngày",
            QuizData.QuestionType.Tactics,
            1
        )
        val q41 = QuizData(
            41,
            "Bạn tránh khiến cho người khác trông xấu xa, dù người đó có lỗi",
            QuizData.QuestionType.Nature,
            0
        )
        val q42 = QuizData(
            42,
            "Bạn hiếm khi cảm thấy bất an",
            QuizData.QuestionType.Identity,
            1
        )
        val q43 = QuizData(
            43,
            "Bạn tránh phải gọi điện thoại cho người khác",
            QuizData.QuestionType.Mind,
            1
        )
        val q44 = QuizData(
            44,
            "Bạn thường dành nhiều thời gian để cố thấu hiểu quan điểm của người khác",
            QuizData.QuestionType.Energy,
            1
        )
        val q45 = QuizData(
            45,
            "Trong cuộc sống, bạn là người hay liên lạc với bạn bè và rủ họ đi chơi",
            QuizData.QuestionType.Mind,
            0
        )
        val q46 = QuizData(
            46,
            "Nếu kế hoạch bị ngắt quãng, bạn ưu tiên tiếp tục nó sớm nhất có thể",
            QuizData.QuestionType.Tactics,
            1
        )
        val q47 = QuizData(
            47,
            "Bạn vẫn còn thấy áy náy vì những sai lầm phạm phải từ rất lâu về trước",
            QuizData.QuestionType.Identity,
            0
        )
        val q48 = QuizData(
            48,
            "Bạn hiếm khi suy ngẫm về những thứ như lý do con người tồn tại hay là ý nghĩa cuộc đời",
            QuizData.QuestionType.Energy,
            0
        )
        val q49 = QuizData(
            49,
            "Bạn để cảm xúc điều khiển thay vì điều khiển cảm xúc",
            QuizData.QuestionType.Nature,
            0
        )

        val q50 = QuizData(
            50,
            "Bạn làm việc một cách bùng nổ vào những thời điểm tự phát thay vì làm việc một cách ổn định và có kế hoạch",
            QuizData.QuestionType.Tactics,
            0
        )
        val q51 = QuizData(
            51,
            "Khi người khác đánh giá cao bạn, bạn tự hỏi rằng liệu người đó có thất vọng về bạn trong tương lai không",
            QuizData.QuestionType.Identity,
            0
        )
        val q52 = QuizData(
            52,
            "Bạn thích những công việc mà cho phép bạn làm việc một mình",
            QuizData.QuestionType.Mind,
            1
        )
        val q53 = QuizData(
            53,
            "Bạn cho rằng ngẫm nghĩ về những câu hỏi triết học trừu tượng thật là vô bổ",
            QuizData.QuestionType.Energy,
            0
        )
        val q54 = QuizData(
            54,
            "Bạn bị thu hút bởi những chốn đông đúc, náo nhiệt hơn là những nơi yên tĩnh, ấm cúng",
            QuizData.QuestionType.Mind,
            0
        )
        val q55 = QuizData(
            55,
            "Bạn nhìn cái là biết ngay người khác đang cảm thấy thế nào",
            QuizData.QuestionType.Nature,
            0
        )
        val q56 = QuizData(
            56,
            "Bạn thường thấy mình bị quá tải",
            QuizData.QuestionType.Identity,
            0
        )
        val q57 = QuizData(
            57,
            "Bạn hoàn thành mọi việc chuẩn chỉnh như sách giáo khoa mà không bỏ qua bước nào",
            QuizData.QuestionType.Tactics,
            1
        )

        val q58 = QuizData(
            58,
            "Bạn sẽ nhường lại cơ hội cho người khác nếu bạn nghĩ người đó cần nó hơn mình",
            QuizData.QuestionType.Nature,
            0
        )
        val q59 = QuizData(
            59,
            "Bạn khốn khổ vì bị deadline dí liên tục",
            QuizData.QuestionType.Tactics,
            0
        )
        val q60 = QuizData(
            60,
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