package biz.mobidev.android.jaxb.library.parser;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: kulik
 * Date: 10/25/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractElementAdapter implements ElementAdapter {


    protected AbstractElementAdapter(InputStream data) {
        init(data);
    }

    protected AbstractElementAdapter(String data){
        init(data);
    }

    public AbstractElementAdapter() {
    }

    protected abstract void init(InputStream data);

    protected abstract void init(String data);

}
