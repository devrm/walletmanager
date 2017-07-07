import com.stone.walletmanager.model.CreditCard;
import com.stone.walletmanager.repository.CreditCardRepository;
import com.stone.walletmanager.service.CreditCardService;
import org.dom4j.IllegalAddException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by rodrigo.mafra on 06/07/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreditCardPaymentTest {


    private CreditCardService creditCardService;

    @Mock
    private CreditCardRepository creditCardRepository;
    private CreditCard card;

    @Before
    public void setUp() {
        this.card = new CreditCard();
        card.setCardAmount(500.0);
        card.setCardLimit(1000.0);
        card.setCardNumber("6666");
        creditCardService = new CreditCardService(creditCardRepository);
        Mockito.doNothing().when(creditCardRepository).modifyCard(Matchers.anyDouble(), Matchers.anyString());
    }


    @Test(expected = IllegalArgumentException.class)
    public void should_not_pay_credit_card_amount_too_big() {
        creditCardService.executeCreditCardPayment(10000.0, card);
    }


    @Test(expected = IllegalArgumentException.class)
    public void should_not_pay_credit_card_negative_amount() {
        creditCardService.executeCreditCardPayment(-10000.0, card);
    }

}
