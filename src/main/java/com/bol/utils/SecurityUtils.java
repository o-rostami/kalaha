package com.bol.utils;


import javax.annotation.PostConstruct;

public class SecurityUtils {

    private static final String TOKEN_KEY = "access_token";

    @PostConstruct
    private void init() {

    }

//    public static String getCurrentUsername() {
//        return getCurrentUser().getUsername();
//    }

    public static Long getCurrentUserId() {
//        return getCurrentUser().getUserId();
        return -1L;
    }

//    public static List<Long> getCurrentUserRoles() {
//        return getCurrentUser().getRoles();
//    }

//    private static CurrentUserDto getCurrentUser() {
//        if (isFakeUserEnable()) return getFakeUser();
//        CurrentUserDto result = CurrentUserDto.builder().build();
//        String currentUserToken = getCurrentUserToken();
//        if (StringUtils.isNotEmpty(currentUserToken)) {
//            //@todo autowire security service and get user
//        }
//        return result;
//    }

//    private static String getCurrentUserToken() {
//        String token = StringUtils.EMPTY;
//        ServletRequestAttributes request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (request != null) {
//            if (ArrayUtils.isNotEmpty(request.getRequest().getCookies())) {
//                Cookie accessToken = Arrays.stream(request.getRequest().getCookies())
//                        .filter(cookie -> cookie.getName().equals(TOKEN_KEY))
//                        .findFirst().orElse(null);
//                token = accessToken != null ? accessToken.getValue() : StringUtils.EMPTY;
//            }
//        }
//        return token;
//    }

//    public static CurrentUserDto getFakeUser() {
//        return CurrentUserDto.builder()
//                .userId(-1L)
//                .username("no_user")
//                .roles(new ArrayList<Long>() {{
//                    add(-1L);
//                }})
//                .build();
//    }

    /**
     * @todo get from setting
     */
    public static boolean isFakeUserEnable() {
        return true;
    }
}
