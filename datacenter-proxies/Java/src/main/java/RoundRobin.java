import java.util.Iterator;
import java.util.List;

public class RoundRobin<T> implements Iterable<T> {
    private final List<T> list;

    public RoundRobin(List<T> list) {
        this.list = list;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                T item = list.get(index);
                index = (index + 1) % list.size();
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }
}