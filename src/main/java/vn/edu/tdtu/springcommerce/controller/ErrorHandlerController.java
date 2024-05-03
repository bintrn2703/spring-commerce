package vn.edu.tdtu.springcommerce.controller;

import vn.edu.tdtu.springcommerce.configuration.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }
}