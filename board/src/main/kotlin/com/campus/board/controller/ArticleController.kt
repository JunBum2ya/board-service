package com.campus.board.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/articles")
class ArticleController {

    @GetMapping
    fun articles(modelMap : ModelMap) : String {
        return "articles/index"
    }

}