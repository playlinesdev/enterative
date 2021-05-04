package br.com.chart.enterative.service.activation;

import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.vo.WSRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 * @author Andre Fontoura
 *
 * Classe responsavel por validar os dados do Json antes de enviar para a bhn
 */
@Service
public class RequestValidationService {

    @Autowired
    private ProductDAO productDAO;

    public RESPONSE_CODE structural(WSRequest solicitacao, User user, boolean validarBarcode) {
        RESPONSE_CODE result = this.structural(solicitacao, user);
        if (!validarBarcode && result == RESPONSE_CODE.E06) {
            result = RESPONSE_CODE.E00;
        }
        return result;
    }

    public RESPONSE_CODE structural(WSRequest request, User user) {
        RESPONSE_CODE result;

        if (Objects.isNull(user)) {
            result = RESPONSE_CODE.E01;
        } else if (!user.isEnabled()) {
            result = RESPONSE_CODE.E02;
        } else if (Objects.isNull(request)) {
            result = RESPONSE_CODE.E03;
        } else {
            result = this.shop(user.getShop(), request.getShop());
            if (result == RESPONSE_CODE.E00) {
                result = this.merchant(user.getShop().getMerchant(), request.getMerchant());
            }
        }

        if (result == RESPONSE_CODE.E00) {
            result = request.getLines().stream().map(l -> {
                if (Objects.isNull(l.getExternalCode()) || l.getExternalCode().isEmpty()) {
                    return RESPONSE_CODE.E05;
                } else if (Objects.isNull(l.getBarcode()) || l.getBarcode().length() != 32) {
                    return RESPONSE_CODE.E06;
                } else if (l.getBarcode().charAt(12) != '0') {
                    return RESPONSE_CODE.E06;
                } else if (Objects.isNull(l.getActivationProcess())) {
                    return RESPONSE_CODE.E32;
                } else {
                    return RESPONSE_CODE.E00;
                }
            }).filter(r -> r != RESPONSE_CODE.E00).findFirst().orElse(RESPONSE_CODE.E00);
        }

        return result;
    }

    private RESPONSE_CODE shop(Shop shopUser, String shop) {
        RESPONSE_CODE result;

        if (Objects.nonNull(shopUser)) {
            String idShop = shopUser.getCode();
            if (Objects.equals(idShop, shop)) {
                if (shopUser.getStatus() == STATUS.ACTIVE) {
                    result = RESPONSE_CODE.E00;
                } else {
                    result = RESPONSE_CODE.E11;
                }
            } else {
                result = RESPONSE_CODE.E11;
            }
        } else {
            result = RESPONSE_CODE.E11;
        }

        return result;
    }

    private RESPONSE_CODE merchant(Merchant merchantUser, String merchant) {
        RESPONSE_CODE result = RESPONSE_CODE.E00;

        if (Objects.nonNull(merchantUser)) {
            String idMerchant = merchantUser.getMerchantIdentifier();
            if (Objects.nonNull(idMerchant) && !idMerchant.isEmpty()) {
                if (!Objects.equals(idMerchant, merchant)) {
                    result = RESPONSE_CODE.E04;
                } else if (Objects.isNull(merchantUser.getMerchantLocation()) || merchantUser.getMerchantLocation().isEmpty()) {
                    result = RESPONSE_CODE.E13;
                } else if (Objects.isNull(merchantUser.getCategory())) {
                    result = RESPONSE_CODE.E14;
                } else if (Objects.isNull(merchantUser.getCategory().getCode()) || merchantUser.getCategory().getCode().isEmpty()) {
                    result = RESPONSE_CODE.E15;
                } else if (merchantUser.getStatus() != STATUS.ACTIVE) {
                    result = RESPONSE_CODE.E04;
                }

            } else {
                result = RESPONSE_CODE.E12;
            }
        } else {
            result = RESPONSE_CODE.E04;
        }

        return result;
    }

    public RESPONSE_CODE consistency(WSRequest request) {
        return request.getLines().stream().map(l -> {
            if (Objects.nonNull(l.getBarcode())) {
                String ean = l.getBarcode().substring(0, 12);
                Product product = this.productDAO.findByEanStartingWith(ean);

                if (Objects.nonNull(product)) {
                    if (product.getStatus() == STATUS.ACTIVE) {
                        if (Objects.isNull(product.getActivationProcess())) {
                            return RESPONSE_CODE.E32;
                        } else switch (product.getActivationProcess()) {
                            case BHN:
                                try {
                                    String.format("%012.0f", (product.getAmount().doubleValue() * 100));
                                    return RESPONSE_CODE.E00;
                                } catch (Exception e) {
                                    return RESPONSE_CODE.E08;
                                }
                            case EPAY:
                                if (Objects.isNull(l.getEpay())) {
                                    return RESPONSE_CODE.E07;
                                } else if (Objects.isNull(l.getEpay().getAreaCode()) || l.getEpay().getAreaCode().isEmpty()) {
                                    return RESPONSE_CODE.E07;
                                } else if (Objects.isNull(l.getEpay().getOperator()) || l.getEpay().getOperator().isEmpty()) {
                                    return RESPONSE_CODE.E07;
                                } else if (Objects.isNull(l.getEpay().getPhone()) || l.getEpay().getPhone().isEmpty()) {
                                    return RESPONSE_CODE.E07;
                                } else if (Objects.isNull(l.getEpay().getProductId()) || l.getEpay().getProductId().isEmpty()) {
                                    return RESPONSE_CODE.E07;
                                } else {
                                    return RESPONSE_CODE.E00;
                                }
                            default:
                                return RESPONSE_CODE.E32;
                        }
                    } else {
                        return RESPONSE_CODE.E07;
                    }
                } else {
                    return RESPONSE_CODE.E07;
                }
            } else {
                return RESPONSE_CODE.E07;
            }
        }).filter(r -> r != RESPONSE_CODE.E00).findFirst().orElse(RESPONSE_CODE.E00);
    }
}
