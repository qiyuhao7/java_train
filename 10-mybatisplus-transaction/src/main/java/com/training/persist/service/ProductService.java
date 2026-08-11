package com.training.persist.service;

import com.training.persist.entity.Product;
import com.training.persist.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 商品服务：演示事务与乐观锁
 */
@Service
@Slf4j
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /**
     * 扣减库存（乐观锁）
     * 失败自动重试，最多3次
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deductStock(Long productId, int quantity) {
        int maxRetry = 3;
        for (int i = 0; i < maxRetry; i++) {
            Product product = productMapper.selectById(productId);
            if (product == null) {
                throw new RuntimeException("商品不存在: " + productId);
            }
            if (product.getStock() < quantity) {
                throw new RuntimeException("库存不足: 当前=" + product.getStock() + ", 需要=" + quantity);
            }

            product.setStock(product.getStock() - quantity);
            int rows = productMapper.updateById(product);  // 自动带 version 条件

            if (rows > 0) {
                log.info("库存扣减成功: productId={}, 扣减={}, 剩余={}", productId, quantity, product.getStock());
                return true;
            }
            log.warn("乐观锁冲突，重试 {}/{}", i + 1, maxRetry);
        }
        throw new RuntimeException("库存扣减失败：并发冲突超过重试次数");
    }

    /**
     * 创建商品（演示事务）
     */
    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(String name, String category, BigDecimal price, int stock) {
        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);
        product.setVersion(0);
        product.setIsDeleted(0);
        productMapper.insert(product);
        log.info("商品创建成功: id={}, name={}", product.getId(), name);
        return product;
    }

    /**
     * 只读查询（readOnly 优化）
     */
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    /**
     * 演示事务失效：自调用
     * ⚠️ 这个方法内的 @Transactional 不会生效
     */
    public void selfCallDemo() {
        // this 调用绕过了代理，事务不生效
        this.createProduct("test", "test", BigDecimal.ONE, 1);
    }
}
