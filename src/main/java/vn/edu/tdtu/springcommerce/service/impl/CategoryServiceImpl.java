package vn.edu.tdtu.springcommerce.service.impl;

import vn.edu.tdtu.springcommerce.model.Category;
import vn.edu.tdtu.springcommerce.repository.CategoryRepository;
import vn.edu.tdtu.springcommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }//findAll

    public void updateCategory(Category category){
    	categoryRepository.save(category);
    }//add or update (tuy vao pri-key)

    public void removeCategoryById(Long id){
        categoryRepository.deleteById(id);
    }//delete truyen vao pri-key

    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }//search theo id

    public List<String> findCategoryByProductId(Long id){
        return categoryRepository.findCategoryByProduct(id);
    }
}
