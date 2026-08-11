package com.training.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.training.persist.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品 Mapper
 * 继承 BaseMapper 即拥有基础 CRUD
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
