package api.vibes.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}

    /**\/ regras de tipos de usuários;
     * permissões para usuários;
     */
    public static enum roles {
        ROLE_ROOT,/* << root; */
        ROLE_ADMIN,/* << admin; */
        ROLE_USER,/* << usuário; */
    }
}
