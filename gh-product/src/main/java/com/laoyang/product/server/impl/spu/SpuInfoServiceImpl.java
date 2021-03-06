package com.laoyang.product.server.impl.spu;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laoyang.common.vo.es.SkuVO;
import com.laoyang.common.to.SkuReductionTo;
import com.laoyang.common.to.SpuBoundTo;
import com.laoyang.common.util.PageUtils;
import com.laoyang.common.util.Query;
import com.laoyang.common.util.R;
import com.laoyang.common.vo.ware.SkuStockVo;
import com.laoyang.product.config.ProductConstant;
import com.laoyang.product.dao.SpuInfoDao;
import com.laoyang.product.entity.*;
import com.laoyang.product.feign.CouponFeignService;
import com.laoyang.product.feign.SearchFeignService;
import com.laoyang.product.feign.WareFeignService;
import com.laoyang.product.server.inter.*;
import com.laoyang.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService imagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService attrValueService;

    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired()
    CouponFeignService couponFeignService;

    @Resource
    BrandService brandService;

    @Resource
    CategoryService categoryService;

    @Resource
    WareFeignService wareFeignService;


    @Resource
    SearchFeignService searchFeignService;


    /**
     * ????????????
     * ??????spu????????????sku??????
     * ???????????????????????????ESmode?????????
     * ?????????ES
     *
     * @param spuId
     */
    @Override
    public void up(Long spuId) {
        try {
            //TODO ??????????????????????????????????????????
            //???????????????????????? by spuId
            List<ProductAttrValueEntity> productAttrValueEntities = attrValueService.baseAttrlistforspu(spuId);
            //???????????????attrIds
            List<Long> allAttrIdList = productAttrValueEntities.stream()
                    .map(attr ->
                            attr.getAttrId())
                    .collect(Collectors.toList());


            allAttrIdList.add(1L);

            //???attrIds???????????????????????????????????????
            //??????AttrId?????????set
            List<AttrEntity> attrEntities = attrService.selectSearchAttr(allAttrIdList);
            List<Long> searchAttrIds = attrEntities.stream()
                    .map(attr ->
                            attr.getAttrId())
                    .collect(Collectors.toList());
            Set<Long> searchAttrIdsSet = new HashSet<>(searchAttrIds);

            //??????????????????spu ???????????????????????????
            List<SkuVO.Attrs> searchAttrs = productAttrValueEntities.stream()
                    //?????????spu??????????????????????????????????????????
                    .filter(spuAttrId ->
                            searchAttrIdsSet.contains(spuAttrId))
                    //??????????????????vo??????
                    .map(attr -> {
                        SkuVO.Attrs attrs = new SkuVO.Attrs();
                        BeanUtils.copyProperties(attr, attrs);
                        return attrs;
                    })
                    .collect(Collectors.toList());


            //TODO ????????????sku????????????
            final Map<Long, Long> stockMap;
            try {
                R<List<SkuStockVo>> selectSkuStock = wareFeignService.selectSkuStock(allAttrIdList);

                TypeReference<List<SkuStockVo>> type = new TypeReference<List<SkuStockVo>>() {
                };
                if (selectSkuStock.getDate(type) != null) {
                    //??????????????????skuId???key???????????????value
                    stockMap = selectSkuStock.getDate(type).stream()
                            .collect(Collectors.toMap(SkuStockVo::getSkuId, item -> item.getStockTotal()));
                } else {
                    stockMap = Collections.emptyMap();
                }
            } catch (Exception e) {
                throw new RuntimeException("??????wareFeignService???????????????" + e.getMessage(), e);
            }

            //1???????????????sku info
            List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);
            //2???????????????
            List<SkuVO> skuVOList = skus.stream().map(sku -> {
                SkuVO skuVO = new SkuVO();
                BeanUtils.copyProperties(sku, skuVO);
                skuVO.setSkuPrice(sku.getPrice());
                skuVO.setSkuImg(sku.getSkuDefaultImg());

                //????????????info??????
                BrandEntity brand = brandService.getById(sku.getBrandId());
                if (brand != null) {
                    skuVO.setBrandId(brand.getBrandId());
                    skuVO.setBrandImg(brand.getLogo());
                    skuVO.setBrandName(brand.getName());
                }
                //??????????????????
                CategoryEntity category = categoryService.getById(sku.getCatalogId());
                if (category != null) {
                    skuVO.setCatalogId(category.getCatId());
                    skuVO.setCatalogName(category.getName());
                }
                //?????????sku????????????
                skuVO.setAttrs(searchAttrs);

                //???????????????0???
                //???????????????
                skuVO.setHotScore(0L);
                skuVO.setStockTotal(stockMap.get(skuVO.getSkuId()));

                return skuVO;
            }).collect(Collectors.toList());

            //TODO ????????????????????????ES??????
            R res = searchFeignService.productUp(skuVOList);
            if (res.getCode() == 200) {
                log.info("ES????????????ok");
                //???????????????????????????????????????
                int val = baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
            } else {
                log.error("ES????????????????????????! SpuInfoServiceImpl");
            }
        } catch (Exception e) {
            log.debug("????????????????????????" + e);
            log.debug("SpuInfoServiceImpl.up");
            log.debug(e.getStackTrace().toString() + "");
            throw e;
        }
    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        //?????????sku???????????????
        SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);

        //??????spuId
        Long spuId = skuInfoEntity.getSpuId();

        //?????????spuId??????spuInfo?????????????????????
        SpuInfoEntity spuInfoEntity = this.baseMapper.selectById(spuId);

        //???????????????????????????????????????
        BrandEntity brandEntity = brandService.getById(spuInfoEntity.getBrandId());
        spuInfoEntity.setBrandName(brandEntity.getName());

        return spuInfoEntity;
    }

    /**
     *  ??????????????????
     * @param vo
     */
    @Transactional
    @Override

    public void saveSpuInfo(SpuSaveVo vo) {

        //1?????????spu???????????? pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        //2?????????Spu??????????????? pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);


        //3?????????spu???????????? pms_spu_images
        List<String> images = vo.getImages();
        imagesService.saveImages(infoEntity.getId(), images);


        //4?????????spu???????????????;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            AttrEntity id = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(id.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());

            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);


        //5?????????spu??????????????????gulimall_sms->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0) {
            log.error("????????????spu??????????????????");
        }


        //5???????????????spu???????????????sku?????????

        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }
                //    private String skuName;
                //    private BigDecimal price;
                //    private String skuTitle;
                //    private String skuSubtitle;
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                //5.1??????sku??????????????????pms_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity -> {
                    //??????true???????????????false????????????
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //5.2??????sku??????????????????pms_sku_image
                skuImagesService.saveBatch(imagesEntities);
                //TODO ?????????????????????????????????

                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);

                    return attrValueEntity;
                }).collect(Collectors.toList());
                //5.3??????sku????????????????????????pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // //5.4??????sku??????????????????????????????gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode() != 0) {
                        log.error("????????????sku??????????????????");
                    }
                }

            });
        }
    }


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }
        // status=1 and (id=1 or spu_name like xxx)
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }

        /**
         * status: 2
         * key:
         * brandId: 9
         * catelogId: 225
         */
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }


}