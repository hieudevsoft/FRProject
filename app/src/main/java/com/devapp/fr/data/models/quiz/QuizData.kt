package com.devapp.fr.data.models.quiz

data class QuizData(
    val id : Int,
    val question : String = "",
    val questionType: QuestionType,
    val TypeReference : Int
)

{
    enum class  QuestionType {
        Mind,
        Energy,
        Nature,
        Tactics,
        Identity
    }
}
