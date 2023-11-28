package com.playwithcode.businessbridge.product.service;

import com.playwithcode.businessbridge.product.domain.Product;
import com.playwithcode.businessbridge.product.domain.repository.ProductRepository;
import com.playwithcode.businessbridge.product.dto.response.CustomerProductsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigInteger;

import static com.playwithcode.businessbridge.product.domain.type.ProductStateType.DELETED;
import static com.playwithcode.businessbridge.product.domain.type.ProductStateType.SALES;


@Service
@RequiredArgsConstructor//반드시 필요한 argument 생성으로 final이 붙은 애들을 전부 생성해줌. 필드 추가만 해도 어노테이션이 생성자를 생성해줌.
public class ProductService {//Repository에 있는 기능들을 불러올거임. 데이터를
    //의존성 주입
    private final ProductRepository productRepository;


    private Pageable getPageable(final Integer page) {

        return PageRequest.of(page -1,10, Sort.by("productCode").descending());
    }


    //1.상품 목록 조회 : 페이징, 주문 불가 상품 제외(고객)-조건이 있어서 쿼리메소드로 작성해주고 사용할수 있음.
    @Transactional(readOnly = true )
    public Page<CustomerProductsResponse> getProductList(final Integer page) {

        Page<Product> products = productRepository.findByProductState(getPageable(page), SALES);

        return products.map(product -> CustomerProductsResponse.from(product));


    }


//    //2.상품 목록 조회 : 페이징, 주문 불가 상품 포함 -관리자
//    @Transactional(readOnly = true )
//    public Page<AdminProductsResponse> getAdminProducts(final Integer page) {
//
//        Page<Product> products = productRepository.findByStatusNot(getPageable(page),DELETED);
//
//        return products.map(product -> CustomerProductsResponse.from(product));
//
//
//    }



    //상품목록조회 - 카테고리 기준, 페이징, 주문불가 상품 제외

    @Transactional(readOnly = true )
    public Page<CustomerProductsResponse> getProductsByCategory(final Integer page, final BigInteger categoryCode) {

        Page<Product> products = productRepository.findByProductCategoryAndProductState(getPageable(page),categoryCode, SALES);

        return products.map(product -> CustomerProductsResponse.from(product));


    }


    //-상품 목록 조회 - 상품명 검색 기준, 페이징 주문 불가 상품 제외

    @Transactional(readOnly = true )
    public Page<CustomerProductsResponse> getProductsByProductName(Integer page, String productName) {


        Page<Product> products = productRepository.findByProductNameContainsAndProductState(getPageable(page),productName, SALES);

        return products.map(product -> CustomerProductsResponse.from(product));


    }
}