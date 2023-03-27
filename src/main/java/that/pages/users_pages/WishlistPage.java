package that.pages.users_pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import that.composites.products.WishlistProduct;
import that.pages.AbstractPage;

import java.util.List;
import java.util.stream.Collectors;

public class WishlistPage extends AbstractPage {
    @FindBy(css = "main .item")
    private ElementsCollection productsList;

    public List<WishlistProduct> getProducts() {
        return productsList
                .stream()
                .map(WishlistProduct::new)
                .collect(Collectors.toList());
    }

    public Boolean isProductsCount(int productCount){
        productsList.shouldBe(CollectionCondition.size(productCount));
        return true;
    }

    public void removeAllProducts(){
        for (int i = getProducts().size(); i>0; i--){
            SelenideElement productToDelete = productsList.shouldBe(CollectionCondition.size(i)).get(0);
            new WishlistProduct(productToDelete).removeFromWishlist();
        }
        productsList.shouldBe(CollectionCondition.size(0));
    }
}
