import com.stone.walletmanager.exception.CardNotFoundException;
import com.stone.walletmanager.model.CreditCard;
import com.stone.walletmanager.repository.CreditCardRepository;
import com.stone.walletmanager.repository.UserRepository;
import com.stone.walletmanager.repository.WalletRepository;
import com.stone.walletmanager.service.CreditCardService;
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
    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletRepository walletRepository;

    private CreditCard card;

    @Before
    public void setUp() {
        this.card = new CreditCard();
        card.setCardAmount(500.0);
        card.setCardLimit(1000.0);
        card.setCardNumber("6666");
        creditCardService = new CreditCardService(creditCardRepository, userRepository, walletRepository);
        Mockito.doReturn(card).when(creditCardRepository).getCard(this.card.getCardNumber());
        Mockito.doNothing().when(creditCardRepository).modifyCard(Matchers.anyDouble(), Matchers.anyString());
    }


    @Test(expected = IllegalArgumentException.class)
    public void should_not_pay_credit_card_amount_too_big() throws CardNotFoundException {
        creditCardService.executeCreditCardPayment(10000.0, card.getCardNumber());
    }

}
