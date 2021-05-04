package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.PRODUCT_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.repository.ProductRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductDAO extends UserAwareDAO<Product, Long> {

    public ProductDAO(BaseRepository<Product, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProductRepository repository() {
        return (ProductRepository) super.repository();
    }

    public Page<Product> findByCategoryIdAndStatus(Long category, STATUS status, Pageable pageable) {
        return this.repository().findByCategoryIdAndStatus(category, status, pageable);
    }

    public List<Product> findByNameAndCategoryId(String name, Long category) {
        return this.repository().findByNameAndCategoryId(name, category);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndStatus(String name, STATUS status, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndStatus(name, status, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatus(String name, Long category, STATUS status, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCategoryIdAndStatus(name, category, status, pageable);
    }

    public List<Product> findByStatus(STATUS status) {
        return this.repository().findByStatus(status);
    }

    public Page<Product> findByStatus(STATUS status, Pageable pageable) {
        return this.repository().findByStatus(status, pageable);
    }

    public List<Product> findByStatusOrderByName(STATUS status) {
        return this.repository().findByStatusOrderByName(status);
    }

    public Product findByEanStartingWith(String ean) {
        return this.repository().findByEanStartingWith(ean);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(String name, String ean, Long id, STATUS status, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(name, ean, id, status, type, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(String name, String ean, Long id, STATUS status, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(name, ean, id, status, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(String name, String ean, Long id, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(name, ean, id, type, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdOrderByName(String name, String ean, Long id, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdOrderByName(name, ean, id, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndTypeOrderByName(String name, String ean, STATUS status, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndTypeOrderByName(name, ean, status, type, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusOrderByName(String name, String ean, STATUS status, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusOrderByName(name, ean, status, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndTypeOrderByName(String name, String ean, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndTypeOrderByName(name, ean, type, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingOrderByName(String name, String ean, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingOrderByName(name, ean, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(String name, Long id, STATUS status, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(name, id, status, type, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(String name, Long id, STATUS status, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(name, id, status, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(String name, Long id, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(name, id, type, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdOrderByName(String name, Long id, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCategoryIdOrderByName(name, id, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndStatusAndTypeOrderByName(String name, STATUS status, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndStatusAndTypeOrderByName(name, status, type, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndStatusOrderByName(String name, STATUS status, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndStatusOrderByName(name, status, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndTypeOrderByName(String name, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndTypeOrderByName(name, type, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(String ean, Long id, STATUS status, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(ean, id, status, type, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(String ean, Long id, STATUS status, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(ean, id, status, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(String ean, Long id, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(ean, id, type, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdOrderByName(String ean, Long id, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndCategoryIdOrderByName(ean, id, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndStatusAndTypeOrderByName(String ean, STATUS status, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndStatusAndTypeOrderByName(ean, status, type, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndStatusOrderByName(String ean, STATUS status, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndStatusOrderByName(ean, status, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndTypeOrderByName(String ean, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndTypeOrderByName(ean, type, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingOrderByName(String ean, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingOrderByName(ean, pageable);
    }

    public Page<Product> findByCategoryIdAndStatusAndTypeOrderByName(Long id, STATUS status, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByCategoryIdAndStatusAndTypeOrderByName(id, status, type, pageable);
    }

    public Page<Product> findByCategoryIdAndStatusOrderByName(Long id, STATUS status, Pageable pageable) {
        return this.repository().findByCategoryIdAndStatusOrderByName(id, status, pageable);
    }

    public Page<Product> findByCategoryIdAndTypeOrderByName(Long id, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByCategoryIdAndTypeOrderByName(id, type, pageable);
    }

    public Page<Product> findByCategoryIdOrderByName(Long id, Pageable pageable) {
        return this.repository().findByCategoryIdOrderByName(id, pageable);
    }

    public Page<Product> findByStatusAndTypeOrderByName(STATUS status, PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByStatusAndTypeOrderByName(status, type, pageable);
    }

    public Page<Product> findByStatusOrderByName(STATUS status, Pageable pageable) {
        return this.repository().findByStatusOrderByName(status, pageable);
    }

    public Page<Product> findByTypeOrderByName(PRODUCT_TYPE type, Pageable pageable) {
        return this.repository().findByTypeOrderByName(type, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(String name, String ean, Long id, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(name, ean, id, status, type, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(String name, String ean, Long id, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(name, ean, id, status, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(String name, String ean, Long id, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(name, ean, id, type, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(String name, String ean, Long id, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(name, ean, id, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(String name, String ean, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(name, ean, status, type, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(String name, String ean, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(name, ean, status, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(String name, String ean, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(name, ean, type, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndActivationProcessOrderByName(String name, String ean, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndActivationProcessOrderByName(name, ean, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(String name, Long id, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(name, id, status, type, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(String name, Long id, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(name, id, status, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(String name, Long id, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(name, id, type, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(String name, Long id, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(name, id, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(String name, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(name, status, type, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(String name, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(name, status, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(String name, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(name, type, activationProcess, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndActivationProcessOrderByName(String name, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndActivationProcessOrderByName(name, activationProcess, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(String ean, Long id, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(ean, id, status, type, activationProcess, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(String ean, Long id, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(ean, id, status, activationProcess, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(String ean, Long id, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(ean, id, type, activationProcess, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(String ean, Long id, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(ean, id, activationProcess, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(String ean, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(ean, status, type, activationProcess, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(String ean, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(ean, status, activationProcess, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(String ean, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(ean, type, activationProcess, pageable);
    }

    public Page<Product> findByEanIgnoreCaseContainingAndActivationProcessOrderByName(String ean, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByEanIgnoreCaseContainingAndActivationProcessOrderByName(ean, activationProcess, pageable);
    }

    public Page<Product> findByCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(Long id, STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(id, status, type, activationProcess, pageable);
    }

    public Page<Product> findByCategoryIdAndStatusAndActivationProcessOrderByName(Long id, STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByCategoryIdAndStatusAndActivationProcessOrderByName(id, status, activationProcess, pageable);
    }

    public Page<Product> findByCategoryIdAndTypeAndActivationProcessOrderByName(Long id, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByCategoryIdAndTypeAndActivationProcessOrderByName(id, type, activationProcess, pageable);
    }

    public Page<Product> findByCategoryIdAndActivationProcessOrderByName(Long id, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByCategoryIdAndActivationProcessOrderByName(id, activationProcess, pageable);
    }

    public Page<Product> findByStatusAndTypeAndActivationProcessOrderByName(STATUS status, PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByStatusAndTypeAndActivationProcessOrderByName(status, type, activationProcess, pageable);
    }

    public Page<Product> findByStatusAndActivationProcessOrderByName(STATUS status, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByStatusAndActivationProcessOrderByName(status, activationProcess, pageable);
    }

    public Page<Product> findByTypeAndActivationProcessOrderByName(PRODUCT_TYPE type, ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByTypeAndActivationProcessOrderByName(type, activationProcess, pageable);
    }

    public Page<Product> findByActivationProcessOrderByName(ACTIVATION_PROCESS activationProcess, Pageable pageable) {
        return this.repository().findByActivationProcessOrderByName(activationProcess, pageable);
    }
}
