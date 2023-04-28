package com.qwict.isbin.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("title", "Not Found");
                model.addAttribute("message", "This page was not found.");
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("title", "Internal Server Error");
                model.addAttribute("message", "An internal server error has occurred.");
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("title", "Forbidden");
                model.addAttribute("message", "You do not have permission to access this page.");
            } else {
                model.addAttribute("title", "Error");
                model.addAttribute("message", "An error has occurred.");
            }
            return "status-error";
        }
        return "error";
    }
}
