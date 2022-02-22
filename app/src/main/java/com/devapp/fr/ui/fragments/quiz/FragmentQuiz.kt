package com.devapp.fr.ui.fragments.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.quiz.QuizConstants
import com.devapp.fr.data.models.quiz.QuizData
import com.devapp.fr.databinding.FragmentQuizBinding
import com.devapp.fr.databinding.FragmentQuizResultBinding
import com.devapp.fr.util.UiHelper.showSnackbar
import com.google.firebase.firestore.util.Assert
import kotlinx.coroutines.NonDisposableHandle.parent
import java.lang.AssertionError


class FragmentQuiz : BaseFragment<FragmentQuizBinding>() {
    var mCurrentPosition : Int = 3
    var mQuestionList : ArrayList<QuizData>?=null
    private var Introvert : Int = 0
    private var Extravert : Int = 0
    private var Intuitive : Int = 0
    private var Observant : Int = 0
    private var Thinking : Int = 0
    private var Feeling : Int = 0
    private var Judging : Int = 0
    private var Prospecting : Int = 0
    private var Assertive : Int = 0
    private var Turbulent : Int = 0

    //Biến tạm
    var mIntrovert = mutableListOf<Int>(0,0,0)
    var mExtravert = mutableListOf<Int>(0,0,0)
    var mIntuitive = mutableListOf<Int>(0,0,0)
    var mObservant = mutableListOf<Int>(0,0,0)
    var mThinking = mutableListOf<Int>(0,0,0)
    var mFeeling = mutableListOf<Int>(0,0,0)
    var mJudging = mutableListOf<Int>(0,0,0)
    var mProspecting = mutableListOf<Int>(0,0,0)
    var mAssertive = mutableListOf<Int>(0,0,0)
    var mTurbulent = mutableListOf<Int>(0,0,0)

    //Personality
    var mChar1 : String = ""
    var mChar2 : String = ""
    var mChar3 : String = ""
    var mChar4 : String = ""
    var mChar5 : String = ""
    var userPersonalities : String = ""

    private lateinit var question1 : QuizData
    private lateinit var question2 : QuizData
    private lateinit var question3 : QuizData

    private var mLine1 : Boolean = false
    private var mLine2 : Boolean = false
    private var mLine3 : Boolean = false

    override fun onSetupView() {
        mQuestionList = QuizConstants.getQuestion()
        setQuestion()
        calculate()




    }
    private fun setQuestion() {
        mLine1 = false
        mLine2 = false
        mLine3 = false
        question1 = mQuestionList!![mCurrentPosition - 3]
        question2 = mQuestionList!![mCurrentPosition - 2]
        question3 = mQuestionList!![mCurrentPosition - 1]
        binding.txQuiz1.text = question1.question.toString()
        binding.txQuiz2.text = question2.question.toString()
        binding.txQuiz3.text = question3.question.toString()
        binding.txQuestion.text = "$mCurrentPosition"
    }
    private fun calculate( ) {
        //Display questions

            question1 = mQuestionList!![mCurrentPosition - 3]
            question2 = mQuestionList!![mCurrentPosition - 2]
            question3 = mQuestionList!![mCurrentPosition - 1]



            //Buttons event

            //left buttons
            binding.btnLeft1.setOnClickListener {
                defaultOptionViewLine1()
                mLine1 = true

                binding.btnLeft1.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_agree_selected
                )


                if (question1.questionType == QuizData.QuestionType.Mind )
                {
                    mExtravert[0] = 0
                    mIntrovert[0] = 0
                    if (question1.TypeReference == 1 ) {

                        mIntrovert[0] +=1
                    }
                    else if (question1.TypeReference == 0 ) {

                        mExtravert[0] += 1
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Energy) {
                    mObservant[0] = 0
                    mIntuitive[0] = 0
                    if (question1.TypeReference == 1 ) {

                        mIntuitive[0] += 1

                    }
                    else if (question1.TypeReference == 0 ) {

                        mObservant[0] += 1
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Nature) {
                    mFeeling[0] = 0
                    mThinking[0] = 0
                    if (question1.TypeReference == 1 ) {

                        mThinking[0] += 1
                    }
                    else if (question1.TypeReference == 0 ) {

                        mFeeling[0] += 1
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Tactics) {
                    mProspecting[0] =0
                    mJudging[0] =0
                    if (question1.TypeReference == 1 ) {

                        mJudging[0] += 1
                    }
                    else if (question1.TypeReference == 0 ) {

                        mProspecting[0] += 1
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Identity ) {
                    mTurbulent[0] = 0
                    mAssertive[0] = 0
                    if (question1.TypeReference == 1 ) {

                        mAssertive[0] += 1
                    }
                    else if (question1.TypeReference == 0 ) {

                        mTurbulent[0] += 1
                    }
                }


            }
            binding.btnLeft2.setOnClickListener {
                defaultOptionViewLine2()
                mLine2 = true
                binding.btnLeft2.background = ContextCompat.getDrawable(
                    requireActivity(), R.drawable.custom_button_quiz_circle_agree_selected)
                resetPoint2()
                if (question2.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[1] = 0
                    mExtravert[1] = 0
                    if (question2.TypeReference == 1 ) {

                        mIntrovert[1] += 1
                    }
                    else if (question2.TypeReference == 0 ) {

                        mExtravert[1] += 1
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Energy) {
                    mIntuitive[1] = 0
                    mObservant[1] = 0
                    if (question2.TypeReference == 1) {

                        mIntuitive[1] += 1
                    }
                    else if (question2.TypeReference == 0 ) {

                        mObservant[1] += 1
                    }
                }
                else if (question2.questionType == QuizData.QuestionType.Nature
                    ) {
                    mThinking[1] = 0
                    mFeeling[1] = 0
                    if (question2.TypeReference == 1 ) {

                        mThinking[1] += 1
                    }
                    else if (question2.TypeReference == 0 ) {

                        mFeeling[1] += 1
                    }
                }
                else if (question2.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[1] = 0
                    mProspecting[1] = 0
                    if (question2.TypeReference == 1 ) {

                        mJudging[1] += 1
                    }
                    else if (question2.TypeReference == 0) {

                        mProspecting[1] += 1
                    }
                }
                else if (question2.questionType == QuizData.QuestionType.Identity) {
                    mAssertive[1] = 0
                    mTurbulent[1] = 0
                    if (question2.TypeReference == 1 ) {

                        mAssertive[1] += 1
                    }
                    else if (question2.TypeReference == 0 ) {

                        mTurbulent[1] += 1
                    }
                }
            }
            binding.btnLeft3.setOnClickListener {
                defaultOptionViewLine3()
                mLine3 = true
                binding.btnLeft3.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_agree_selected
                )
                resetPoint3()
                if (question3.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[2] = 0
                    mExtravert[2] = 0
                    if (question3.TypeReference == 1) {

                        mIntrovert[2] += 1
                    }
                    else if (question3.TypeReference == 0) {

                        mExtravert[2] += 1
                    }
                }
                else if (question3.questionType == QuizData.QuestionType.Energy) {
                    mIntuitive[2] = 0
                    mObservant[2] = 0
                    if (question3.TypeReference == 1) {
                        mIntuitive[2] = 0
                        mIntuitive[2] += 1
                    }
                    else if (question3.TypeReference == 0) {

                        mObservant[2] += 1
                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Nature) {
                    mThinking[2] = 0
                    mFeeling[2] = 0
                    if (question3.TypeReference == 1) {

                        mThinking[2] += 1
                    }
                    else if (question3.TypeReference == 0) {

                        mFeeling[2] += 1
                    }
                }
                else if (question3.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[2] = 0
                    mProspecting[2] =0
                    if (question3.TypeReference == 1) {

                        mJudging[2] += 1
                    }
                    else if (question3.TypeReference == 0) {

                        mProspecting[2] += 1
                    }
                }
                else if (question3.questionType == QuizData.QuestionType.Identity) {
                    mAssertive[2] = 0
                    mTurbulent[2] = 0
                    if (question3.TypeReference ==1) {

                        mAssertive[2] += 1
                    }
                    else if (question3.TypeReference == 0) {

                        mTurbulent[2] += 1
                    }
                }
            }

            // further left buttons
            binding.btnFurtherleft1.setOnClickListener {
                defaultOptionViewLine1()
                mLine1 = true
                binding.btnFurtherleft1.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_agree_selected
                )
                resetPoint1()
                if (question1.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[0] = 0
                    mExtravert[0] =0
                    if (question1.TypeReference == 1) {

                        mIntrovert[0] += 2
                    }
                    else if (question1.TypeReference == 0 ) {

                        mExtravert[0] += 2
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Energy) {
                    mIntuitive[0] =0
                    mObservant[0] =0
                    if (question1.TypeReference == 1) {

                        mIntuitive[0] += 2
                    }
                    else if (question1.TypeReference == 0) {
                        mObservant[0] += 2
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Nature ) {
                    mFeeling[0] =0
                    mThinking[0] =0
                    if (question1.TypeReference == 1) {
                        mThinking[0] += 2
                    }
                    else if (question1.TypeReference == 0) {
                        mFeeling[0] += 2
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Tactics) {
                    mProspecting[0] =0
                    mJudging[0] = 0
                    if (question1.TypeReference == 1) {
                        mJudging[0] += 2
                    }
                    else if (question1.TypeReference == 0) {
                        mProspecting[0] += 2
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Identity) {
                    mTurbulent[0] =0
                    mAssertive[0] =0
                    if (question1.TypeReference == 1 ) {
                        mAssertive[0] += 2
                    }
                    else if (question1.TypeReference == 0 ) {
                        mTurbulent[0] += 2
                    }
                }
            }
            binding.btnFurtherleft2.setOnClickListener {
                defaultOptionViewLine2()
                mLine2 = true
                binding.btnFurtherleft2.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_agree_selected
                )
                resetPoint2()
                if (question2.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[1] =0
                    mExtravert[1] =0
                    if (question2.TypeReference == 1) {
                        mIntrovert[1] +=2

                    }
                    else if (question2.TypeReference == 0) {
                        mExtravert[1] +=2

                    }
                }
                else if (question2.questionType == QuizData.QuestionType.Energy) {
                    mObservant[1] =0
                    mIntuitive[1] =0
                    if (question2.TypeReference == 1) {
                        mIntuitive[1] +=2

                    }
                    else if (question2.TypeReference == 0) {
                        mObservant[1] +=2

                    }
                }
                else if (question2.questionType == QuizData.QuestionType.Nature
                    ) {
                    mThinking[1] =0
                    mFeeling[1] =0
                    if (question2.TypeReference == 1 ) {
                        mThinking[1] +=2

                    }
                    else if (question2.TypeReference == 0 ) {
                        mFeeling[1] +=2

                    }
                }
                else if (question2.questionType == QuizData.QuestionType.Tactics
                    ) {
                    mJudging[1] =0
                    mProspecting[1] =0
                    if (question2.TypeReference == 1) {
                        mJudging[1] +=2

                    }
                    else if (question2.TypeReference == 0 ) {
                        mProspecting[1] +=2

                    }
                }
                else if (question2.questionType == QuizData.QuestionType.Identity
                    ) {
                    mTurbulent[1] =0
                    mAssertive[1] =0
                    if (question2.TypeReference == 1) {
                        mAssertive[1] +=2

                    }
                    else if (question2.TypeReference == 0) {
                        mTurbulent[1] +=2

                    }
                }
            }
            binding.btnFurtherleft3.setOnClickListener {
                defaultOptionViewLine3()
                mLine3 = true
                binding.btnFurtherleft3.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_agree_selected
                )
                resetPoint3()
                if (question3.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[2] =0
                    mExtravert[2] =0
                    if (question3.TypeReference == 1) {
                        mIntrovert[2] +=2

                    }
                    else if (question3.TypeReference == 0) {
                        mExtravert[2] +=2

                    }
                }
                else if (question3.questionType == QuizData.QuestionType.Energy) {
                    mIntuitive[2] =0
                    mObservant[2] =0
                    if (question3.TypeReference == 1) {
                        mIntuitive[2] +=2

                    }
                    else if (question3.TypeReference == 0) {
                        mObservant[2] +=2

                    }
                }
                else if (question3.questionType == QuizData.QuestionType.Nature) {
                    mThinking[2] =0
                    mFeeling[2] =0
                    if (question3.TypeReference == 1) {
                        mThinking[2] +=2

                    }
                    else if (question3.TypeReference == 0) {
                        mFeeling[2] +=2

                    }
                }
                else if (question3.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[2] =0
                    mProspecting[2] =0
                    if (question3.TypeReference == 1) {
                        mJudging[2] +=2

                    }
                    else if (question3.TypeReference == 0) {
                        mProspecting[2] +=2

                    }
                }
                else if (question3.questionType == QuizData.QuestionType.Identity) {
                    mAssertive[2] =0
                    mTurbulent[2] =0
                    if (question3.TypeReference ==1) {
                        mAssertive[2] +=2

                    }
                    else if (question3.TypeReference == 0) {
                        mTurbulent[2] +=2

                    }
                }
            }

            // furthest left buttons
            binding.btnFurthestleft1.setOnClickListener {
                defaultOptionViewLine1()
                mLine1 = true
                binding.btnFurthestleft1.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_agree_selected
                )
                resetPoint1()
                if (question1.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[0] = 0
                    mExtravert[0] = 0
                    if (question1.TypeReference == 1 ) {

                        mIntrovert[0] += 3
                    }
                    else if (question1.TypeReference == 0 ) {

                        mExtravert[0] += 3
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Energy ) {
                    mIntuitive[0] = 0
                    mObservant[0] = 0
                    if (question1.TypeReference == 1) {

                        mIntuitive[0] +=3
                    }
                    else if (question1.TypeReference == 0) {

                        mObservant[0] += 3
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Nature) {
                    mThinking[0] = 0
                    mFeeling[0] = 0
                    if (question1.TypeReference == 1) {

                        mThinking[0] += 3
                    }
                    else if (question1.TypeReference == 0 ) {

                        mFeeling[0] += 3
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Tactics ) {
                    mProspecting[0] = 0
                    mJudging[0] = 0
                    if (question1.TypeReference == 1) {

                        mJudging[0] += 3
                    }
                    else if (question1.TypeReference == 0) {

                        mProspecting[0] += 3
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Identity ) {
                    mTurbulent[0] = 0
                    mAssertive[0] = 0
                    if (question1.TypeReference == 1) {

                        mAssertive[0] += 3
                    }
                    else if (question1.TypeReference == 0 ) {

                        mTurbulent[0] += 3
                    }
                }
            }
            binding.btnFurthestleft2.setOnClickListener {
                defaultOptionViewLine2()
                mLine2 = true
                binding.btnFurthestleft2.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_agree_selected
                )
                resetPoint2()
                if (question2.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[1] = 0
                    mExtravert[1] = 0
                    if (question2.TypeReference == 1 ) {

                        mIntrovert[1] += 3
                    }
                    else if (question2.TypeReference == 0 ) {

                        mExtravert[1] += 3
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Energy ) {
                    mObservant[1] = 0
                    mIntuitive[1] = 0
                    if (question2.TypeReference == 1) {

                        mIntuitive[1] += 3
                    }
                    else if (question2.TypeReference == 0) {

                        mObservant[1] += 3
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Nature) {
                    mThinking[1] = 0
                    mFeeling[1] = 0
                    if (question1.TypeReference == 1) {

                        mThinking[1] += 3
                    }
                    else if (question2.TypeReference == 0 ) {

                        mFeeling[1] += 3
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Tactics ) {
                    mJudging[1] = 0
                    mJudging[1] = 0
                    if (question2.TypeReference == 1) {

                        mJudging[1] += 3
                    }
                    else if (question2.TypeReference == 0) {

                        mProspecting[1] += 3
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Identity ) {
                    mAssertive[1] = 0
                    mTurbulent[1] = 0
                    if (question2.TypeReference == 1) {
                        mAssertive[1] += 3

                    }
                    else if (question2.TypeReference == 0 ) {

                        mTurbulent[1] += 3
                    }
                }
            }
            binding.btnFurthestleft3.setOnClickListener {
                defaultOptionViewLine3()
                mLine3 = true
                binding.btnFurthestleft3.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_agree_selected
                )
                resetPoint3()
                if (question3.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[2] =0
                    mExtravert[2] =0
                    if (question3.TypeReference == 1 ) {
                        mIntrovert[2] +=3

                    }
                    else if (question3.TypeReference == 0 ) {
                        mExtravert[2] +=3

                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Energy ) {
                    mIntuitive[2] =0
                    mObservant[2] =0
                    if (question3.TypeReference == 1) {
                        mIntuitive[2] +=3

                    }
                    else if (question3.TypeReference == 0) {
                        mObservant[2] +=3

                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Nature) {
                    mThinking[2] =0
                    mFeeling[2] =0
                    if (question3.TypeReference == 1) {
                        mThinking[2] +=3

                    }
                    else if (question3.TypeReference == 0 ) {
                        mFeeling[2] +=3

                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Tactics ) {
                    mJudging[2] =0
                    mProspecting[2] =0
                    if (question3.TypeReference == 1) {
                        mJudging[2] +=3

                    }
                    else if (question3.TypeReference == 0) {
                        mProspecting[2] +=3

                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Identity ) {
                    mTurbulent[2] =0
                    mAssertive[2] =0
                    if (question3.TypeReference == 1) {
                        mAssertive[2] +=3

                    }
                    else if (question3.TypeReference == 0 ) {
                        mTurbulent[2] +=3

                    }
                }
            }

            //right buttons
            binding.btnRight1.setOnClickListener {
                defaultOptionViewLine1()
                mLine1 = true
                binding.btnRight1.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_disagree_selected
                )
                resetPoint1()
                if (question1.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[0] =0
                    mExtravert[0] =0
                    if (question1.TypeReference == 0 ) {

                        mIntrovert[0] +=1
                    }
                    else if (question1.TypeReference == 1 ) {
                        mExtravert[0] = 0
                        mExtravert[0] +=1
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Energy ) {
                    mIntuitive[0] =0
                    mObservant[0] =0
                    if (question1.TypeReference == 0 ) {

                        mIntuitive[0] +=1
                    }
                    else if (question1.TypeReference == 1) {

                        mObservant[0] +=1
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Nature) {
                    mThinking[0] =0
                    mFeeling[0]=0
                    if (question1.TypeReference == 0 ) {

                        mThinking[0] += 1
                    }
                    else if (question1.TypeReference == 1 ) {

                        mFeeling[0] +=1
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[0] =0
                    mProspecting[0] =0
                    if (question1.TypeReference == 0 ) {

                        mJudging[0] +=1
                    }
                    else if (question1.TypeReference == 1 ) {

                        mJudging[0] +=1
                    }
                }
                else if (question1.questionType == QuizData.QuestionType.Identity ) {
                    mAssertive[0] =0
                    mTurbulent[0] =0
                    if (question1.TypeReference == 0 ) {
                        mAssertive[0] = 0
                        mAssertive[0] +=1
                    }
                    else if (question1.TypeReference == 1) {
                        mTurbulent[0] = 0
                        mTurbulent[0] +=1
                    }
                }


            }
            binding.btnRight2.setOnClickListener {
                defaultOptionViewLine2()
                mLine2 = true
                binding.btnRight2.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_disagree_selected
                )
                resetPoint2()
                if (question2.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[1] = 0
                    mExtravert[1] = 0
                    if (question2.TypeReference == 0 ) {

                        mIntrovert[1] += 1
                    }
                    else if (question1.TypeReference == 1 ) {
                        mExtravert[1] += 1
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Energy ) {
                    mIntuitive[1] = 0
                    mObservant[1] = 0
                    if (question2.TypeReference == 0 ) {
                        mIntuitive[1] += 1
                    }
                    else if (question1.TypeReference == 1) {
                        mObservant[1] += 1
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Nature) {
                    mThinking[1] = 0
                    mFeeling[1] = 0
                    if (question1.TypeReference == 0 ) {
                    mThinking[1] += 1

                }
                else if (question2.TypeReference == 1 ) {
                    mFeeling[1] += 1

                }
            }
            else if ( question2.questionType == QuizData.QuestionType.Tactics) {
                mJudging[1] = 0
                mProspecting[1] = 0
                if (question2.TypeReference == 0 ) {
                    mJudging[1] += 1
                }
                else if (question2.TypeReference == 1 ) {
                    mProspecting[1] += 1
                }
            }
            else if (question2.questionType == QuizData.QuestionType.Identity ) {
                mAssertive[1] = 0
                mTurbulent[1] = 0
                if (question2.TypeReference == 0 ) {

                    mAssertive[1] += 1
                }
                else if (question2.TypeReference == 1) {

                    mTurbulent[1] += 1
                }
            }
            }
            binding.btnRight3.setOnClickListener {
                defaultOptionViewLine3()
                mLine3 = true
                binding.btnRight3.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_disagree_selected
                )
                resetPoint3()
                if (question3.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[2] =0
                    mExtravert[2] =0
                    if (question3.TypeReference == 0 ) {
                        mIntrovert[2] +=1

                    }
                    else if (question3.TypeReference == 1 ) {
                        mExtravert[2] +=1

                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Energy ) {
                    mIntuitive[2] =0
                    mObservant[2] =0
                    if (question3.TypeReference == 0 ) {
                        mIntuitive[2] +=1

                    }
                    else if (question3.TypeReference == 1) {
                        mObservant[2] +=1
                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Nature) {
                    mThinking[2] =0
                    mFeeling[2] =0
                    if (question3.TypeReference == 0 ) {
                        mThinking[2] +=1

                    }
                    else if (question3.TypeReference == 1 ) {
                        mFeeling[2] +=1

                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[2] =0
                    mProspecting[2] =0
                    if (question3.TypeReference == 0 ) {
                        mJudging[2] +=1

                    }
                    else if (question3.TypeReference == 1 ) {
                        mProspecting[2] +=1

                    }
                }
                else if (question3.questionType == QuizData.QuestionType.Identity ) {
                    mTurbulent[2] =0
                    mAssertive[2] =0
                    if (question3.TypeReference == 0 ) {
                        mAssertive[2] +=1

                    }
                    else if (question3.TypeReference == 1) {
                        mTurbulent[2] +=1

                    }
                }
            }

            //further right buttons
            binding.btnFurtherright1.setOnClickListener {
                defaultOptionViewLine1()
                mLine1 = true
                binding.btnFurtherright1.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_disagree_selected
                )
                resetPoint1()
                if (question1.questionType == QuizData.QuestionType.Mind)
                {
                    mExtravert[0] =0
                    mIntrovert[0] =0
                    if (question1.TypeReference == 0 ) {
                        mIntrovert[0] +=2

                    }
                    else if (question1.TypeReference == 1 ) {
                        mExtravert[0] +=2

                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Energy ) {
                    mObservant[0] =0
                    mIntuitive[0] =0
                    if (question1.TypeReference == 0 ) {
                        mIntuitive[0] +=2

                    }
                    else if (question1.TypeReference == 1) {
                        mObservant[0] +=2

                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Nature) {
                    mFeeling[0] =0
                    mThinking[0] =0
                    if (question1.TypeReference == 0 ) {
                        mThinking[0] +=2

                    }
                    else if (question1.TypeReference == 1 ) {
                        mFeeling[0] +=2

                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[0] = 0
                    mProspecting[0] =0
                    if (question1.TypeReference == 0 ) {

                        mJudging[0] += 2
                    }
                    else if (question1.TypeReference == 1 ) {
                        mProspecting[0] += 2
                    }
                }
                else if (question1.questionType == QuizData.QuestionType.Identity ) {
                    mAssertive[0] =0
                    mTurbulent[0] =0
                    if (question1.TypeReference == 0 ) {
                        mAssertive[0] += 2
                    }
                    else if (question1.TypeReference == 1) {
                        mTurbulent[0] += 2
                    }
                }
            }
            binding.btnFurtherright2.setOnClickListener {
                defaultOptionViewLine2()
                mLine2 = true
                binding.btnFurtherright2.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_disagree_selected
                )
                resetPoint2()
                if (question2.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[1] = 0
                    mExtravert[1] = 0
                    if (question2.TypeReference == 0 ) {
                        mIntrovert[1] += 2
                    }
                    else if (question2.TypeReference == 1 ) {
                        mExtravert[1] += 2
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Energy ) {
                    mIntuitive[1] = 0
                    mObservant[1] = 0
                    if (question2.TypeReference == 0 ) {
                        mIntuitive[1] += 2
                    }
                    else if (question2.TypeReference == 1) {
                        mObservant[1] += 2
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Nature) {
                    mThinking[1] = 0
                    mFeeling[1]  = 0
                    if (question2.TypeReference == 0 ) {
                        mThinking[1] += 2
                    }
                    else if (question2.TypeReference == 1 ) {
                        mFeeling[1] += 2
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[1] = 0
                    mProspecting[1] = 0
                    if (question2.TypeReference == 0 ) {
                        mJudging[1] += 2
                    }
                    else if (question2.TypeReference == 1 ) {
                        mProspecting[1] += 2
                    }
                }
                else if (question2.questionType == QuizData.QuestionType.Identity ) {
                    mAssertive[1] =0
                    mTurbulent[1] =0
                    if (question2.TypeReference == 0 ) {
                        mAssertive[1] += 2
                    }
                    else if (question2.TypeReference == 1) {
                        mTurbulent[1] += 2
                    }
                }
            }
            binding.btnFurtherright3.setOnClickListener {
                defaultOptionViewLine3()
                mLine3 = true
                binding.btnFurtherright3.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_disagree_selected
                )
                resetPoint3()
                if (question3.questionType == QuizData.QuestionType.Mind)
                {
                    mIntrovert[2] =0
                    mExtravert[2] =0
                    if (question3.TypeReference == 0 ) {
                        mIntrovert[2] += 2
                    }
                    else if (question3.TypeReference == 1 ) {
                        mExtravert[2] += 2
                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Energy ) {
                    mIntuitive[2] =0
                    mObservant[2] =0

                    if (question3.TypeReference == 0 ) {
                        mIntuitive[2] += 2
                    }
                    else if (question3.TypeReference == 1) {
                        mObservant[2] += 2
                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Nature) {
                    mThinking[2] =0
                    mFeeling[2] =0
                    if (question3.TypeReference == 0 ) {
                        mThinking[2] += 2
                    }
                    else if (question3.TypeReference == 1 ) {
                        mFeeling[2] += 2
                    }
                }
                else if ( question3.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[2] =0
                    mProspecting[2] =0
                    if (question3.TypeReference == 0 ) {
                        mJudging[2] += 2
                    }
                    else if (question3.TypeReference == 1 ) {
                        mProspecting[2] += 2
                    }
                }
                else if (question3.questionType == QuizData.QuestionType.Identity ) {
                    mAssertive[2] = 0
                    mTurbulent[2] = 0
                    if (question3.TypeReference == 0 ) {
                        mAssertive[2] += 2
                    }
                    else if (question3.TypeReference == 1) {
                        mTurbulent[2] += 2
                    }
                }
            }

            //furthest right buttons
            binding.btnFurthestright1.setOnClickListener {
                defaultOptionViewLine1()
                mLine1 = true
                binding.btnFurthestright1.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_disagree_selected
                )
                resetPoint1()
                if (question1.questionType == QuizData.QuestionType.Mind )
                {
                    mIntrovert[0] =0
                    mExtravert[0] =0
                    if (question1.TypeReference == 0 ) {
                        mIntrovert[0] +=3
                    }
                    else if (question1.TypeReference == 1 ) {
                        mExtravert[0] +=3
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Energy) {
                    mObservant[0] =0
                    mIntuitive[0] =0
                    if (question1.TypeReference == 0) {
                        mIntuitive[0] += 3
                    }
                    else if (question1.TypeReference == 1) {
                        mObservant[0] += 3
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Nature ) {
                    mThinking[0] =0
                    mFeeling[0] =0
                    if (question1.TypeReference == 0 ) {
                        mThinking[0] += 3
                    }
                    else if (question1.TypeReference == 1) {
                        mFeeling[0] += 3
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[0] =0
                    mProspecting[0] =0
                    if (question1.TypeReference == 0) {
                        mJudging[0] += 3
                    }
                    else if (question1.TypeReference == 1) {
                        mProspecting[0] += 3
                    }
                }
                else if ( question1.questionType == QuizData.QuestionType.Identity) {
                    mAssertive[0] =0
                    mTurbulent[0] =0
                    if (question1.TypeReference == 0 ) {
                        mAssertive[0] += 3
                    }
                    else if (question1.TypeReference == 1) {
                        mTurbulent[0] += 3
                    }
                }
            }
            binding.btnFurthestright2.setOnClickListener {
                defaultOptionViewLine2()
                mLine2 = true
                binding.btnFurthestright2.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_disagree_selected
                )
                resetPoint2()
                if (question2.questionType == QuizData.QuestionType.Mind )
                {
                    mIntrovert[1] =0
                    mExtravert[1] =0
                    if (question2.TypeReference == 0 ) {
                        mIntrovert[1] += 3
                    }
                    else if (question2.TypeReference == 1 ) {
                        mExtravert[1] += 3
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Energy) {
                    mIntuitive[1] =0
                    mObservant[1] =0
                    if (question2.TypeReference == 0) {
                        mIntuitive[1] += 3
                    }
                    else if (question2.TypeReference == 1) {
                        mObservant[1] += 3
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Nature ) {
                    mThinking[1] =0
                    mFeeling[1] =0
                    if (question2.TypeReference == 0 ) {
                        mThinking[1] += 3
                    }
                    else if (question2.TypeReference == 1) {
                        mFeeling[1] += 3
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[1] =0
                    mProspecting[1] =0
                    if (question2.TypeReference == 0) {
                        mJudging[1] += 3
                    }
                    else if (question2.TypeReference == 1) {
                        mProspecting[1] += 3
                    }
                }
                else if ( question2.questionType == QuizData.QuestionType.Identity) {
                    mAssertive[1] =0
                    mTurbulent[1] =0
                    if (question2.TypeReference == 0 ) {
                        mAssertive[1] += 3
                    }
                    else if (question2.TypeReference == 1) {
                        mTurbulent[1] += 3
                    }
                }
            }
            binding.btnFurthestright3.setOnClickListener {
                defaultOptionViewLine3()
                mLine3 = true
                binding.btnFurthestright3.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_disagree_selected
                )
                resetPoint3()
                if (question3.questionType == QuizData.QuestionType.Mind )
                {
                    mIntrovert[2] =0
                    mExtravert[2] =0
                    if (question3.TypeReference == 0 ) {
                        mIntrovert[2] += 3
                    }
                    else if (question3.TypeReference == 1 ) {
                        mExtravert[2] += 3
                    }
                }
                if ( question3.questionType == QuizData.QuestionType.Energy) {
                    mIntuitive[2] =0
                    mObservant[2] =0
                    if (question3.TypeReference == 0) {
                        mIntuitive[2] += 3
                    }
                    else if (question3.TypeReference == 1) {
                        mObservant[2] += 3
                    }
                }
                if ( question3.questionType == QuizData.QuestionType.Nature ) {
                    mThinking[2] =0
                    mFeeling[2] =0
                    if (question3.TypeReference == 0 ) {
                        mThinking[2] += 3
                    }
                    else if (question3.TypeReference == 1) {
                        mFeeling[2] += 3
                    }
                }
                if ( question3.questionType == QuizData.QuestionType.Tactics) {
                    mJudging[2] =0
                    mProspecting[2] =0
                    if (question3.TypeReference == 0) {
                        mJudging[2] += 3
                    }
                    else if (question3.TypeReference == 1) {
                        mProspecting[2] += 3
                    }
                }
                if ( question3.questionType == QuizData.QuestionType.Identity) {
                    mAssertive[2] =0
                    mTurbulent[2] =0
                    if (question3.TypeReference == 0 ) {
                        mAssertive[2] += 3
                    }
                    else if (question3.TypeReference == 1) {
                        mTurbulent[2] += 3
                    }
                }
            }

            //neutral buttons
            binding.btnMiddle1.setOnClickListener {
                defaultOptionViewLine1()
                mLine1 = true
                binding.btnMiddle1.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_neutral_selected
                )
                resetPoint1()
            }
            binding.btnMiddle2.setOnClickListener {
                defaultOptionViewLine2()
                mLine2 = true
                binding.btnMiddle2.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_neutral_selected
                )
                resetPoint2()
            }
            binding.btnMiddle3.setOnClickListener {
                defaultOptionViewLine3()
                mLine3 = true
                binding.btnMiddle3.background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_button_quiz_circle_neutral_selected
                )
                resetPoint3()
            }

            //next button

            binding.btnNext.setOnClickListener {
                if(!mLine1||!mLine2||!mLine3) {
                    binding.root.showSnackbar("Bạn phải trả lời hết các câu hỏi :(")
                    return@setOnClickListener
                }
                else if (mCurrentPosition < mQuestionList!!.size ) {
                    mCurrentPosition += 3
                    setQuestion()
                    defaultOptionViewLine1()
                    defaultOptionViewLine2()
                    defaultOptionViewLine3()
                    Introvert += mIntrovert[0] + mIntrovert[1] + mIntrovert[2]
                    Extravert += mExtravert[0] + mExtravert[1] + mExtravert[2]
                    Intuitive += mIntuitive[0] + mIntuitive[1] + mIntuitive[2]
                    Observant += mObservant[0] + mObservant[1] + mObservant[2]
                    Thinking += mThinking[0] + mThinking[1] + mThinking[2]
                    Feeling += mFeeling[0] + mFeeling[1] + mFeeling[2]
                    Judging += mJudging[0] + mJudging[1] + mJudging[2]
                    Prospecting += mProspecting[0] + mProspecting[1] + mProspecting[2]
                    Assertive += mAssertive[0] + mAssertive[1] + mAssertive[2]
                    Turbulent += mTurbulent[0] + mTurbulent[1] + mTurbulent[2]
                }
                else {
                    binding.btnNext.setImageResource(R.drawable.ic_done)
                    binding.txQuestion.text = "Xem kết quả"
                    binding.sharp.text = null
                    binding.txTotalquestion.text = null
                    if (Introvert > Extravert){
                        mChar1 = "I"

                    } else if (Extravert > Introvert)  {
                        mChar1 = "E"
                    }
                    if (Intuitive > Observant) {
                        mChar2 = "N"
                    } else if (Observant > Intuitive) {
                        mChar2 = "O"
                    }
                    if (Thinking > Feeling) {
                        mChar3 = "T"
                    } else if (Feeling > Thinking) {
                        mChar3 = "F"
                    }
                    if (Judging > Prospecting) {
                        mChar4 = "J"
                    } else if (Prospecting > Judging) {
                        mChar4 = "P"
                    }
                    if (Assertive > Turbulent) {
                        mChar5 = "-A"
                    } else if (Turbulent > Assertive) {
                        mChar5 = "-T"
                    }
                    userPersonalities = "$mChar1" + "$mChar2" + "$mChar3" + "$mChar4" + "$mChar5"
                    Toast.makeText(requireActivity(), "$userPersonalities", Toast.LENGTH_LONG).show()
                    val result = userPersonalities
                    val action = FragmentQuizDirections.actionFragmentQuizToFragmentQuizResult(result)
                    findNavController().navigate(action)

                }
                }
    }

    private fun defaultOptionViewLine1() {
        binding.btnMiddle1.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_neutral)
        binding.btnLeft1.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_agree)
        binding.btnFurtherleft1.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_agree)
        binding.btnFurthestleft1.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_agree)
        binding.btnRight1.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_disagree)
        binding.btnFurtherright1.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_disagree)
        binding.btnFurthestright1.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_disagree)

    }
    private fun defaultOptionViewLine2() {
        binding.btnMiddle2.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_neutral)
        binding.btnLeft2.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_agree)
        binding.btnFurtherleft2.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_agree)
        binding.btnFurthestleft2.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_agree)
        binding.btnRight2.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_disagree)
        binding.btnFurtherright2.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_disagree)
        binding.btnFurthestright2.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_disagree)

    }
    private fun defaultOptionViewLine3() {
        binding.btnMiddle3.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_neutral)
        binding.btnLeft3.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_agree)
        binding.btnFurtherleft3.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_agree)
        binding.btnFurthestleft3.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_agree)
        binding.btnRight3.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_disagree)
        binding.btnFurtherright3.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_disagree)
        binding.btnFurthestright3.background = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_button_quiz_circle_disagree)

    }
    private fun resetPoint1() {
        mExtravert[0] = 0
        mIntrovert[0] = 0
        mObservant[0] = 0
        mIntuitive[0] = 0
        mFeeling[0] = 0
        mThinking[0] = 0
        mProspecting[0] =0
        mJudging[0] =0
        mTurbulent[0] = 0
        mAssertive[0] = 0



    }
    private fun resetPoint2() {
        mExtravert[1] = 0
        mIntrovert[1] = 0
        mObservant[1] = 0
        mIntuitive[1] = 0
        mFeeling[1] = 0
        mThinking[1] = 0
        mProspecting[1] =0
        mJudging[1] =0
        mTurbulent[1] = 0
        mAssertive[1] = 0

    }
    private fun resetPoint3(){

        mExtravert[2] = 0
        mIntrovert[2] = 0
        mObservant[2] = 0
        mIntuitive[2] = 0
        mFeeling[2] = 0
        mThinking[2] = 0
        mProspecting[2] =0
        mJudging[2] =0
        mTurbulent[2] = 0
        mAssertive[2] = 0

    }


}
