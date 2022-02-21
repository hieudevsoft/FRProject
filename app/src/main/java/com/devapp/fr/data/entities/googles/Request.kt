package com.devapp.fr.data.entities.googles

data class Request(
    val ink: List<List<List<Int>>>,
    val max_completions: Int,
    val max_num_results: Int,
    val pre_context: String,
    val writing_guide: WritingGuide
)