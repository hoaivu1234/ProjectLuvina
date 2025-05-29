package com.luvina.la;

import com.luvina.la.config.Constants;
import com.luvina.la.config.DefaultProfileUtil;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Lớp khởi chạy chính của ứng dụng Spring Boot.
 * <p>
 * Cài đặt {@link InitializingBean} để thực hiện kiểm tra sau khi các thuộc tính đã được thiết lập,
 * nhằm đảm bảo không cấu hình sai khi chạy đồng thời cả hai profile 'dev' và 'prod'.
 * </p>
 *
 * <p>
 * Chức năng chính:
 * <ul>
 *     <li>Khởi tạo và chạy ứng dụng Spring Boot.</li>
 *     <li>Kiểm tra cấu hình các profile đang hoạt động.</li>
 *     <li>Ghi log thông tin khởi động của ứng dụng (địa chỉ URL truy cập, profile, context path,...).</li>
 * </ul>
 * </p>
 *
 * @author
 */
@SpringBootApplication
public class MainApplication implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    private final Environment env;

    /**
     * Hàm khởi tạo, nhận vào đối tượng {@link Environment} của Spring,
     * dùng để lấy thông tin cấu hình và các profile đang hoạt động.
     *
     * @param env môi trường Spring chứa thông tin cấu hình
     */
    public MainApplication(Environment env) {
        this.env = env;
    }

    /**
     * Hàm được gọi sau khi các thuộc tính đã được thiết lập.
     * <p>
     * Kiểm tra nếu cả hai profile 'dev' (phát triển) và 'prod' (sản xuất) đang cùng hoạt động,
     * thì ghi log lỗi vì đây là một cấu hình sai nghiêm trọng.
     * </p>
     *
     * @throws Exception nếu phát hiện cấu hình sai
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT)
                && activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run "
                    + "with both the 'dev' and 'prod' profiles at the same time.");
        }
    }

    /**
     * Phương thức main khởi chạy ứng dụng Spring Boot.
     *
     * @param args các đối số dòng lệnh khi khởi động ứng dụng
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MainApplication.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    /**
     * Ghi log các thông tin khởi động như:
     * - Tên ứng dụng
     * - Địa chỉ truy cập nội bộ và bên ngoài
     * - Các profile đang hoạt động
     *
     * @param env đối tượng môi trường của Spring chứa thông tin cấu hình
     */
    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (!StringUtils.hasText(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        String[] profile = env.getActiveProfiles();
        if (profile.length == 0) {
            profile = env.getDefaultProfiles();
        }

        String textBlock = """
                
                ----------------------------------------------------------
                Application '{}' is running! Access URLs:
                Local: \t\t{}://localhost:{}{}
                External: \t{}://{}:{}{}
                Profile(s): {}
                ----------------------------------------------------------
                
                """;

        log.info(textBlock, env.getProperty("spring.application.name"),
                protocol, serverPort, contextPath,
                protocol, hostAddress, serverPort, contextPath, profile);
    }
}
