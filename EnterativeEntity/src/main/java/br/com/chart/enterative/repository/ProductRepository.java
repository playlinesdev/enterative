package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.PRODUCT_TYPE;
import br.com.chart.enterative.enums.STATUS;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author William Leite
 */
public interface ProductRepository extends UserAwareRepository<Product, Long> {

    public Page<Product> findByCategoryIdAndStatus(Long category, STATUS status, Pageable pageable);

    public Product findByEanStartingWith(String ean);

    public List<Product> findByNameAndCategoryId(String name, Long category);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.status = :status AND p.category.id = :category")
    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatus(@Param("name") String name, @Param("category") Long category, @Param("status") STATUS status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.status = :status")
    public Page<Product> findByNameIgnoreCaseContainingAndStatus(@Param("name") String name, @Param("status") STATUS status, Pageable pageable);

    public List<Product> findByStatus(STATUS status);

    public Page<Product> findByStatus(STATUS status, Pageable pageable);

    public List<Product> findByStatusOrderByName(STATUS status);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.status = :status AND p.type = :type AND p.category.id = :category ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("category") Long id, @Param("status") STATUS status, @Param("type") PRODUCT_TYPE type, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.status = :status AND p.category.id = :category ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("category") Long id, @Param("status") STATUS status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.type = :type AND p.category.id = :category ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("category") Long id, @Param("type") PRODUCT_TYPE type, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.category.id = :category ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("category") Long id, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.status = :status AND p.type = :type ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndTypeOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("status") STATUS status, @Param("type") PRODUCT_TYPE type, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.status = :status ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("status") STATUS status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.type = :type ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndTypeOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("type") PRODUCT_TYPE type, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingOrderByName(@Param("name") String name, @Param("ean") String ean, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.category.id = :category AND p.status = :status AND p.type = :type ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(@Param("name") String name, @Param("category") Long id, @Param("status") STATUS status, @Param("type") PRODUCT_TYPE type, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.category.id = :category AND p.status = :status ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(@Param("name") String name, @Param("category") Long id, @Param("status") STATUS status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.category.id = :category AND p.type = :type ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(@Param("name") String name, @Param("category") Long id, @Param("type") PRODUCT_TYPE type, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.category.id = :category ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdOrderByName(@Param("name") String name, @Param("category") Long id, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.status = :status AND p.type = :type ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndStatusAndTypeOrderByName(@Param("name") String name, @Param("status") STATUS status, @Param("type") PRODUCT_TYPE type, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.status = :status ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndStatusOrderByName(@Param("name") String name, @Param("status") STATUS status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.type = :type ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndTypeOrderByName(@Param("name") String name, @Param("type") PRODUCT_TYPE type, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(String ean, Long id, STATUS status, PRODUCT_TYPE type, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(String ean, Long id, STATUS status, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(String ean, Long id, PRODUCT_TYPE type, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdOrderByName(String ean, Long id, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndStatusAndTypeOrderByName(String ean, STATUS status, PRODUCT_TYPE type, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndStatusOrderByName(String ean, STATUS status, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndTypeOrderByName(String ean, PRODUCT_TYPE type, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingOrderByName(String ean, Pageable pageable);
    
    public Page<Product> findByStatusAndTypeOrderByName(STATUS status, PRODUCT_TYPE type, Pageable pageable);

    public Page<Product> findByStatusOrderByName(STATUS status, Pageable pageable);

    public Page<Product> findByCategoryIdAndStatusAndTypeOrderByName(Long id, STATUS status, PRODUCT_TYPE type, Pageable pageable);

    public Page<Product> findByCategoryIdAndStatusOrderByName(Long id, STATUS status, Pageable pageable);

    public Page<Product> findByCategoryIdAndTypeOrderByName(Long id, PRODUCT_TYPE type, Pageable pageable);

    public Page<Product> findByCategoryIdOrderByName(Long id, Pageable pageable);

    public Page<Product> findByTypeOrderByName(PRODUCT_TYPE type, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.status = :status AND p.type = :type AND p.category.id = :category AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("category") Long id, @Param("status") STATUS status, @Param("type") PRODUCT_TYPE type, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.status = :status AND p.category.id = :category AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("category") Long category, @Param("status") STATUS status, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.category.id = :category AND p.type = :type AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("category") Long category, @Param("type") PRODUCT_TYPE type, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.category.id = :category AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("category") Long category, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.status = :status AND p.type = :type AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("status") STATUS status, @Param("type") PRODUCT_TYPE type, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.status = :status AND p.category.id = :category AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("status") STATUS status, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.type = :type AND p.category.id = :category AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("type") PRODUCT_TYPE type, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.ean LIKE CONCAT('%',:ean,'%') AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndActivationProcessOrderByName(@Param("name") String name, @Param("ean") String ean, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.status = :status AND p.type = :type AND p.category.id = :category AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(@Param("name") String name, @Param("category") Long category, @Param("status") STATUS status, @Param("type") PRODUCT_TYPE type, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.status = :status AND p.category.id = :category AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(@Param("name") String name, @Param("category") Long category, @Param("status") STATUS status, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.category.id = :category AND p.type = :type AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(@Param("name") String name, @Param("category") Long category, @Param("type") PRODUCT_TYPE type, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.category.id = :category AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(@Param("name") String name, @Param("category") Long category, @Param("activationProcess") ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.status = :status AND p.type = :type AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(String name, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.status = :status AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(String name, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.type = :type AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(String name, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE CONCAT('%',:name,'%') OR p.name IS NULL OR p.displayName LIKE CONCAT('%',:name,'%') OR p.displayName IS NULL) AND p.activationProcess = :activationProcess ORDER BY p.name")
    public Page<Product> findByNameIgnoreCaseContainingAndActivationProcessOrderByName(String name, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(String ean, Long id, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(String ean, Long id, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(String ean, Long id, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(String ean, Long id, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(String ean, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(String ean, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(String ean, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByEanIgnoreCaseContainingAndActivationProcessOrderByName(String ean, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(Long id, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByCategoryIdAndStatusAndActivationProcessOrderByName(Long id, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByCategoryIdAndTypeAndActivationProcessOrderByName(Long id, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByCategoryIdAndActivationProcessOrderByName(Long id, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByStatusAndTypeAndActivationProcessOrderByName(STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByStatusAndActivationProcessOrderByName(STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByTypeAndActivationProcessOrderByName(PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable);

    public Page<Product> findByActivationProcessOrderByName(ACTIVATION_PROCESS activationProcess, Pageable pageable);
}
