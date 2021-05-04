package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.dao.UserDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.*;
import br.com.chart.enterative.entity.vo.PartnerVO;
import br.com.chart.enterative.entity.vo.UserRoleVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.converter.UserConverterService;
import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.vo.ChangePasswordVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.UserSearchVO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class UserCRUDService extends UserAwareCRUDService<User, Long, UserVO, UserSearchVO> implements UserDetailsService {

    public UserCRUDService(UserAwareDAO<User, Long> dao, ConverterService<User, UserVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserDAO dao() {
        return (UserDAO) super.dao();
    }

    public User findByIdWithFavoriteProductsEager(Long id) {
        return this.dao().findByIdWithFavoriteProductsEager(id);
    }

    public User findByLogin(String login) throws InstantiationException, IllegalAccessException {
        User userExample = new User();
        userExample.setLogin(login);
        return this.findOneOrReturnNull(userExample, new String[]{"login"});
    }

    @Override
    @Transactional(rollbackFor = {CRUDServiceException.class})
    public ServiceResponse processSave(UserVO vo, Long user) throws CRUDServiceException {
        if (Objects.nonNull(vo.getId())) {
            User db = this.dao().findOne(vo.getId());
            vo.setPassword(db.getPassword());
        } else if (Objects.nonNull(vo.getPassword()) && !vo.getPassword().isEmpty()) {
            if (!Objects.equals(vo.getPassword(), vo.getPasswordMatch())) {
                ServiceResponse result = new ServiceResponse().setMessage("Senhas diferem!");
                throw new CRUDServiceException(result);
            }
        } else {
            ServiceResponse result = new ServiceResponse().setMessage("Senha vazia!");
            throw new CRUDServiceException(result);
        }
        List<UserRoleVO> selectedRoles = vo.getRoles().stream().filter(UserRoleVO::isSelected).collect(Collectors.toList());
        vo.setRoles(selectedRoles);
        User entity = this.converter().convert(vo);
        this.fill(entity);

        if (Objects.isNull(vo.getId())) {
            this.criptografaSenha(entity);
        }
        entity = this.dao().saveAndFlush(entity, user);
        vo = this.converter().convert(entity);
        return new ServiceResponse().put("entity", vo);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetails retorno = this.dao().findByLogin(login);
        if (Objects.isNull(retorno)) {
            throw new UsernameNotFoundException("Usuário " + login + " não encontrado");
        }
        return retorno;
    }

    public void setTokenForID(String token, Long id) {
        this.dao().setTokenForID(token, id);
    }

    public void criptografaSenha(User user) {
        if (Objects.nonNull(user.getPassword())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
        }
    }

    @Override
    public UserVO initVO() {
        UserVO vo = super.initVO();
        vo.setPartner(null);
        vo.setRoles(new ArrayList<>());
        List<UserRoleVO> roles = ((UserConverterService) this.converter()).searchRoles(vo);
        vo.setRoles(roles);
        return vo;
    }

    public UserVO initVO(PartnerVO partner) {
        UserVO vo = this.initVO();
        vo.setPartner(partner);
        return vo;
    }

    public User findByToken(String token) {
        return this.dao().findByToken(token);
    }

    public List<User> findBySituacaoOrderByName(STATUS status) {
        return this.dao().findByStatusOrderByName(status);
    }

    public void delete(UserVO user) {
        this.dao().delete(this.converter().convert(user));
    }

    public String generateToken(String login) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String random1 = String.valueOf(new Random().nextLong());
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String random2 = String.valueOf(new Random().nextLong());
        String user = login;
        String random3 = String.valueOf(new Random().nextLong());

        StringBuilder raw = new StringBuilder();
        raw.append(random1).append(date).append(random2).append(user).append(random3);
        return encoder.encode(raw.toString());
    }

    public ServiceResponse changePassword(String token) {
        User user = this.userDAO.findByForgotPasswordToken(token);
        if (Objects.nonNull(user)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode("123456"));
            user.setForgotPasswordToken(null);
            this.userDAO.saveAndFlush(user, this.systemUserId());
        } else {
            throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E34));
        }
        return new ServiceResponse().setResponseCode(RESPONSE_CODE.E00);
    }

    public ServiceResponse changePassword(ChangePasswordVO vo, Long id) {
        if (!Objects.equals(vo.getNewPassword(), vo.getConfirmNewPassword())) {
            return new ServiceResponse().setMessage("Nova senha e confirmação não são iguais!");
        }

        User user = this.dao().findOne(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(vo.getOldPassword(), user.getPassword())) {
            return new ServiceResponse().setMessage("Senha antiga não corresponde a senha atual!");
        }

        String newPassword = encoder.encode(vo.getNewPassword());
        this.dao().setPasswordForID(newPassword, id);
        return new ServiceResponse().setMessage(null);
    }

    @Override
    protected Supplier<User> initEntitySupplier() {
        return User::new;
    }

    @Override
    protected Supplier<UserVO> initVOSupplier() {
        return UserVO::new;
    }

    @Override
    public ServiceResponse validate(UserVO vo, CRUD_OPERATION operation) {
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
    public PageWrapper<UserVO> retrieve(UserSearchVO searchForm, Pageable pageable, String url) {
        String name = searchForm.getName();
        String login = searchForm.getLogin();
        String email = searchForm.getEmail();

        Specification<User> specification = this.assembleSpecification(name, login, email);
        Page<User> page = this.dao().repository().findAll(specification, pageable);
        return new PageWrapper<>(page.map(this.converter()::convert), url);
    }

    public Specification<User> assembleSpecification(final String name, final String login, final String email) {
        return (Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(name)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
            }
            if (Objects.nonNull(login)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("login"), "%" + login + "%")));
            }
            if (Objects.nonNull(email)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("email"), "%" + email + "%")));
            }

            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
