package classes;

import java.util.List;

public class GoodIterator implements EntityIterator{
    private List<Good> goodList;
    private int currentPosition = 0;

    public GoodIterator(List<Good> goodList) {
        this.goodList = goodList;
    }

    @Override
    public boolean hasNext() {
        return currentPosition < goodList.size();
    }

    @Override
    public Object getNext() {
        if (!hasNext()) {
            return null;
        }

        return goodList.get(currentPosition++);
    }

    @Override
    public void reset() {
        currentPosition = 0;
    }

    @Override
    public int getListSize() {
        return goodList.size();
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }
}
