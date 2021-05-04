package br.com.chart.enterative.enums;

public enum USER_ROLE {
    ROLE_ADMIN("ROLE_ADMIN", "ADMIN", "Administrador"),
    ROLE_CUSTOMER("ROLE_CUSTOMER", "CUSTOMER", "Fluxo de Consumidor"),
    ROLE_BHN("ROLE_BHN", "BHN", "Fluxo Ativação direta BHN"),
    ROLE_ENTERATIVE_BALANCE("ROLE_ENTERATIVE_BALANCE", "ENTERATIVE_BALANCE", "Fluxo Ativação Saldo"),
    ROLE_ENTERATIVE_CREDIT("ROLE_ENTERATIVE_CREDIT", "ENTERATIVE_CREDIT", "Fluxo Ativação Crédito"),
    ROLE_FAST_ACTIVATION("ROLE_FAST_ACTIVATION", "FAST_ACTIVATION", "Fluxo Ativação Rápido"),
    ROLE_SHOP_ADMIN("ROLE_SHOP_ADMIN", "SHOP_ADMIN", "Administrador da Loja"),
    ROLE_PARTNER_CUSTOMER("ROLE_PARTNER_CUSTOMER", "PARTNER_CUSTOMER", "Consumidor vinculado a Parceiro"),
    ROLE_CHART_SUPPORT("ROLE_CHART_SUPPORT", "CHART_SUPPORT", "Suporte Chart"),
    ROLE_PAYGO_INTEGRATOR("ROLE_PAYGO_INTEGRATOR", "PAYGO_INTEGRATOR", "Api de Integração com a PayGo");

    private final String fullRole;
    private final String role;
    private final String description;

    USER_ROLE(String fullRole, String role, String description) {
        this.fullRole = fullRole;
        this.role = role;
        this.description = description;
    }

    public String getFullRole() {
        return fullRole;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }
}
