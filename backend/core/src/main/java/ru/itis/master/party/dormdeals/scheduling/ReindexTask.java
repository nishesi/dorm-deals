package ru.itis.master.party.dormdeals.scheduling;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.mapper.ProductMapper;
import ru.itis.master.party.dormdeals.models.elasticsearch.ItemElastic;
import ru.itis.master.party.dormdeals.repositories.ItemElasticRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.ProductRepository;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReindexTask {
    private static final int PAGE_SIZE = 50_000;
    private final ProductRepository productRepository;
    private final ItemElasticRepository itemElasticRepository;
    private final ProductMapper productMapper;
    private final ExecutorService executorService = Executors.newFixedThreadPool(30);

    @Scheduled(fixedDelay = 12, timeUnit = TimeUnit.HOURS)
    @Transactional
    public void reindex() throws ExecutionException, InterruptedException {
        log.info("генерация индексов по товарам запущена");

        int lastInd = (int) (productRepository.count() / PAGE_SIZE + 1);
        var tasks = IntStream.range(0, lastInd)
                .mapToObj(i -> executorService.submit(reindexPageTask(i)))
                .toList();
        for (Future<?> f : tasks)
            f.get();

        log.info("генерация индексов по товарам закончилась");
    }

    private Runnable reindexPageTask(int i) {
        return () -> {
            List<ItemElastic> list = productRepository.findAll(PageRequest.of(i, PAGE_SIZE))
                    .stream().parallel()
                    .map(productMapper::toItemElastic)
                    .toList();
            itemElasticRepository.saveAll(list);
        };
    }
}
