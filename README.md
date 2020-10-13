# AACSample

## 참고 사이트
https://blog.yena.io/studynote/2019/03/27/Android-MVVM-AAC-2.html   

## 위 사이트의 문제점
MainActivity.kt에서 
<pre>
<code>
contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
</code>
</pre>
실행 도중 ContactViewModel의 인스턴스를 생성하지 못해 앱이 죽는 현상이 발생한다.   

## 해결방안
ViewModelProvider()에 파라미터로 Factory 객체를 넘겨주지 않아서, ContactViewModel의 인스턴스가 제대로 생성되지 않았다. 
문제해결을 위해 구글링을 하던 중, **ViewModelProvider**을 찾게 되었고, 오버로딩 파라미터를 확인해보았다.
<pre>
<code>
/**
 * Creates {@code ViewModelProvider}, which will create {@code ViewModels} via the given
 * {@code Factory} and retain them in a store of the given {@code ViewModelStoreOwner}.
 *
 * @param owner   a {@code ViewModelStoreOwner} whose {@link ViewModelStore} will be used to
 *                retain {@code ViewModels}
 * @param factory a {@code Factory} which will be used to instantiate
 *                new {@code ViewModels}
 */
public ViewModelProvider(@NonNull ViewModelStoreOwner owner, @NonNull Factory factory) {
    this(owner.getViewModelStore(), factory);
}
</code>
</pre>

Factory 객체를 인자로 받으니, 저 Factory를 한번 더 알아보자.
<pre>
<code>
/**
 * Implementations of {@code Factory} interface are responsible to instantiate ViewModels.
 */
public interface Factory {
    /**
     * Creates a new instance of the given {@code Class}.
     * <p>
     *
     * @param modelClass a {@code Class} whose instance is requested
     * @param <T>        The type parameter for the ViewModel.
     * @return a newly created ViewModel
     */
     @NonNull
     <T extends ViewModel> T create(@NonNull Class<T> modelClass);
}

static class OnRequeryFactory {
    void onRequery(@NonNull ViewModel viewModel) {
    }
}
</code>
</pre>

이러한 연유로 ViewModel에 파라미터를 넘기기 위해서 Factory객체를 생성하여 ViewModelProvider에 함께 넘겨주어야 정상적으로 동작을 하겠다.

<pre>
<code>
class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this, ContactViewModel.Factory(application)).get(ContactViewModel::class.java)
    }
//    contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
    contactViewModel = viewModel
}
</code>
</pre>

그러므로 ViewModelProviders가 아닌 **ViewModelProvider**을 활용하여 viewmodel 인스턴스를 생성해주어야 한다.
