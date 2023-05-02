package classes;

public interface EntityIterator {
    boolean hasNext();
    Object getNext();
    void reset();
    int getListSize();
    int getCurrentPosition();
}