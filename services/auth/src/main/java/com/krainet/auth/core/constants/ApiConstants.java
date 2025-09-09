package com.krainet.auth.core.constants;

public final class ApiConstants {
    public static final String API_PREFIX_V1 = "/api/v1";

    public static class Account {
        public static final String API_ACCOUNT_CONTROLLER_NAME = "Accounts";
        public static final String API_ACCOUNT_BY_ID = "/account/{id}";
        public static final String API_ACCOUNT_CREATE_UPDATE = "/account";
        public static final String API_ACCOUNT_LIST= "/accounts";
        public static final String API_ACCOUNT_ROLE_UPDATE = "/account/role";
    }

    public static class Authorization{
        public static final String API_AUTHORIZATION_CONTROLLER_NAME = "Authorization";
        public static final String API_AUTHORIZATION_LOGIN = "/auth/login";
        public static final String API_AUTHORIZATION_REFRESH = "/auth/refresh";
        public static final String API_AUTHORIZATION_LOGOUT = "/auth/logout";
    }
}