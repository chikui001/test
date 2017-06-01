import android.test.suitebuilder.annotation.SmallTest;
import com.example.jiao.myapplication.LoginPresenter;
import com.example.jiao.myapplication.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class LoginPresenterTest {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void login() {

		List mockedList = mock(List.class);
		//using mock object
		mockedList.add("one");
		mockedList.remove(0);
		mockedList.clear();
		//verification
		verify(mockedList).add("one");
		verify(mockedList).clear();

	}
}