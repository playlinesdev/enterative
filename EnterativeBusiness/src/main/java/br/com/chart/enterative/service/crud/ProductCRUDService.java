package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.ProductConverterService;
import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.FileUploadDAO;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.entity.FileUpload;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShopProductCommission;
import br.com.chart.enterative.enums.FILE_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.repository.ShopProductCommissionRepository;
import br.com.chart.enterative.repository.ShopRepository;
import br.com.chart.enterative.vo.search.ProductSearchVO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductCRUDService extends UserAwareCRUDService<Product, Long, ProductVO, ProductSearchVO> {

    @Autowired
    private ShopProductCommissionRepository shopProductCommissionRepository;

    @Autowired
    private ShopRepository shopRepository;

    public ProductCRUDService(UserAwareDAO<Product, Long> dao, ConverterService<Product, ProductVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProductDAO dao() {
        return (ProductDAO) super.dao();
    }

    @Override
    public ProductConverterService converter() {
        return super.converter();
    }

    @Override
    public PageWrapper<ProductVO> retrieve(ProductSearchVO searchForm, Pageable pageable, String url) {
        Page<Product> page;
        if (Objects.nonNull(searchForm.getName())) {
            if (Objects.nonNull(searchForm.getEan())) {
                if (Objects.nonNull(searchForm.getCategory())) {
                    if (Objects.nonNull(searchForm.getStatus())) {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page =  this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page =  this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getStatus(), pageable);
                            }
                        }
                    } else {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndCategoryIdOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getCategory().getId(), pageable);
                            }
                        }
                    }
                } else {
                    if (Objects.nonNull(searchForm.getStatus())) {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getStatus(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndTypeOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getStatus(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getStatus(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndStatusOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getStatus(), pageable);
                            }
                        }
                    } else {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndTypeOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingAndActivationProcessOrderByName(searchForm.getName(), searchForm.getEan(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndEanIgnoreCaseContainingOrderByName(searchForm.getName(), searchForm.getEan(), pageable);
                            }
                        }
                    }
                }
            } else {
                if (Objects.nonNull(searchForm.getCategory())) {
                    if (Objects.nonNull(searchForm.getStatus())) {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(searchForm.getName(), searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(searchForm.getName(), searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(searchForm.getName(), searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(searchForm.getName(), searchForm.getCategory().getId(), searchForm.getStatus(), pageable);
                            }
                        }
                    } else {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(searchForm.getName(), searchForm.getCategory().getId(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(searchForm.getName(), searchForm.getCategory().getId(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(searchForm.getName(), searchForm.getCategory().getId(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndCategoryIdOrderByName(searchForm.getName(), searchForm.getCategory().getId(), pageable);
                            }
                        }
                    }
                } else {
                    if (Objects.nonNull(searchForm.getStatus())) {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(searchForm.getName(), searchForm.getStatus(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndStatusAndTypeOrderByName(searchForm.getName(), searchForm.getStatus(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(searchForm.getName(), searchForm.getStatus(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndStatusOrderByName(searchForm.getName(), searchForm.getStatus(), pageable);
                            }
                        }
                    } else {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(searchForm.getName(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingAndTypeOrderByName(searchForm.getName(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByNameIgnoreCaseContainingAndActivationProcessOrderByName(searchForm.getName(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByNameIgnoreCaseContainingOrderByName(searchForm.getName(), pageable);
                            }
                        }
                    }
                }
            }
        } else {
            if (Objects.nonNull(searchForm.getEan())) {
                if (Objects.nonNull(searchForm.getCategory())) {
                    if (Objects.nonNull(searchForm.getStatus())) {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndTypeOrderByName(searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByEanIgnoreCaseContainingAndCategoryIdAndStatusAndActivationProcessOrderByName(searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByEanIgnoreCaseContainingAndCategoryIdAndStatusOrderByName(searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getStatus(), pageable);
                            }
                        }
                    } else {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByEanIgnoreCaseContainingAndCategoryIdAndTypeAndActivationProcessOrderByName(searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getType(), searchForm.getActivationProcess(), pageable);   
                            } else {
                                page = this.dao().findByEanIgnoreCaseContainingAndCategoryIdAndTypeOrderByName(searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getType(), pageable);   
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByEanIgnoreCaseContainingAndCategoryIdAndActivationProcessOrderByName(searchForm.getEan(), searchForm.getCategory().getId(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByEanIgnoreCaseContainingAndCategoryIdOrderByName(searchForm.getEan(), searchForm.getCategory().getId(), pageable);
                            }
                        }
                    }
                } else {
                    if (Objects.nonNull(searchForm.getStatus())) {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByEanIgnoreCaseContainingAndStatusAndTypeAndActivationProcessOrderByName(searchForm.getEan(), searchForm.getStatus(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByEanIgnoreCaseContainingAndStatusAndTypeOrderByName(searchForm.getEan(), searchForm.getStatus(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByEanIgnoreCaseContainingAndStatusAndActivationProcessOrderByName(searchForm.getEan(), searchForm.getStatus(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByEanIgnoreCaseContainingAndStatusOrderByName(searchForm.getEan(), searchForm.getStatus(), pageable);
                            }
                        }
                    } else {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByEanIgnoreCaseContainingAndTypeAndActivationProcessOrderByName(searchForm.getEan(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByEanIgnoreCaseContainingAndTypeOrderByName(searchForm.getEan(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByEanIgnoreCaseContainingAndActivationProcessOrderByName(searchForm.getEan(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByEanIgnoreCaseContainingOrderByName(searchForm.getEan(), pageable);
                            }
                        }
                    }
                }
            } else {
                if (Objects.nonNull(searchForm.getCategory())) {
                    if (Objects.nonNull(searchForm.getStatus())) {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByCategoryIdAndStatusAndTypeAndActivationProcessOrderByName(searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByCategoryIdAndStatusAndTypeOrderByName(searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByCategoryIdAndStatusAndActivationProcessOrderByName(searchForm.getCategory().getId(), searchForm.getStatus(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByCategoryIdAndStatusOrderByName(searchForm.getCategory().getId(), searchForm.getStatus(), pageable);
                            }
                        }
                    } else {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByCategoryIdAndTypeAndActivationProcessOrderByName(searchForm.getCategory().getId(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByCategoryIdAndTypeOrderByName(searchForm.getCategory().getId(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByCategoryIdAndActivationProcessOrderByName(searchForm.getCategory().getId(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByCategoryIdOrderByName(searchForm.getCategory().getId(), pageable);
                            }
                        }
                    }
                } else {
                    if (Objects.nonNull(searchForm.getStatus())) {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByStatusAndTypeAndActivationProcessOrderByName(searchForm.getStatus(), searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByStatusAndTypeOrderByName(searchForm.getStatus(), searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByStatusAndActivationProcessOrderByName(searchForm.getStatus(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByStatusOrderByName(searchForm.getStatus(), pageable);
                            }
                        }
                    } else {
                        if (Objects.nonNull(searchForm.getType())) {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByTypeAndActivationProcessOrderByName(searchForm.getType(), searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findByTypeOrderByName(searchForm.getType(), pageable);
                            }
                        } else {
                            if (Objects.nonNull(searchForm.getActivationProcess())) {
                                page = this.dao().findByActivationProcessOrderByName(searchForm.getActivationProcess(), pageable);
                            } else {
                                page = this.dao().findAll(pageable);
                            }
                        }
                    }
                }
            }
        }

        return new PageWrapper<>(page.map(this.converter()::convert), url);
    }

    public Page<Product> findByCategoryIdAndStatus(Long category, STATUS status, Pageable pageable) {
        return this.dao().findByCategoryIdAndStatus(category, status, pageable);
    }

    public List<Product> findByNameAndCategoryId(String name, Long category) {
        return this.dao().findByNameAndCategoryId(name, category);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndStatus(String name, STATUS status, Pageable pageable) {
        return this.dao().findByNameIgnoreCaseContainingAndStatus(name, status, pageable);
    }

    public Page<Product> findByNameIgnoreCaseContainingAndCategoryIdAndStatus(String name, Long category, STATUS status, Pageable pageable) {
        return this.dao().findByNameIgnoreCaseContainingAndCategoryIdAndStatus(name, category, status, pageable);
    }

    public List<ProductVO> findByNameAndCategoryIdVO(String name, Long category) {
        return this.findByNameAndCategoryId(name, category).stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    public List<Product> findByStatus(STATUS status) {
        return this.dao().findByStatus(status);
    }

    public Page<Product> findByStatus(STATUS status, Pageable pageable) {
        return this.dao().findByStatus(status, pageable);
    }

    public List<ProductVO> findByStatusVO(STATUS status) {
        return this.findByStatus(status).stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    public List<Product> findByStatusOrderByName(STATUS status) {
        return this.dao().findByStatusOrderByName(status);
    }

    public List<ProductVO> findByStatusOrderByNameVO(STATUS status) {
        return this.findByStatusOrderByName(status).stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    public ServiceResponse propagateCommission(ProductVO product) {
        try {
            ServiceResponse response = new ServiceResponse();

            this.shopProductCommissionRepository.deleteByProductId(product.getId());

            List<Shop> shops = this.shopRepository.findByStatusOrderByName(STATUS.ACTIVE);
            shops.stream().forEach(s -> {
                ShopProductCommission spc = new ShopProductCommission();
                spc.setProduct(new Product());
                spc.getProduct().setId(product.getId());
                spc.setPercentage(product.getPropagateCommission().divide(new BigDecimal("100")));
                spc.setShop(s);
                this.shopProductCommissionRepository.saveAndFlush(spc);
            });

            return response;
        } catch (Exception e) {
            throw new CRUDServiceException(new ServiceResponse().handleException(e));
        }
    }

    @Override
    public ServiceResponse validate(ProductVO vo, CRUD_OPERATION operation) {
        ServiceResponse response = new ServiceResponse();
        switch (operation) {
            case CREATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case DELETE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case READ:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case UPDATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
        }
        return response;
    }

    @Override
    protected Supplier<Product> initEntitySupplier() {
        return Product::new;
    }

    @Override
    protected Supplier<ProductVO> initVOSupplier() {
        return ProductVO::new;
    }
}
