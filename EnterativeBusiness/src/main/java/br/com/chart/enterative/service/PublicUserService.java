package br.com.chart.enterative.service;

import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.UserRole;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.ShopCRUDService;
import br.com.chart.enterative.service.crud.UserCRUDService;
import br.com.chart.enterative.service.crud.UserRoleCRUDService;
import br.com.chart.enterative.vo.ForgotPasswordVO;
import br.com.chart.enterative.vo.ServiceResponse;

import java.util.*;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
@Service
public class PublicUserService extends UserAwareComponent {

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private UserRoleCRUDService userRoleService;

    @Autowired
    private UserCRUDService userCRUDService;

    @Autowired
    private ShopCRUDService shopService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private HelperConverterService helperConverterService;

    @Transactional(rollbackFor = CRUDServiceException.class)
    public ServiceResponse confirm(String token) throws CRUDServiceException {
        ServiceResponse result = new ServiceResponse();

        if (Objects.isNull(token) || token.isEmpty()) {
            return result.setResponseCode(RESPONSE_CODE.E23);
        }

        User user = this.userDAO.findByToken(token);
        if (Objects.isNull(user)) {
            return result.setResponseCode(RESPONSE_CODE.E23);
        }
        user.setStatus(STATUS.ACTIVE);
        user.setToken(null);
        user = this.userDAO.saveAndFlush(user, this.systemUserId());

        return result.setResponseCode(RESPONSE_CODE.E00).put("entity", user);
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public ServiceResponse newCustomerUser(UserVO vo) throws CRUDServiceException {
        ServiceResponse result = new ServiceResponse();

        if (Objects.isNull(vo.getPassword()) || Objects.isNull(vo.getPasswordMatch()) || Objects.isNull(vo.getEmail()) || Objects.isNull(vo.getLogin())
                || Objects.isNull(vo.getName()) || Objects.isNull(vo.getEmailMatch()) || vo.getPassword().isEmpty() || vo.getPasswordMatch().isEmpty() || vo.getEmail().isEmpty()
                || vo.getLogin().isEmpty() || vo.getName().isEmpty() || vo.getEmailMatch().isEmpty()) {
            return result.setResponseCode(RESPONSE_CODE.E20);
        }
        if (!Objects.equals(vo.getPassword(), vo.getPasswordMatch())) {
            return result.setResponseCode(RESPONSE_CODE.E19);
        }

        if (!Objects.equals(vo.getEmail(), vo.getEmailMatch())) {
            return result.setResponseCode(RESPONSE_CODE.E27);
        }

        Shop defaultShop = this.shopService.findDefaultCustomerShop();
        if (Objects.isNull(defaultShop)) {
            return result.setResponseCode(RESPONSE_CODE.E21);
        }

        UserRole clientRole = this.userRoleService.findClientRole();
        if (Objects.isNull(clientRole)) {
            return result.setResponseCode(RESPONSE_CODE.E22);
        }

        UserRole balanceRole = this.userRoleService.findBalanceRole();
        if (Objects.isNull(balanceRole)) {
            return result.setResponseCode(RESPONSE_CODE.E30);
        }
        vo.setRoles(new ArrayList<>());
        vo.getRoles().add(this.userRoleService.converter().convert(clientRole));
        vo.getRoles().add(this.userRoleService.converter().convert(balanceRole));

        User entity = this.userCRUDService.converter().convert(vo);
        this.userCRUDService.fill(entity);

        entity.setShop(defaultShop);
        entity.setStatus(STATUS.INACTIVE);
        entity.setTerminal(this.parameterDAO.get(ENVIRONMENT_PARAMETER.DEFAULT_CUSTOMER_TERMINAL));
        entity.setToken(this.utils.generateConfirmationToken());

        Account account = this.accountService.create(entity);
        entity.setAccount(account);

        this.userCRUDService.criptografaSenha(entity);
        entity = this.userDAO.saveAndFlush(entity, this.systemUserId());
        vo = this.userCRUDService.converter().convert(entity);

        try {
            this.sendConfirmationEmail(vo);
        } catch (MessagingException e) {
            throw new CRUDServiceException(new ServiceResponse().setMessage(e.getLocalizedMessage()));
        }

        result.put("entity", vo);
        return result;
    }

    private void sendConfirmationEmail(UserVO vo) throws MessagingException {
        String subject = "user.emailconfirmation.subject";
        String from = this.parameterDAO.get(ENVIRONMENT_PARAMETER.DEFAULT_FROM_ADDRESS);
        String template = "mail/newUser";

        Map<String, Object> context = new HashMap<>();
        context.put("subject", subject);
        context.put("name", vo.getName());
        context.put("link", String.format("%s/%s", this.parameterDAO.get(ENVIRONMENT_PARAMETER.CUSTOMER_EMAILCONFIRMATION_LINK), vo.getToken()));

        String translatedUniqueSubject = String.format("%s - %s", this.helperConverterService.getMessage(subject), vo.getLogin());
        this.emailService.sendSimpleMail(context, translatedUniqueSubject, from, vo.getEmail(), this.locale(), template);
    }

    public ServiceResponse createForgotPasswordToken(ForgotPasswordVO vo) {
        User user = this.userDAO.findByLogin(vo.getLogin());

        if (Objects.nonNull(user)) {
            String email = user.getEmail();
            if (Objects.equals(email, vo.getEmail())) {
                String token = UUID.randomUUID().toString();
                user.setForgotPasswordToken(token);

                user = this.userDAO.saveAndFlush(user, this.systemUserId());
                try {
                    this.sendForgotPasswordEmail(user);
                } catch (MessagingException e) {
                    throw new CRUDServiceException(new ServiceResponse().setMessage(e.getLocalizedMessage()));
                }
            } else {
                throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E33));
            }
        } else {
            throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E33));
        }

        return new ServiceResponse().setResponseCode(RESPONSE_CODE.E00);
    }

    private void sendForgotPasswordEmail(User user) throws MessagingException {
        String subject = "user.forgotpassword.subject";
        String from = this.parameterDAO.get(ENVIRONMENT_PARAMETER.DEFAULT_FROM_ADDRESS);
        String template = "mail/forgotPassword";

        Map<String, Object> context = new HashMap<>();
        context.put("subject", subject);
        context.put("name", user.getName());
        context.put("link", String.format("%s/%s", this.parameterDAO.get(ENVIRONMENT_PARAMETER.CUSTOMER_FORGOTPASSWORD_LINK), user.getForgotPasswordToken()));

        String translatedUniqueSubject = String.format("%s - %s", this.helperConverterService.getMessage(subject), user.getLogin());
        this.emailService.sendSimpleMail(context, translatedUniqueSubject, from, user.getEmail(), this.locale(), template);
    }
}
