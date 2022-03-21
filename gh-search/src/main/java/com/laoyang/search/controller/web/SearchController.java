package com.laoyang.search.controller.web;

import com.laoyang.search.server.GoopSearchService;
import com.laoyang.search.vo.SearchParams;
import com.laoyang.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Resource
    GoopSearchService searchService;


    @GetMapping("/list.html")
    public String searchMainPage() {
        return "list";
    }


    /**
     * 前台搜索处理接口
     * @param params    接收的参数集合
     * @param model     //
     * @return
     */
    @GetMapping("/search")
//    @ResponseBody
    public Object search(SearchParams params, Model model) {
        SearchResult searchResult = searchService.searchParams(params);
        model.addAttribute("result", searchResult);
        return "list";
    }
}
