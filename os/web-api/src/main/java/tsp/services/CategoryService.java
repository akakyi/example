package tsp.services;

import tsp.database.dao.CategoryDao;
import tsp.database.entity.CategoryEntity;
import tsp.rest.json.request.category.CategoryCreateJson;
import tsp.rest.json.request.category.CategoryUpdateJson;
import tsp.rest.json.response.CategoryResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class CategoryService {

    @EJB
    private CategoryDao categoryDao;

    public List<CategoryResponse> getAllCategory() {
        return categoryDao.list().stream().map(CategoryResponse::transform).collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(int id) {
        CategoryEntity resuilt = categoryDao.getById(id);
        return resuilt == null ? null : CategoryResponse.transform(resuilt);
    }

    public List<CategoryResponse> getCategoryByUserId(int id) {
        final List<CategoryEntity> categories = categoryDao.getByUserId(id);
        final List<CategoryResponse> result = categories.stream()
            .map(CategoryResponse::transform)
            .collect(Collectors.toList());
        return result;
    }

    public void saveCategory(CategoryCreateJson category) {
        categoryDao.save(CategoryEntity.transform(category));
    }

    public void updateCategory(CategoryUpdateJson category) {
        CategoryEntity entity = CategoryEntity.transform(category);
        entity.setCreatedAt(categoryDao.getById(entity.getId()).getCreatedAt());
        categoryDao.update(entity);
    }

    public void deleteById(int id) {
        CategoryEntity entity = categoryDao.getById(id);
        categoryDao.delete(entity);
    }

}

