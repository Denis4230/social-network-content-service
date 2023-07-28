package kata.academy.eurekacontentservice.feign.fallback;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class LikeServiceFallbackFactory implements FallbackFactory<LikeServiceFeignClient> {

    @Override
    public LikeServiceFeignClient create(Throwable cause) {
        return new LikeServiceFallback(cause);
    }
}
