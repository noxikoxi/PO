#!/bin/bash

API="http://localhost:9000"

get_curl(){
    local endpoint=$1
    echo $(curl -s -o /dev/null -w "%{http_code}" "$API/$endpoint")
}

post_curl(){
    local endpoint=$1
    local data=$2
    echo $(curl -s -o /dev/null -w "%{http_code}" -X POST "$API/$endpoint" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "$data")
}

test_response(){
    local response=$1
    local exptectedCode=$2

    if [ "$response" -eq "$exptectedCode" ]; then
    echo "Success: Returned code ($exptectedCode)"
else
    echo "Error: Expected $exptectedCode, got $response"
    exit 1
fi
}

echo "Test 1: Products list"
response=$(get_curl "product" )
test_response $response "200"

echo ""

echo "Test 2: Getting product with ID 1"
response=$(get_curl "product/1" )
test_response $response "200"

echo ""

echo "Test 3: Creating new product"

x_www_form_data="product[name]=Nowy_Produkt&product[price]=99&product[category]=1"

response=$(post_curl "product/new" $x_www_form_data )
test_response $response "303"

echo ""

echo "Test 4: Editing product"

x_www_form_data="product[name]=Updated_Produkt&product[price]=99999&product[category]=4"

response=$(post_curl "product/6/edit" $x_www_form_data )
test_response $response "303"

echo ""

echo "Test 5: Deleting product"
response=$(post_curl "product/6" "" )
test_response $response "403"

echo ""

echo "Test 6: Category list"
response=$(get_curl "category" )
test_response $response "200"

echo ""

echo "Test 7: Getting category with ID 1"
response=$(get_curl "category/1" )
test_response $response "200"

echo ""

echo "Test 8: Creating new category"

x_www_form_data="category[name]=Nowa_Kategoria"

response=$(post_curl "category/new" $x_www_form_data )
test_response $response "303"

echo ""

echo "Test 9: Editing category"

x_www_form_data="category[name]=Zmiana_Nazwy"

response=$(post_curl "category/4/edit" $x_www_form_data )
test_response $response "303"

echo ""

echo "Test 10: Deleting category"
response=$(post_curl "category/1" "" )
test_response $response "403"

echo ""

echo "Test 11: CartItem list"
response=$(get_curl "cart-item" )
test_response $response "200"

echo ""

echo "Test 12: Getting CartItem with ID 1"
response=$(get_curl "cart-item/1" )
test_response $response "200"

echo ""

echo "Test 13: Creating new CartItem"

x_www_form_data="cart_item[cart]=3&cart_item[product]=3&cart_item[quantity]=1"

response=$(post_curl "cart-item/new" $x_www_form_data )
test_response $response "303"

echo ""

echo "Test 14: Editing CartItem"

x_www_form_data="cart_item[cart]=3&cart_item[product]=2&cart_item[quantity]=999"

response=$(post_curl "cart-item/1/edit" $x_www_form_data )
test_response $response "303"

echo ""

echo "Test 15: Deleting CartItem"
response=$(post_curl "cart-item/1" "" )
test_response $response "403"

echo ""

echo "Test 16: Cart list"
response=$(get_curl "cart" )
test_response $response "200"

echo ""

echo "Test 17: Getting Cart with ID 1"
response=$(get_curl "cart/3" )
test_response $response "200"

echo ""

echo "Test 18: Creating new Cart"

response=$(get_curl "cart/new")
test_response $response "303"

echo ""

echo "Test 19: Deleting Cart"
response=$(post_curl "cart/2/delete" "" )
test_response $response "403"

